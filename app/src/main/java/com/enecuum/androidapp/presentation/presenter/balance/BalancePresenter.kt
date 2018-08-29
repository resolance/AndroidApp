package com.enecuum.androidapp.presentation.presenter.balance

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.enecuum.androidapp.application.EnecuumApplication
import com.enecuum.androidapp.models.inherited.models.MicroblockResponse
import com.enecuum.androidapp.navigation.FragmentType
import com.enecuum.androidapp.navigation.TabType
import com.enecuum.androidapp.presentation.view.balance.BalanceView
import com.enecuum.androidapp.ui.activity.testActivity.CustomBootNodeFragment
import com.enecuum.androidapp.ui.activity.testActivity.PoaClient
import com.jraska.console.Console
import java.util.*
import android.os.CountDownTimer


@InjectViewState
class BalancePresenter : MvpPresenter<BalanceView>() {

    private lateinit var poaClient: PoaClient

    val sharedPreferences = EnecuumApplication.applicationContext().getSharedPreferences("pref", Context.MODE_PRIVATE);

    val microblockList = hashMapOf<String, MicroblockResponse>()

    val custom = sharedPreferences.getBoolean(CustomBootNodeFragment.customBN, false)
    val customPath = sharedPreferences.getString(CustomBootNodeFragment.customBNIP, CustomBootNodeFragment.BN_PATH_DEFAULT);
    val customPort = sharedPreferences.getString(CustomBootNodeFragment.customBNPORT, CustomBootNodeFragment.BN_PORT_DEFAULT);
    val path = if (custom) customPath else CustomBootNodeFragment.BN_PATH_DEFAULT
    val port = if (custom) customPort else CustomBootNodeFragment.BN_PORT_DEFAULT

    fun onCreate() {
        poaClient = PoaClient(EnecuumApplication.applicationContext(),
                path,
                port,
                onTeamSizeListener = object : PoaClient.onTeamListener {
                    override fun onTeamSize(size: Int) {
                        Handler(Looper.getMainLooper()).post {
                            viewState.displayTeamSize(size);
                        }
                    }
                },
                onMicroblockCountListerer = object : PoaClient.onMicroblockCountListener {
                    override fun onMicroblockCountAndLast(count: Int, microblockResponse: MicroblockResponse, microblockSignature: String) {
                        microblockList.put(microblockSignature, microblockResponse);
                        viewState.displayTransactionsHistory(microblockList.keys.toList())
                        viewState.displayMicroblocks(10 * count);
                    }
                },
                onConnectedListner = object : PoaClient.onConnectedListener {
                    override fun onConnectionError() {
                        viewState.showConnectionError()
                    }

                    override fun onStartConnecting() {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(true)
                            viewState.showLoading()
                        }
                    }

                    override fun onDisconnected() {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(true)
                            viewState.hideProgress()
                            viewState.hideLoading()

                            //prevent show disconnected
                            if (!disconnectedByUser) {
                                object : CountDownTimer(5000, 1000) {
                                    override fun onTick(millisUntilFinished: Long) {
                                        viewState.updateProgressMessage("Connecting...  " + millisUntilFinished / 1000 + " sec")

                                    }

                                    override fun onFinish() {
                                        viewState.updateProgressMessage("Connecting...")
                                        connect()
                                    }

                                }.start()
                                viewState.showProgress()
                                disconnectedByUser = false
                            }
                        }
                    }

                    override fun onConnected(ip: String, port: String) {
                        Handler(Looper.getMainLooper()).post {
                            viewState.changeButtonState(false)
                            viewState.showProgress()
                            viewState.hideLoading()
                        }
                    }
                },
                balanceListener = object : PoaClient.BalanceListener {
                    override fun onBalance(amount: Int) {
                        viewState.setBalance(amount)
                    }
                }
        )
    }

    internal inner class CleanTask : TimerTask() {
        override fun run() {
            Console.clear()
        }
    };
    fun onTokensClick() {
        EnecuumApplication.navigateToFragment(FragmentType.Tokens, TabType.Home)
    }


    private var disconnectedByUser: Boolean = false

    fun onMiningToggle() {
        if (::poaClient.isInitialized) {
            if (!poaClient.isConnected()) {
                connect()
            } else {
                disconnectedByUser = true
                disconnect()
            }
        }
    }

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }

    fun connect() {
        if (::poaClient.isInitialized) {
            if (!poaClient.isConnected()) {
                poaClient.connect()
            }
        }
    }

    fun disconnect() {
        if (::poaClient.isInitialized) {
            if (poaClient.isConnected()) {
                poaClient.disconnect()
            }
        }
    }
}
