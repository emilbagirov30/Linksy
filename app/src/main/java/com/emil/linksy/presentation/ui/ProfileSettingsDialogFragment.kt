package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.emil.linksy.presentation.viewmodel.ProfileManagementViewModel
import com.emil.linksy.util.BackgroundState
import com.emil.linksy.util.Linksy
import com.emil.linksy.util.changeEditTextBackgroundColor
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.linksy.util.string
import com.emil.presentation.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.source
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileSettingsDialogFragment: DialogFragment() {
    private lateinit var changeAvatarImageView: ImageView
    private lateinit var changePasswordTextView: MaterialTextView
    private lateinit var linkExistTextView: MaterialTextView
    private lateinit var usernameShortTextView: MaterialTextView
    private lateinit var toolBar: MaterialToolbar
    private lateinit var shimmerAvatar: ShimmerFrameLayout
    private lateinit var shimmerContent: ShimmerFrameLayout
    private lateinit var contentLinearLayout:LinearLayout
    private lateinit var sharedPref: SharedPreferences
    private lateinit var usernameEditText:EditText
    private lateinit var emailEditText:EditText
    private lateinit var linkEditText:EditText
    private lateinit var birthdayEditText:EditText
    private lateinit var avatarFrameLayout: FrameLayout
    private lateinit var avatarImageView:ImageView
    private lateinit var saveButton:MaterialButton
    private val profileManagementViewModel:ProfileManagementViewModel by viewModel<ProfileManagementViewModel>()
    private lateinit var currentUsername:String
    private lateinit var currentAvatar:String
    private lateinit var currentLink:String
    private lateinit var currentBirthday:String
    private var selectedUri: Uri? = null
    private var shouldDeletePhoto:Boolean = false
    private var avatarExist:Boolean = false
    private var currentToast: Toast? = null
    @SuppressLint("MissingInflatedId", "ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.profile_settings_dialog, container, false)
        changeAvatarImageView = view.findViewById(R.id.iv_change_avatar)
        toolBar = view.findViewById(R.id.tb_edit_data)
        changePasswordTextView = view.findViewById(R.id.tv_change_password)
        linkExistTextView = view.findViewById(R.id.tv_error_link_exist)
        usernameShortTextView = view.findViewById(R.id.tv_error_username_short)
        shimmerAvatar = view.findViewById(R.id.shimmer_avatar)
        shimmerContent = view.findViewById(R.id.shimmer_content)
        contentLinearLayout = view.findViewById(R.id.ll_content)
        usernameEditText = view.findViewById(R.id.et_username)
        emailEditText = view.findViewById(R.id.et_email)
        linkEditText = view.findViewById(R.id.et_link)
        birthdayEditText = view.findViewById(R.id.et_birthday)
        avatarFrameLayout = view.findViewById(R.id.fl_avatar)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        saveButton = view.findViewById(R.id.bt_save)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
        fetchData()
        val usernameTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                saveButton.isEnabled = s.toString()!=currentUsername
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        val linkTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {  saveButton.isEnabled = s.toString()!=currentLink }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        val birthdayTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {saveButton.isEnabled = s.toString()!=currentBirthday }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        usernameEditText.addTextChangedListener(usernameTextWatcher)
        linkEditText.addTextChangedListener(linkTextWatcher)
        birthdayEditText.addTextChangedListener(birthdayTextWatcher)
        saveButton.setOnClickListener {
            hideAllError()
            val token = sharedPref.getString("ACCESS_TOKEN",null).toString()
            val birthday = birthdayEditText.string()
            val link = linkEditText.string()
            val username = usernameEditText.string()
            if(selectedUri!=null) {
                val filePart = createFilePart(selectedUri!!, requireContext())
                profileManagementViewModel.uploadAvatar(token = token, filePart, onSuccess = { selectedUri=null}, onError = {displayError()})
            }
            if (shouldDeletePhoto){
                profileManagementViewModel.deleteAvatar(token = token,onSuccess = {shouldDeletePhoto = false}, onError = {displayError()})
            }
            if (birthday!=currentBirthday) {
                profileManagementViewModel.updateBirthday(token = token, birthday = birthday,onSuccess = {currentBirthday=birthday}, onError = {displayError()})
            }

            if (username!=currentUsername){
                if (username.isNotEmpty()) {
                    profileManagementViewModel.updateUsername(token = token, username = username, onSuccess = { currentUsername = username },
                        onError = {displayError()})
                }
                else usernameShortTextView.show()

            }

            if (link!=currentLink) {
                profileManagementViewModel.updateLink(token, link, onSuccess = {currentLink=link},
                    onIncorrect = { linkExistTextView.show() },
                    onError = {displayError()})
            }

            saveButton.isEnabled = false
        }
        birthdayEditText.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val dialogView = layoutInflater.inflate(R.layout.dialog_date_spinner, null)
                val datePicker = dialogView.findViewById<DatePicker>(R.id.datePicker)

                val alertDialog = AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .setTitle(getString(R.string.indicate_date))
                    .setPositiveButton(getString(R.string.save)) { _, _ ->
                        val selectedYear = datePicker.year
                        val selectedMonth = datePicker.month + 1
                        val selectedDay = datePicker.dayOfMonth
                        val formattedDate = String.format("%02d.%02d.%04d", selectedDay,selectedMonth,selectedYear)
                        birthdayEditText.setText(formattedDate)
                    }
                    .setNegativeButton(getString(R.string.cancel), null)
                    .create()

                alertDialog.show()
                true
            } else {
                false
            }
        }

        profileManagementViewModel.userData.observe(requireActivity()){ data ->
            currentUsername = data.username
            usernameEditText.setText(currentUsername)
            emailEditText.setText(data.email)
            currentLink = if (data.link==null) "" else data.link!!
            linkEditText.setText(currentLink)
            currentBirthday = if (data.birthday.isNullOrEmpty()) "" else data.birthday.toString()
            birthdayEditText.setText(currentBirthday)
            currentAvatar = data.avatarUrl
            if (currentAvatar != "null") {
                avatarExist = true
                Glide.with(requireContext())
                    .load(data.avatarUrl)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
            }
            showContent()
        }
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.circleCropTransform())
                    .into(avatarImageView)
                shouldDeletePhoto = false
                handleSelectedImage(uri)
            }
        }
        changePasswordTextView.setOnClickListener {
           PasswordChangeDialogFragment().show(parentFragmentManager, "ChangePasswordDialogFragment")
        }
        changeAvatarImageView.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), changeAvatarImageView)
            val menu = popupMenu.menu
            menu.add(0, 1, 0, getString(R.string.upload))
            if (avatarExist) {
                menu.add(0, 2, 1, getString(R.string.delete))
                val menuItem = menu.findItem(2)
                val spannableTitle = SpannableString(menuItem.title)
                spannableTitle.setSpan(ForegroundColorSpan(Color.RED), 0, spannableTitle.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                menuItem.title = spannableTitle
            }
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    1 -> {
                        pickImageLauncher.launch("image/*")
                        avatarExist = true
                        shouldDeletePhoto = false
                        true
                    }

                    2 -> {
                        shouldDeletePhoto = true
                        selectedUri=null
                        avatarExist = false
                        avatarImageView.setImageResource(R.drawable.default_avatar)
                        saveButton.isEnabled = true
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()


        }
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }
        return view
    }
    @SuppressLint("Recycle", "NewApi")
    private fun handleSelectedImage(uri: Uri) {
        selectedUri = uri
        saveButton.isEnabled = true
    }
    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(false)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }
    private fun showContent (){
        shimmerAvatar.stopShimmer()
        shimmerContent.stopShimmer()
        shimmerContent.setShimmer(null)
        shimmerAvatar.setShimmer(null)
        shimmerContent.hide()
        shimmerAvatar.hide()
        avatarFrameLayout.show()
        contentLinearLayout.show()
    }
    private fun fetchData() {
        startShimmer()
        val token = sharedPref.getString("ACCESS_TOKEN",null).toString()
        profileManagementViewModel.getData(token, onIncorrect = {showToast(requireContext(),R.string.error_invalid_token) } ,
            onError = {
                stopShimmer()
                if (isAdded && view != null) {
                    Snackbar.make(requireView(), getString(R.string.error_loading_data), Snackbar.LENGTH_INDEFINITE).apply {
                        setBackgroundTint(Color.WHITE)
                        setTextColor(Color.GRAY)
                        setAction(getString(R.string.repeat)) { fetchData() }
                        setActionTextColor(Color.BLUE)
                        show()
                    }
                }
            })
    }
    private fun startShimmer(){
        shimmerAvatar.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerContent.setShimmer(Linksy.CUSTOM_SHIMMER)
        shimmerContent.startShimmer()
        shimmerAvatar.startShimmer()
    }
    private fun stopShimmer (){
        shimmerAvatar.stopShimmer()
        shimmerContent.stopShimmer()
    }
    private fun createFilePart(uri: Uri, context: Context): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)!!
        val fileName = "avatar_${System.currentTimeMillis()}.png"
        val fileBody = RequestBody.create(MediaType.parse("image/png"), inputStream.readBytes())
        inputStream.close()
        return MultipartBody.Part.createFormData("file", fileName, fileBody)
    }
    private fun hideAllError(){
        linkExistTextView.hide()
        usernameShortTextView.hide()
    }
    private fun displayError(){
        currentToast?.cancel()
        currentToast = Toast.makeText(requireContext(), R.string.failed_connection, Toast.LENGTH_SHORT)
        currentToast?.show()
    }
}