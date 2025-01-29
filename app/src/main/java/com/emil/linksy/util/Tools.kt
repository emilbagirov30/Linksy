package com.emil.linksy.util
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import com.emil.presentation.R
import com.facebook.shimmer.Shimmer
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.File
import java.io.InputStream

class Linksy {
    companion object {
        const val PASSWORD_MIN_LENGTH = 6
        const val CODE_MIN_LENGTH = 5
        const val MOMENT_LONG_DURATION = 15
        const val MOMENT_SHORT_DURATION = 5
        const val REFRESH_DELAY = 5L
        const val MAX_FILE_SIZE = 50 * 1024 * 1024
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

enum class RelationType() {
    SUBSCRIBERS,
    SUBSCRIPTIONS,
    REQUESTS,
    CHANNEL_MEMBERS,
    LIKES,
    ADD_MEMBERS
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
    val fileSize: Long
    try {
        val fileDescriptor = contentResolver.openAssetFileDescriptor(uri, "r") ?: return null
        fileSize = fileDescriptor.length
        fileDescriptor.close()

        if (fileSize > Linksy.MAX_FILE_SIZE) {
            showToast(context,R.string.big_file)
            return null
        }
        inputStream = contentResolver.openInputStream(uri) ?: return null

    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
    val fileName = "${fieldName}_${System.currentTimeMillis()}.${mimeType.substringAfter("/")}"

    val fileBody = object : RequestBody() {
        @SuppressLint("Recycle")
        override fun contentLength(): Long {
            return contentResolver.openAssetFileDescriptor(uri, "r")?.length ?: 0
        }

        override fun contentType(): MediaType? {
            return MediaType.parse(mimeType)
        }

        override fun writeTo(sink: BufferedSink) {
            val buffer = ByteArray(8192)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                sink.write(buffer, 0, bytesRead)
            }
            inputStream.close()
        }
    }

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


fun createContentPickerForFragment(fragment: Fragment, action: (Uri) -> Unit) =
    fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { action(it) }
    }

fun createContentPickerForActivity(activity: AppCompatActivity, action: (Uri) -> Unit) =
    activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { action(it) }
    }


fun trimMedia(context: Context, uri: Uri, durationLimitSec: Int = Linksy.MOMENT_LONG_DURATION): Uri? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(context, uri)
    val durationMillis = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLongOrNull() ?: 0
    retriever.release()

    if (durationMillis <= durationLimitSec * 1000) {
        return uri
    }

    val outputFile = File(context.cacheDir, "trimmed_${System.currentTimeMillis()}_${getFileExtension(uri)}")
    val inputPath = LinksyFileUtils.getPath(context, uri) ?: return null
    val outputPath = outputFile.absolutePath

    val command = buildCommand(inputPath, outputPath, durationLimitSec)

    val session = FFmpegKit.execute(command)

    return if (ReturnCode.isSuccess(session.returnCode)) {
        Uri.fromFile(outputFile)
    } else {
        null
    }
}

fun buildCommand(inputPath: String, outputPath: String, durationLimitSec: Int): String {
    return if (inputPath.endsWith(".mp3", true)) {

        "-i $inputPath -ss 0 -t $durationLimitSec -c copy $outputPath"
    } else {
        "-i $inputPath -ss 0 -t $durationLimitSec -c:v copy -c:a copy $outputPath"
    }
}

fun getFileExtension(uri: Uri): String {
    return when (val extension = uri.lastPathSegment?.substringAfterLast('.')) {
        "mp3" -> ".mp3"
        "mp4" -> ".mp4"
        else -> ".mp4"
    }
}

object LinksyFileUtils {
    fun getPath(context: Context, uri: Uri): String? {
        val projection = arrayOf(android.provider.MediaStore.MediaColumns.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(projection[0])
                return it.getString(columnIndex)
            }
        }
        return null
    }
}

@SuppressLint("ClickableViewAccessibility", "InflateParams")
fun View.showHint (context: Context, text:Int){
    val popupView = LayoutInflater.from(context).inflate(R.layout.popup_hint_moment, null)
    val popupWindow = PopupWindow(popupView,
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT)
    val textView = popupView.findViewById<TextView>(R.id.tv_popup)
    textView.text = context.getString(text)
    popupWindow.isOutsideTouchable = true
    popupWindow.isFocusable = true
    popupWindow.showAsDropDown(this)
    popupView.setOnTouchListener { _, _ ->
        popupWindow.dismiss()
        true
    }
}



fun View.showMenu(context: Context, editAction: (() -> Unit)? = null, deleteAction: (() -> Unit)? = null) {
    val popupMenu = PopupMenu(context, this)
    val menu = popupMenu.menu

    if (editAction != null) {
        menu.add(0, 1, 0, context.getString(R.string.edit))
    }

    if (deleteAction != null) {
        val deleteMenuItem = menu.add(0, 2, 1, context.getString(R.string.delete))
        val spannableTitle = SpannableString(deleteMenuItem.title)
        spannableTitle.setSpan(
            ForegroundColorSpan(Color.RED),
            0,
            spannableTitle.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        deleteMenuItem.title = spannableTitle
    }

    popupMenu.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            1 -> {
                editAction?.invoke()
                true
            }

            2 -> {
                deleteAction?.invoke()
                true
            }

            else -> false
        }
    }
    popupMenu.show()
}

fun generateQRCode(data: String, imageView: ImageView) {

    /*
Copyright (C) 2012-2022 ZXing authors, Journey Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

    val qrCodeWriter = QRCodeWriter()
    val hints = mutableMapOf<EncodeHintType, Any>()
    hints[EncodeHintType.MARGIN] = 1
    val bitMatrix: BitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 512, 512, hints)

    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
    }
    imageView.setImageBitmap(bmp)
}





