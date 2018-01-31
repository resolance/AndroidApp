package com.enecuum.androidapp.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.enecuum.androidapp.R
import com.enecuum.androidapp.ui.fragment.send_single_tab.SendSingleTabFragment
import com.enecuum.androidapp.ui.fragment.tokens_single.TokensSingleFragment
import kotlinx.android.synthetic.main.fragment_balance.view.*

/**
 * Created by oleg on 30.01.18.
 */
class SendEnqTabsAdapter(fm: FragmentManager?, val context: Context) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val currentMode = if(position == 0)
            SendSingleTabFragment.Companion.SendMode.Enq
        else
            SendSingleTabFragment.Companion.SendMode.EnqPlus
        return SendSingleTabFragment.newInstance(currentMode)
    }

    override fun getPageTitle(position: Int): CharSequence? =
            if(position == 0)
                "ENQ"
            else
                "ENQ+"

    override fun getCount(): Int = 2
}