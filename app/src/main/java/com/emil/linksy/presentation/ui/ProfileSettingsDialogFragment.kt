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
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileSettingsDialogFragment: DialogFragment() {
    private lateinit var changeAvatarImageView: ImageView
    private lateinit var changePasswordTextView: MaterialTextView
    private lateinit var linkExistTextView: MaterialTextView
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
    private var selectedUri: Uri? = null
    private var shouldDeletePhoto:Boolean = false
    private var avatarExist:Boolean = false
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
        saveButton.setOnClickListener {
            val token = sharedPref.getString("ACCESS_TOKEN",null).toString()
            val birthday = birthdayEditText.string()
            if(selectedUri!=null) {
                val filePart = createFilePart(selectedUri!!, requireContext())
                profileManagementViewModel.uploadAvatar(token = token, filePart, onSuccess = {}, onIncorrect = {}, onError = {})
            }
            if (shouldDeletePhoto){
                profileManagementViewModel.deleteAvatar(token = token,onSuccess = {}, onIncorrect = {}, onError = {})
            }
            if (birthday.isNotEmpty()) {
                profileManagementViewModel.updateBirthday(token = token, birthday = birthday,onSuccess = {},onIncorrect = {}, onError = {})
            }
            val username = usernameEditText.string()
               profileManagementViewModel.updateUsername(token = token, username = username,onSuccess = {},onIncorrect = {}, onError = {})

            val link = linkEditText.string()
            if (link.isNotEmpty()) {
                profileManagementViewModel.updateLink(token, link, onSuccess = {},
                    onIncorrect = {
                        changeEditTextBackgroundColor(
                            requireContext(),
                            BackgroundState.ERROR,
                            linkEditText
                        )
                        linkExistTextView.show()

                    },
                    onError = {})
            }

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
            usernameEditText.setText(  data.username)
            emailEditText.setText(data.email)
            linkEditText.setText(if (data.link.isNullOrEmpty()) ""
            else data.link)
            birthdayEditText.setText(if (data.birthday.isNullOrEmpty()) ""
            else data.birthday)
            if (data.avatarUrl != "null") {
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
                        true
                    }

                    2 -> {
                        shouldDeletePhoto = true
                        selectedUri=null
                        avatarExist = false
                        avatarImageView.setImageResource(R.drawable.default_avatar)
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
        profileManagementViewModel.getData(token, onIncorrect = {} ,
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
        return MultipartBody.Part.createFormData("file", fileName, fileBody)
    }
}