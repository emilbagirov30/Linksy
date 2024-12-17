package com.emil.linksy.util
import android.app.Activity
import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Build
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.emil.presentation.R
import com.facebook.shimmer.Shimmer
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.InputStream

class Linksy {
    companion object {
        const val PASSWORD_MIN_LENGTH = 6
        const val CODE_MIN_LENGTH = 5
        val CUSTOM_SHIMMER: Shimmer? = Shimmer.ColorHighlightBuilder()
            .setBaseColor(Color.GRAY)
            .setHighlightColor(Color.WHITE)
            .setWidthRatio(1.5f)
            .setIntensity(0.01f)
            .setDuration(1200)
            .build()
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

enum class ContentType(val mimeType: String) {
    IMAGE("image/*"),
    VIDEO("video/*"),
    AUDIO("audio/*");

    override fun toString(): String = mimeType
}

private fun createFilePart(uri: Uri, context: Context, fieldName: String, mimeType: String): MultipartBody.Part? {
    val contentResolver = context.contentResolver
    val inputStream: InputStream
    try {
        inputStream = contentResolver.openInputStream(uri)!!
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

    val fileName = "${fieldName}_${System.currentTimeMillis()}.${mimeType.substringAfter("/")}"
    val fileBody = RequestBody.create(MediaType.parse(mimeType), inputStream.readBytes())
    inputStream.close()

    return MultipartBody.Part.createFormData(fieldName, fileName, fileBody)
}

fun createImageFilePart(context: Context, uri: Uri): MultipartBody.Part? {
    return createFilePart(uri, context, "image", "image/png")
}

fun createVideoFilePart(context: Context, uri: Uri): MultipartBody.Part? {
    return createFilePart(uri, context, "video", "video/mp4")
}

fun createAudioFilePart(context: Context, uri: Uri): MultipartBody.Part? {
    return createFilePart(uri, context, "audio", "audio/mpeg")
}

fun createVoiceFilePart(context: Context, uri: Uri): MultipartBody.Part? {
    return createFilePart(uri, context, "voice", "audio/wav")
}


fun createContentPicker(fragment: Fragment, action: (Uri) -> Unit) =
    fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { action(it) }
    }
