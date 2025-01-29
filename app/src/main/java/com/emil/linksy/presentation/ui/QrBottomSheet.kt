package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.emil.linksy.util.generateQRCode
import com.emil.presentation.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.properties.Delegates

class QrBottomSheet: BottomSheetDialogFragment() {
    private var id by Delegates.notNull<Long>()

    companion object {
        private const val ARG_ID = "id"
        fun newInstance(id:Long): QrBottomSheet {
            val fragment = QrBottomSheet()
            val args = Bundle()
            args.putLong(ARG_ID, id)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getLong(ARG_ID) ?: -1
    }

    override fun getTheme() = R.style.BottomSheetDialogTheme

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.qr_bottom_sheet, container, false)
        val qrImageView = view.findViewById<ImageView>(R.id.iv_qr)
        dialog?.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            val bottomSheet =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val layoutParams = it.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                it.layoutParams = layoutParams
            }
        }
          generateQRCode(id.toString(),qrImageView)
        return view
    }
}