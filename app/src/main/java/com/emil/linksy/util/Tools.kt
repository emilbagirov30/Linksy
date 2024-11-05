package com.emil.linksy.util
import android.app.Activity
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.emil.presentation.R

class Linksy {
    companion object {
        const val PASSWORD_MIN_LENGTH = 6
    }
}


fun togglePasswordVisibility(passwordEditText: EditText, toggleButton: ImageView) {

    if (passwordEditText.transformationMethod is PasswordTransformationMethod) {
        passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
        toggleButton.setImageResource(R.drawable.hide_password)
    } else {
        passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
        toggleButton.setImageResource(R.drawable.show_password)
    }
    passwordEditText.setSelection(passwordEditText.text.length)

}
enum class BackgroundState { ERROR, DEFAULT }

fun changeEditTextBackgroundColor(context: Context, state: BackgroundState, vararg editTexts: EditText) {
    val colorResId = when (state) {
        BackgroundState.ERROR -> ContextCompat.getColor(context, R.color.edit_text_background_error_color)
        BackgroundState.DEFAULT -> ContextCompat.getColor(context, R.color.edit_text_background_default_color)
    }

    for (editText in editTexts) {

        val drawable = editText.background.mutate().constantState?.newDrawable()?.mutate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable?.colorFilter = BlendModeColorFilter(colorResId, BlendMode.SRC_IN)
        } else {
            drawable?.colorFilter = PorterDuffColorFilter(colorResId, PorterDuff.Mode.SRC_IN)
        }
        editText.background = drawable
    }

}

fun showToast(context: Context,message:Int){
    Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
}

fun hideKeyboard (context: Context,view: View){
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}
