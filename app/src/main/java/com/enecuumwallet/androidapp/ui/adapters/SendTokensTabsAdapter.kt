package com.enecuumwallet.androidapp.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.enecuumwallet.androidapp.R
import com.enecuumwallet.androidapp.ui.fragment.tokens_single.TokensSingleFragment
import kotlinx.android.synthetic.main.fragment_balance.view.*

/**
 * Created by oleg on 30.01.18.
 */
class SendTokensTabsAdapter(fm: FragmentManager?, val context: Context) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        val currentMode = if(position == 0)
            TokensSingleFragment.Companion.Mode.TokenMode
        else
            TokensSingleFragment.Companion.Mode.JettonMode
        return TokensSingleFragment.newInstance(currentMode)
    }

    override fun getPageTitle(position: Int): CharSequence? =
            if(position == 0)
                context.getString(R.string.tokens)
            else
                context.getString(R.string.jettons)

    override fun getCount(): Int = 2
}