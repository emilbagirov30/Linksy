package com.emil.linksy.presentation.ui.navigation.channel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.emil.linksy.presentation.ui.navigation.chat.CreateGroupActivity
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityCreateChannelBinding
import com.emil.presentation.databinding.FragmentChannelBinding

class ChannelFragment : Fragment() {
    private lateinit var binding: FragmentChannelBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(layoutInflater)
        val view = binding.root
        binding.tb.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.channel_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.action_create_channel -> {
                        startActivity(Intent(context, CreateChannelActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }, requireActivity(), Lifecycle.State.CREATED)
        return view


    }


}