package com.emil.linksy.util

import android.view.View
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.emil.presentation.R

fun Fragment.replaceFragment(fragment: Fragment) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container, fragment)
    transaction.commit()
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