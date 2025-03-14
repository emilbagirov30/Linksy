package com.emil.linksy.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.emil.domain.model.MomentResponse
import com.emil.linksy.presentation.ui.ActionDialog
import com.emil.linksy.presentation.ui.FullScreenMomentDialogFragment
import com.emil.linksy.presentation.viewmodel.MomentViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.hide
import com.emil.linksy.util.show
import com.emil.linksy.util.showMenu
import com.emil.linksy.util.showToast
import com.emil.presentation.R


class MomentsAdapter(private val momentsList: List<MomentResponse>, private val momentViewModel: MomentViewModel,
                     private val tokenManager: TokenManager? =null
): RecyclerView.Adapter< MomentsAdapter.MomentsViewHolder>() {



    companion object {
        const val FULLSCREEN_DIALOG_TAG = "FullScreenMomentDialogFragment"
        private const val VIDEO_FRAME = 0L
    }
    inner class MomentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val context = itemView.context
        private val momentImageView = itemView.findViewById<ImageView>(R.id.iv_moment_frame)
        private val markerImageView = itemView.findViewById<ImageView>(R.id.iv_video_marker)


        fun bind(moment: MomentResponse) {
            val videoUrl = moment.videoUrl
            val imageUrl = moment.imageUrl

            if(imageUrl!=null){
                Glide.with(context)
                    .load(imageUrl)
                    .into(momentImageView)
                markerImageView.hide()
            }
             if (videoUrl!=null){
                 markerImageView.show()
                 Glide.with(context)
                     .load(Uri.parse(videoUrl))
                     .frame(VIDEO_FRAME)
                     .into(momentImageView)
             }
            momentImageView.setOnClickListener {
                FullScreenMomentDialogFragment(momentsList = momentsList, position = bindingAdapterPosition).show(
                    (context as AppCompatActivity).supportFragmentManager, FULLSCREEN_DIALOG_TAG)
            }
            momentImageView.setOnLongClickListener {
                if(tokenManager!=null) {
                    it.showMenu(context,
                        deleteAction = {
                            val dialog = ActionDialog(context)
                            dialog.setTitle(context.getString(R.string.delete_moment_title))
                            dialog.setConfirmText(context.getString(R.string.delete_moment_confirm_text))
                            dialog.setAction {
                                val token = tokenManager.getAccessToken()
                                momentViewModel.deleteMoment(
                                    token= token,
                                    momentId = moment.momentId,
                                    onSuccess = { dialog.dismiss() },
                                    onError = { showToast(context,R.string.error_when_deleting) })
                            }
                        }
                    )
                }
                true

            }
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