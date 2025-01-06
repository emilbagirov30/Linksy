package com.emil.linksy.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.emil.linksy.presentation.ui.SearchPeopleFragment
import com.emil.linksy.presentation.ui.SubscribersFragment
import com.emil.linksy.presentation.ui.SubscriptionsFragment

class PeoplePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SearchPeopleFragment()
            1 -> SubscriptionsFragment ()
            2 -> SubscribersFragment ()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}



