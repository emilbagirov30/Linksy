package com.emil.linksy.presentation.custom_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.emil.presentation.R

class CustomAudioWave @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val wavePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = ContextCompat.getColor(context, R.color.audio_wave)
        style = Paint.Style.FILL
    }

    private val amplitudes = mutableListOf<Float>()
    private val maxAmplitudes = 50
    private val cornerRadius = 10f
    private val rectF = RectF()

    fun addAmplitude(amplitude: Float) {
        val normalizedAmplitude = amplitude / Short.MAX_VALUE
        amplitudes.add(normalizedAmplitude)
        if (amplitudes.size > maxAmplitudes) {
            amplitudes.removeAt(0)
        }
        invalidate()
    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (amplitudes.isEmpty()) return

        val widthPerAmplitude = width.toFloat() / maxAmplitudes
        val centerY = height / 2f

        for (i in 0 until maxAmplitudes) {
            val amplitude = amplitudes.getOrElse(i) { 0f } * height / 2
            val left = i * widthPerAmplitude
            val right = left + widthPerAmplitude * 0.8f
            rectF.set(left, centerY - amplitude, right, centerY + amplitude)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, wavePaint)
        }
    }
}
