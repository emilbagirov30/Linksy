package com.emil.presentation

import androidx.fragment.app.Fragment

fun Fragment.replaceFragment(fragment: Fragment) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container, fragment)
    transaction.commit()
}
