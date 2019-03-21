package com.khaled.omdbmoves.di.net.connectivity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import com.khaled.omdbmoves.di.net.connectivity.ConnectivityListener.ConnectivityChangeListener
import javax.inject.Inject

class ConnectivityListenerImpl @Inject constructor(
    private val context: Context,
    private val applicationLifeCycleListener: ApplicationLifeCycleListener
) : BroadcastReceiver(), ConnectivityListener {

    private var _isConnected: Boolean = false
    private val statusListeners: MutableSet<ConnectivityChangeListener> = HashSet()

    override val isConnected: Boolean
        get() {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }

    init {
        register()
        applicationLifeCycleListener.registerForLifeCycleChange(object :
            ApplicationLifeCycleListener.LifeCycleChangeListener {
            override fun onLifecycleChanged(status: ApplicationLifeCycleListener.LifeCycleStatus) {
                when (status) {
                    ApplicationLifeCycleListener.LifeCycleStatus.IN_FOREGROUND, ApplicationLifeCycleListener.LifeCycleStatus.KILLED -> register()
                    ApplicationLifeCycleListener.LifeCycleStatus.IN_BACKGROUND -> unRegister()
                }
            }

            override fun onActivityResumed(activity: Activity) {
                // We need to override this method to keep SnackBar shown
                // In case of configuration changes
                if (!_isConnected) {
                    showConnectionStatusSnackBar()
                }
            }
        })
    }

    override fun onReceive(context: Context, intent: Intent) {
        val wasConnected = _isConnected
        _isConnected = isConnected

        if (wasConnected != _isConnected) {
            notifyNetworkStatusListeners(_isConnected)
        }

        if (wasConnected != _isConnected || !_isConnected) {
            showConnectionStatusSnackBar()
        }
    }

    override fun registerForConnectivityChange(listener: ConnectivityListener.ConnectivityChangeListener): Boolean =
        statusListeners.add(listener)


    override fun unRegisterForConnectivityChange(listener: ConnectivityListener.ConnectivityChangeListener): Boolean =
        statusListeners.remove(listener)


    private fun notifyNetworkStatusListeners(isConnected: Boolean) {
        for (listener in statusListeners) {
            listener.onConnectivityChanged(isConnected)
        }
    }

    private fun register() {
        _isConnected = isConnected
        context.registerReceiver(this, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun unRegister() {
        context.unregisterReceiver(this)
    }

    private fun showConnectionStatusSnackBar() {
        val activity = applicationLifeCycleListener.getCurrentActivity()
        if (activity == null || !applicationLifeCycleListener.isAppInForeground()) {
            return
        }
        if (isConnected) {
            Toast.makeText(activity, R.string.connected, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(activity, R.string.disconnected, Toast.LENGTH_LONG).show()
        }
    }
}