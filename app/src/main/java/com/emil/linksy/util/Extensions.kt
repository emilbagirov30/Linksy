package com.emil.linksy.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
fun View.anim() {
    val scaleXDown = ObjectAnimator.ofFloat(this, View.SCALE_X, 1f, 0.5f)
    val scaleYDown = ObjectAnimator.ofFloat(this, View.SCALE_Y, 1f, 0.5f)
    val scaleXUp = ObjectAnimator.ofFloat(this, View.SCALE_X, 0.5f, 1f)
    val scaleYUp = ObjectAnimator.ofFloat(this, View.SCALE_Y, 0.5f, 1f)

    scaleXDown.duration = 200
    scaleYDown.duration = 200
    scaleXUp.duration = 200
    scaleYUp.duration = 200

    val animatorSet = AnimatorSet()
    animatorSet.play(scaleXDown).with(scaleYDown)
    animatorSet.play(scaleXUp).with(scaleYUp).after(scaleXDown)
    animatorSet.start()
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