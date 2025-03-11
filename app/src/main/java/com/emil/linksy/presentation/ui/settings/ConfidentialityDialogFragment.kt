package com.emil.linksy.presentation.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import com.emil.domain.model.MessageMode
import com.emil.linksy.presentation.viewmodel.UserViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.show
import com.emil.linksy.util.showToast
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfidentialityDialogFragment: DialogFragment() {
    override fun getTheme() = R.style.FullScreenDialog
    private val userViewModel:UserViewModel by viewModel<UserViewModel>()
    private val tokenManager:TokenManager by inject<TokenManager>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confidentiality_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolBar = view.findViewById<MaterialToolbar>(R.id.tb)
        val applyButton = view.findViewById<MaterialButton>(R.id.bt_apply)
        val messageModeLayout = view.findViewById<LinearLayout>(R.id.ll_message_mode)
        val spinner = view.findViewById<Spinner>(R.id.message_mode_spinner)
        var mode:MessageMode = MessageMode.ALL
        toolBar.setNavigationOnClickListener {
            dialog?.dismiss()
        }


        ArrayAdapter.createFromResource(requireContext(), R.array.message_mode,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
               mode = when (position) {
                    0 -> MessageMode.ALL
                    1 -> MessageMode.SUBSCRIPTIONS_ONLY
                    else -> MessageMode.NOBODY
               }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        userViewModel.messageMode.observe(requireActivity()){messageMode ->
            messageModeLayout.show()
            applyButton.show()
            println (messageMode.name)
            when (messageMode){
                MessageMode.NOBODY -> spinner.setSelection(2)
                MessageMode.SUBSCRIPTIONS_ONLY -> spinner.setSelection(1)
                else -> spinner.setSelection(0)
            }
        }
          userViewModel.getMessageMode(tokenManager.getAccessToken())

        applyButton.setOnClickListener {
            userViewModel.setMessageMode(tokenManager.getAccessToken(),mode, onSuccess = {
                context?.let { c -> showToast(c, R.string.the_changes_have_been_applied) }
            }, onError = { context?.let { c -> showToast(c, R.string.failed_connection) }})
        }
    }
}