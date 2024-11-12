package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.emil.linksy.presentation.viewmodel.UserProfileDataViewModel
import com.emil.presentation.R
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProfileFragment : Fragment() {
private lateinit var usernameTextView:TextView
private lateinit var avatarImageView:ImageView
   private lateinit var sharedPref: SharedPreferences
    private val userProfileDataViewModel: UserProfileDataViewModel by viewModel<UserProfileDataViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        usernameTextView = view.findViewById(R.id.tv_username)
        avatarImageView = view.findViewById(R.id.iv_user_avatar)
        sharedPref = requireContext().getSharedPreferences("TokenData", Context.MODE_PRIVATE)
         val token = sharedPref.getString("ACCESS_TOKEN",null)
        userProfileDataViewModel.userData.observe(requireActivity()){
            data ->
                usernameTextView.text = data.username
            if (data.avatarUrl != "null") {
                Glide.with(requireContext())
                    .load(data.avatarUrl)
                    .into(avatarImageView)
            }
        }
        userProfileDataViewModel.getData(token!!, onSuccess = {

        }, onIncorrect = {} , onError = {}, onEnd = {})
        return view
    }

}