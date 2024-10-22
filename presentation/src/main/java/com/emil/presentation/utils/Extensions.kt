package com.emil.presentation.utils
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.emil.presentation.R
import androidx.fragment.app.Fragment

fun Fragment.replaceFragment(fragment: Fragment) {
    val transaction = requireActivity().supportFragmentManager.beginTransaction()
    transaction.replace(R.id.fragment_container, fragment)
    transaction.commit()
}
fun EditText.string():String = this.text.toString().trim()
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

fun changeEditTextBackgroundColor(context: Context, state: BackgroundState, vararg editTexts: EditText){
    val colorResId = when (state) {
        BackgroundState.ERROR -> ContextCompat.getColor(context, R.color.edit_text_background_error_color)
        BackgroundState.DEFAULT -> ContextCompat.getColor(context, R.color.edit_text_background_default_color)
    }
    val drawable = editTexts[0].background.mutate()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        drawable.colorFilter = BlendModeColorFilter(colorResId, BlendMode.SRC_IN)
    }else{
        drawable.colorFilter = PorterDuffColorFilter (colorResId, PorterDuff.Mode.SRC_IN)
    }
    for (editText in editTexts) {
        editText.background = drawable
    }

}