package com.emil.linksy.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.emil.presentation.R

class OptionsCreatingAdapter: RecyclerView.Adapter<OptionsCreatingAdapter.OptionViewHolder>() {
    private val options = mutableListOf("", "")

    companion object {
        const val MAX_OPTIONS = 10
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_options_creating, parent, false)
        return OptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bind(options[position])
    }

    override fun getItemCount(): Int = options.size

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val editText = itemView.findViewById<EditText>(R.id.et_option)
        private val delete = itemView.findViewById<ImageButton>(R.id.iv_delete)
        fun bind(option: String) {
            editText.setText(option)
            editText.addTextChangedListener(object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(p0: Editable?) { options[bindingAdapterPosition] = p0.toString() }

            })
            delete.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    options.removeAt(position)
                    notifyItemRemoved(position)
            }
        }
    }

}
    fun getOptionList():MutableList<String> = options
    fun addOption() {
        if (options.size < MAX_OPTIONS) {
            options.add("")
            notifyItemInserted(options.size - 1)
        }
    }


}
