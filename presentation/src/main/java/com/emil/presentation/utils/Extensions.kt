package com.emil.presentation.utils
import android.widget.EditText
import com.emil.presentation.R
import androidx.fragment.app.Fragment

fun Fragment.replaceFragment(fragment: Fragment) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container, fragment)
    transaction.commit()
}
fun EditText.string():String = this.text.toString().trim()
