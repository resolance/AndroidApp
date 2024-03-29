package com.enecuumwallet.androidapp.presentation.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.enecuumwallet.androidapp.models.Transaction

/**
 * Created by oleg on 31.01.18.
 */
interface TransactionsView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupForTransaction(transaction: Transaction)
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setupNormally()
}