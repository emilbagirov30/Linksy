package com.emil.linksy.presentation.ui.navigation.channel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.emil.linksy.adapters.ChannelsAdapter
import com.emil.linksy.presentation.ui.CameraXActivity
import com.emil.linksy.presentation.ui.navigation.chat.CreateGroupActivity
import com.emil.linksy.presentation.viewmodel.ChannelViewModel
import com.emil.linksy.util.TokenManager
import com.emil.linksy.util.anim
import com.emil.linksy.util.show
import com.emil.linksy.util.showHint
import com.emil.presentation.R
import com.emil.presentation.databinding.ActivityCreateChannelBinding
import com.emil.presentation.databinding.FragmentChannelBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChannelFragment : Fragment() {
    private lateinit var binding: FragmentChannelBinding
    private val channelViewModel: ChannelViewModel by viewModel<ChannelViewModel>()
    private val tokenManager: TokenManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChannelBinding.inflate(layoutInflater)
        val view = binding.root
        binding.ibHint.setOnClickListener {
            it.anim()
            it.showHint(requireContext(),R.string.channel_search_hint)
        }
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
        val sharedPref: SharedPreferences = requireContext().getSharedPreferences("AppData", Context.MODE_PRIVATE)
        val userId = sharedPref.getLong("ID",-1)


        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val currentText = it.toString()
                    if (currentText.startsWith("@")) {
                       channelViewModel.findChannelByLink(currentText, onSuccess = {
                           binding.tvSearchTitle.show()
                           binding.tvSearchTitle.text = getString(R.string.search_results)})
                    }else{
                        channelViewModel.findChannelByName(currentText, onSuccess = {
                            binding.tvSearchTitle.show()
                            binding.tvSearchTitle.text = getString(R.string.search_results)})
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etSearch.addTextChangedListener(textWatcher)


        binding.rvChannels.layoutManager = LinearLayoutManager(context)


        channelViewModel.channelList.observe(requireActivity()){channelList ->
            binding.rvChannels.adapter = context?.let { ChannelsAdapter(channelList, it,userId) }
        }


       getChannels()
        binding.ibScan.setOnClickListener {
            it.anim()
            val switchingToCameraXActivity= Intent(requireContext(), CameraXActivity::class.java)
            switchingToCameraXActivity.putExtra("TARGET","GROUP")
            startActivity(switchingToCameraXActivity)
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            getChannels()
        }
        return view


    }

    override fun onResume() {
        super.onResume()
        getChannels()
    }
private fun getChannels(){
    channelViewModel.getChannels(token = tokenManager.getAccessToken(), onSuccess = {
        binding.tvSearchTitle.show()
        binding.tvSearchTitle.text = getString(R.string.your_subscriptions)
        binding.swipeRefreshLayout.isRefreshing = false
    })
}

}