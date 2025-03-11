package com.emil.linksy.presentation.ui.navigation.channel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.OptionsCreatingAdapter
import com.emil.presentation.R
import com.emil.presentation.databinding.AddPollDialogBinding

class AddPollDialogFragment (private val addChannelPostDialogFragment: AddChannelPostDialogFragment): DialogFragment() {
    private lateinit var binding: AddPollDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AddPollDialogBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvOptions.layoutManager = LinearLayoutManager (requireContext())
        val adapter = OptionsCreatingAdapter()
        binding.rvOptions.adapter = adapter
        binding.tb.setNavigationOnClickListener {
        dismiss()
        }
        binding.btAdd.setOnClickListener {
            adapter.addOption()
        }
        binding.btCreate.setOnClickListener {
            val title = binding.etTitle.text.toString()
            addChannelPostDialogFragment.addPollInPost(title,adapter.getOptionList())
            dismiss()
        }
    }
    override fun getTheme() = R.style.FullScreenDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

}