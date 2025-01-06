package com.emil.linksy.presentation.ui.navigation.people

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.emil.linksy.adapters.UsersAdapter
import com.emil.linksy.presentation.ui.CameraXActivity
import com.emil.linksy.presentation.ui.auth.LanguageSelectionActivity
import com.emil.linksy.presentation.viewmodel.PeopleViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.showHint
import com.emil.presentation.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchPeopleFragment : Fragment() {
    private lateinit var hintImageButton: ImageButton
    private lateinit var scanImageButton: ImageButton
    private lateinit var searchEditText: EditText
    private lateinit var userRecyclerView: RecyclerView
    private val peopleViewModel:PeopleViewModel by viewModel<PeopleViewModel>()
    private val tokenManager: TokenManager by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_search_people, container, false)
        hintImageButton = view.findViewById(R.id.ib_hint)
        scanImageButton = view.findViewById(R.id.ib_scan)
        searchEditText = view.findViewById(R.id.et_search)
        userRecyclerView = view.findViewById(R.id.rv_users)
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        peopleViewModel.userList.observe(requireActivity()){ userlist ->
            userRecyclerView.adapter =
                UsersAdapter(userList = userlist,peopleViewModel = peopleViewModel,context = requireContext(),tokenManager = tokenManager)
        }
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val currentText = it.toString()
                    if (currentText.startsWith("@")) {
                       peopleViewModel.findByLink(tokenManager.getAccessToken(),currentText)
                    }else{
                        peopleViewModel.findByUsername(tokenManager.getAccessToken(),currentText)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        searchEditText.addTextChangedListener(textWatcher)
        hintImageButton.setOnClickListener {
            it.anim()
            it.showHint(requireContext(),R.string.user_search_hint)
        }
        scanImageButton.setOnClickListener {
            it.anim()
            val switchingToCameraXActivity= Intent(requireContext(), CameraXActivity::class.java)
            startActivity(switchingToCameraXActivity)
        }
        return view
    }

}