package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emil.domain.model.OptionResponse
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.show
import com.emil.presentation.R

class OptionsAdapter(var isVoted:Boolean, val options:List<OptionResponse>, val channelViewModel: ChannelViewModel, val tokenManager: TokenManager): RecyclerView.Adapter<OptionsAdapter.OptionViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_options, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mainLinearLayout = itemView.findViewById<LinearLayout>(R.id.ll_main)
        private val titleTextView = itemView.findViewById<TextView>(R.id.tv_option)
        private val optionProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_option)
        private val optionCount = itemView.findViewById<TextView>(R.id.tv_option_count)
        @SuppressLint("SetTextI18n")
        fun bind(option:OptionResponse) {
                  titleTextView.text = option.optionText
            if(isVoted) {
                optionProgressBar.max = options.sumOf { it.selectedCount }.toInt()
                optionCount.show()
                optionCount.text = option.selectedCount.toString()
            }
            mainLinearLayout.setOnClickListener {
                channelViewModel.vote(tokenManager.getAccessToken(),option.optionId, onSuccess = {isVoted=true
                    options[bindingAdapterPosition].selectedCount++
                    notifyItemChanged(bindingAdapterPosition)
                })
            }
            }
        }

    }





