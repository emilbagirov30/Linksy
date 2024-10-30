package com.emil.linksy.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatImageView
import com.emil.presentation.R

class CustomProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private val animation = AnimationUtils.loadAnimation(context, R.anim.loading_anim)

    init {
        setBackgroundResource(R.drawable.custom_loaging_backgroung)
        startAnimation(animation)
    }

    fun visible() {
        visibility = VISIBLE
        startAnimation(animation)
    }

    fun gone() {
        clearAnimation()
        visibility = GONE
    }
}
