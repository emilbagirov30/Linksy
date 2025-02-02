package com.emil.linksy.presentation.custom_view

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
    private var widthPerAmplitude = 0f
    private var centerY = 0f
    private var left = 0f
    private var right = 0f
    private var amplitude = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        widthPerAmplitude = w.toFloat() / maxAmplitudes
        centerY = h / 2f
    }

    fun addAmplitude(amplitude: Float) {
        val normalizedAmplitude = amplitude / Short.MAX_VALUE
        amplitudes.add(normalizedAmplitude)
        if (amplitudes.size > maxAmplitudes) {
            amplitudes.removeAt(0)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (amplitudes.isEmpty()) return

        for (i in 0 until maxAmplitudes) {
            amplitude = amplitudes.getOrElse(i) { 0f } * height / 2
            left = i * widthPerAmplitude
            right = left + widthPerAmplitude * 0.8f
            rectF.set(left, centerY - amplitude, right, centerY + amplitude)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, wavePaint)
        }
    }
}
