package com.emil.linksy.util

import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import com.emil.presentation.R

fun Fragment.replaceFragment(containerId:Int,fragment: Fragment) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    transaction.replace(containerId, fragment)
    transaction.commit()
}


fun FragmentActivity.replaceFragment(containerId:Int,fragment: Fragment,tag:String) {
    supportFragmentManager.commit {
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        replace(containerId, fragment, tag)
    }
}

fun EditText.string():String = this.text.toString().trim()

fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return this.matches(emailRegex)
}
fun View.hide (){
    this.visibility = View.GONE
}
fun View.show (){
    this.visibility = View.VISIBLE
}