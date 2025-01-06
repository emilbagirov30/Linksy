package com.emil.linksy.presentation.ui.navigation.people

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.emil.linksy.adapters.PeoplePagerAdapter
import com.emil.presentation.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class PeopleFragment : Fragment() {
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_people, container, false)
        tabLayout = view.findViewById(R.id.tl_people_navigation)
        val viewPager = view.findViewById<ViewPager2>(R.id.vp_people_pager)
        val pagerAdapter = PeoplePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.search)
                1 -> getString(R.string.subscriptions)
                2 -> getString(R.string.subscribers)
                else -> null
            }
        }.attach()


        return view
    }

}