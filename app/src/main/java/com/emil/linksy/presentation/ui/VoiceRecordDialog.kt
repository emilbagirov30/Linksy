package com.emil.linksy.presentation.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.pm.PackageManager
import android.widget.ImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.emil.linksy.presentation.custom_view.CustomAudioWave
import com.emil.linksy.presentation.ui.navigation.profile.AddPostDialogFragment
import com.emil.linksy.util.AudioRecorderManager
import com.emil.presentation.R

class VoiceRecordDialog(private val dialog: Fragment) : Dialog(dialog.requireContext(), R.style.TransparentDialog) {

    private val RECORD_AUDIO_PERMISSION_CODE = 100
    private var audioRecorderManager: AudioRecorderManager
    private var audioWaveView: CustomAudioWave
    private var cancelButton: ImageButton
    private var saveButton: ImageButton

    init {
        setContentView(R.layout.record_voice_dialog)
        setCancelable(false)
        show()
        audioRecorderManager = AudioRecorderManager(context)
        audioWaveView = findViewById(R.id.caw_voice)
        cancelButton = findViewById(R.id.ib_cancel)
        saveButton = findViewById(R.id.ib_save)

        saveButton.setOnClickListener {
            audioRecorderManager.stopRecording()
            val recordedFile = audioRecorderManager.getRecordedFile()
            (dialog as? AddPostDialogFragment)?.handleVoiceRecordingSaved(recordedFile)
            dismiss()
        }

        cancelButton.setOnClickListener {
            audioRecorderManager.stopRecording()
            dismiss()
        }

        checkAudioPermissionAndStartRecording()
    }

    private fun checkAudioPermissionAndStartRecording() {
        val permissionsGranted = hasAllPermissions()
        if (permissionsGranted) {
            startRecording()
        } else {
            ActivityCompat.requestPermissions(
                dialog.requireActivity(),
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO
                ),
                RECORD_AUDIO_PERMISSION_CODE
            )
            dismiss()
        }

    }

    private fun hasAllPermissions(): Boolean {
        val readPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val writePermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val recordAudioPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        return readPermission && writePermission && recordAudioPermission
    }


    private fun startRecording() {
        audioRecorderManager.startRecording { amplitude ->
            (dialog.requireContext() as? Activity)?.runOnUiThread {
                audioWaveView.addAmplitude(amplitude)
            }
        }
    }


}
