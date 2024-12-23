package com.emil.linksy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.emil.domain.model.MomentResponse
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.TokenManager
import com.emil.presentation.R


class MomentsAdapter(private val momentsList: List<MomentResponse>, private val momentsViewModel: MomentViewModel,
                     private val context: Context, private val tokenManager: TokenManager
): RecyclerView.Adapter< MomentsAdapter.MomentsViewHolder>() {

    inner class MomentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val momentImageView = itemView.findViewById<ImageView>(R.id.iv_moment_frame)
        private val markerImageView = itemView.findViewById<ImageView>(R.id.iv_video_marker)


        fun bind(moment: MomentResponse) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MomentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_moments, parent, false)
        return MomentsViewHolder(view)
    }

    override fun getItemCount(): Int = momentsList.size


    override fun onBindViewHolder(holder: MomentsViewHolder, position: Int) {
        holder.bind (momentsList[position])
    }

}