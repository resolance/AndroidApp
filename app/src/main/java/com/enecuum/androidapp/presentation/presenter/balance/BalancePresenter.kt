package com.enecuum.androidapp.presentation.presenter.balance

import android.content.Context
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.SendReceiveMode
import com.enecuum.androidapp.models.Transaction
import com.enecuum.androidapp.models.TransactionType
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import com.enecuum.androidapp.ui.activity.testActivity.CustomBootNodeFragment
import com.enecuum.androidapp.ui.activity.testActivity.PoaService

@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {

    var poaService: PoaService? = null;

    fun onCreate() {
        //TODO: fill with real values
        viewState.displayCurrencyRates(7.999999, 7.999999)
        viewState.displayBalances(30.0, 30.0)
        viewState.displayPoints(1.0)
        viewState.displayKarma(1.0)
        val transactionsList = listOf(
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.EnqPlus),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Send, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq),
                Transaction(TransactionType.Receive, 1517307367, 8.0, "5Kb8kLL6TsZZY36hWXMssSzNyd…", SendReceiveMode.Enq)
        )
        viewState.displayTransactionsHistory(transactionsList)
    }

    fun onTokensClick() {
        EnecuumApplication.navigateToFragment(FragmentType.Tokens, TabType.Home)
    }

    fun onMiningToggle() {
        val sharedPreferences = EnecuumApplication.applicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

        if (poaService == null) {
            val custom = sharedPreferences.getBoolean(CustomBootNodeFragment.customBN, false)

            val customPath = sharedPreferences.getString(CustomBootNodeFragment.customBNIP, CustomBootNodeFragment.BN_PATH_DEFAULT);
            val customPort = sharedPreferences.getString(CustomBootNodeFragment.customBNPORT, CustomBootNodeFragment.BN_PORT_DEFAULT);
            val path = if (custom) customPath else CustomBootNodeFragment.BN_PATH_DEFAULT
            val port = if (custom) customPort else CustomBootNodeFragment.BN_PORT_DEFAULT
            poaService = PoaService(EnecuumApplication.applicationContext(),
                    path,
                    port,
                    onTeamSize = object : PoaService.onTeamListener {
                        override fun onTeamSize(size: Int) {
                            viewState.displayTeamSize(size);
                        }
                    },
                    onMicroblockCountListerer = object : PoaService.onMicroblockCountListener {
                        override fun onMicroblockCount(count: Int) {
                            viewState.displayMicroblocks(count);
                        }
                    }
            )

            poaService?.connect()
            viewState.showProgress()
        } else {
            poaService = null
            viewState.hideProgress()


        }

        viewState.changeButtonState(poaService == null)


    }
}
