package com.emil.linksy.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.emil.presentation.R
import com.google.android.material.appbar.MaterialToolbar
import io.getstream.photoview.PhotoView

/*
 * Copyright 2024 Chris Banes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@SuppressLint("MissingInflatedId")
class BigPictureDialog:  DialogFragment() {

    private var imageUrl:String = ""

    companion object{
        private const val URL = "URL"
        fun newInstance(url:String): BigPictureDialog {
            val fragment = BigPictureDialog()
            val args = Bundle()
            args.putString(URL, url)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_big_picture, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val  bigPictureImageView = view.findViewById<PhotoView>(R.id.iv_big_picture)
        Glide.with(requireContext())
            .load(imageUrl)
            .into(bigPictureImageView)
        val toolBar = view.findViewById<MaterialToolbar>(R.id.tb_edit_data)
        toolBar.setNavigationOnClickListener { dialog?.dismiss() }
    }

    override fun getTheme() = R.style.FullScreenDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imageUrl = it.getString(URL).toString()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setWindowAnimations(android.R.style.Animation_Dialog)
        }
    }

}