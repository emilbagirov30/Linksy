package com.emil.linksy.util

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.*
import java.io.*

class AudioRecorderManager(private val context: Context) {
    private val sampleRate = 44100
    private val bufferSize = AudioRecord.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_IN_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    ).coerceAtLeast(1024)
    private var recordedFile: File? = null
    private var audioRecord: AudioRecord? = null
    private var isRecording = false
    private var fileOutputStream: OutputStream? = null

    @SuppressLint("MissingPermission")
    fun startRecording(onAmplitudeUpdate: (Float) -> Unit) {
        if (audioRecord == null) {
            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )

            if (audioRecord?.state != AudioRecord.STATE_INITIALIZED) {
                throw IllegalStateException("AudioRecord failed to initialize")
            }
        }

        recordedFile = File(context.cacheDir, "recorded_audio.wav")
        fileOutputStream = recordedFile?.outputStream()
        audioRecord?.startRecording()
        isRecording = true

        CoroutineScope(Dispatchers.IO).launch {
            val buffer = ShortArray(bufferSize / 2)
              while (isRecording) {
                val readSize = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                if (readSize > 0) {
                    val maxAmplitude = buffer.maxOrNull()?.toFloat() ?: 0f
                    onAmplitudeUpdate(maxAmplitude)
                    val byteArray = buffer.toByteArray()
                    fileOutputStream?.write(byteArray)
                }
                delay(5)
            }
        }
    }

    private fun ShortArray.toByteArray(): ByteArray {
        val byteArray = ByteArray(this.size * 2)
        for (i in this.indices) {
            val shortValue = this[i]
            byteArray[i * 2] = (shortValue.toInt() and 0xFF).toByte()
            byteArray[i * 2 + 1] = ((shortValue.toInt() shr 8) and 0xFF).toByte()
        }
        return byteArray
    }

    fun stopRecording() {
        isRecording = false
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        fileOutputStream?.flush()
        fileOutputStream?.close()
        fileOutputStream = null
        recordedFile?.let { writeWavHeader(it) }
    }

    fun getRecordedFile(): File? {
        return recordedFile
    }

    private fun writeWavHeader(file: File) {
        val totalAudioLen = file.length() - 44
        val totalDataLen = totalAudioLen + 36
        val longSampleRate = sampleRate.toLong()
        val channels = 1
        val byteRate = 16 * sampleRate * channels / 8

        val header = ByteArray(44)
        header[0] = 'R'.code.toByte()
        header[1] = 'I'.code.toByte()
        header[2] = 'F'.code.toByte()
        header[3] = 'F'.code.toByte()
        header[4] = (totalDataLen and 0xff).toByte()
        header[5] = ((totalDataLen shr 8) and 0xff).toByte()
        header[6] = ((totalDataLen shr 16) and 0xff).toByte()
        header[7] = ((totalDataLen shr 24) and 0xff).toByte()
        header[8] = 'W'.code.toByte()
        header[9] = 'A'.code.toByte()
        header[10] = 'V'.code.toByte()
        header[11] = 'E'.code.toByte()
        header[12] = 'f'.code.toByte()
        header[13] = 'm'.code.toByte()
        header[14] = 't'.code.toByte()
        header[15] = ' '.code.toByte()
        header[16] = 16
        header[17] = 0
        header[18] = 0
        header[19] = 0
        header[20] = 1
        header[21] = 0
        header[22] = channels.toByte()
        header[23] = 0
        header[24] = (longSampleRate and 0xff).toByte()
        header[25] = ((longSampleRate shr 8) and 0xff).toByte()
        header[26] = ((longSampleRate shr 16) and 0xff).toByte()
        header[27] = ((longSampleRate shr 24) and 0xff).toByte()
        header[28] = (byteRate and 0xff).toByte()
        header[29] = ((byteRate shr 8) and 0xff).toByte()
        header[30] = ((byteRate shr 16) and 0xff).toByte()
        header[31] = ((byteRate shr 24) and 0xff).toByte()
        header[32] = (2 * 16 / 8).toByte()
        header[33] = 0
        header[34] = 16
        header[35] = 0
        header[36] = 'd'.code.toByte()
        header[37] = 'a'.code.toByte()
        header[38] = 't'.code.toByte()
        header[39] = 'a'.code.toByte()
        header[40] = (totalAudioLen and 0xff).toByte()
        header[41] = ((totalAudioLen shr 8) and 0xff).toByte()
        header[42] = ((totalAudioLen shr 16) and 0xff).toByte()
        header[43] = ((totalAudioLen shr 24) and 0xff).toByte()
        val randomAccessFile = RandomAccessFile(file, "rw")
        randomAccessFile.seek(0)
        randomAccessFile.write(header, 0, 44)
        randomAccessFile.close()
    }
}
