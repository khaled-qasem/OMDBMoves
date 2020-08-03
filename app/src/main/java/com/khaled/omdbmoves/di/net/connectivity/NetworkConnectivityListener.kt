package com.khaled.omdbmoves.di.net.connectivity

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.khaled.omdbmoves.R
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener
import com.khaled.omdbmoves.di.lifecycle.ApplicationLifeCycleListener.LifeCycleStatus
import com.khaled.omdbmoves.utils.Event
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityListener @Inject constructor(
    private val context: Context,
    private val applicationLifeCycleListener: ApplicationLifeCycleListener
) : ConnectivityManager.NetworkCallback() {

    private val networkStatusListeners = HashSet<NetworkStatusListener>()

    // Initial value is true, it will be triggered to false if there's no network in checkConnectivity fun.
    private val _isConnected = MutableLiveData(Event(true))
    val isConnected: LiveData<Event<Boolean>> get() = _isConnected

    init {
        applicationLifeCycleListener.registerForLifeCycleChange(object :
            ApplicationLifeCycleListener.LifeCycleChangeListener {

            override fun onLifecycleChanged(status: LifeCycleStatus) {
                when (status) {
                    LifeCycleStatus.IN_FOREGROUND, LifeCycleStatus.KILLED -> register()
                    LifeCycleStatus.IN_BACKGROUND -> unregister()
                }
            }

            override fun onActivityCreated(activity: Activity) {
                (activity as? LifecycleOwner)?.let {
                    isConnected.observe(it, Observer { event ->
                        event.getContentIfNotHandled()?.let { isConnected ->
                            showConnectionStatusSnackBar(isConnected)
                            networkStatusListeners.forEach { listener ->
                                listener.onNetworkStatusChanged(isConnected)
                            }
                        }
                    })
                }
            }
        })

        checkConnectivity()
    }

    /**
     * Registers this network callback to listen for changes to WiFi/Mobile connection statues.
     * Note that it first tries to unregister any existing callbacks to ensure not duplicates.
     */
    private fun register() {
        unregister()

        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
            .registerNetworkCallback(networkRequest, this)
    }

    /**
     * Attempts to unregister this network callback
     */
    private fun unregister() {
        try {
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .unregisterNetworkCallback(this)
        } catch (e: Exception) {
            Timber.d("Connectivity listener was not registered or already unregistered.")
        }
    }

    /**
     * Used to force check of network connectivity outside the use of callbacks.
     *
     * When internet connection is not available, send event to show snackbar and notify listeners
     * When internet connection is available, notify listeners only
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun checkConnectivity() {
        if (!isAnyConnectionAvailable(context)) {
            _isConnected.postValue(Event(false))
        } else if (_isConnected.value?.peekContent() == false) {
            _isConnected.postValue(Event(true))
        }
    }

    /**
     * Callback invoked when status of any network specified in network request during [register]
     * is changed to connected or available status
     *
     * @param network the network whose status has changed to available/connected
     */
    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        if (_isConnected.value?.peekContent() == false && isAnyConnectionAvailable(context)) {
            _isConnected.postValue(Event(true))
        }
    }

    /**
     * Callback invoked when status of any network specified in network request during [register]
     * is changed to disconnected or unavailable status
     *
     * @param network the network whose status has changed to unavailable/disconnected
     */
    override fun onLost(network: Network) {
        if (!isAnyConnectionAvailable(context)) {
            _isConnected.postValue(Event(false))
        }
        super.onLost(network)
    }

    /**
     * Checks whether WiFi or Mobile network is connected
     *
     * @return [Boolean] true if at least one network is connected
     */
    private fun isAnyConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.activeNetwork?.let { network ->
                val networkCapabilities =
                    connectivityManager.getNetworkCapabilities(network)
                networkCapabilities?.let {
                    if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    ) return true
                }
            }
            false
        } else {
            // Return true is better that returning false as UX
            connectivityManager?.activeNetworkInfo?.isConnected ?: true
        }
    }

    /**
     * Shows network status snackbar based on current connection status
     *
     * @param isConnected whether any network connection is available (determines snackbar props)
     */
    private fun showConnectionStatusSnackBar(isConnected: Boolean) {
        applicationLifeCycleListener.getCurrentActivity()?.let {
            if (applicationLifeCycleListener.isAppInForeground()) {
                showConnectionStatus(isConnected)
            }
        }
    }

    //TODO(Implement it with Snackbar)
    private fun showConnectionStatus(isConnected: Boolean) {
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

    fun registerForNetworkStatusChanges(
        networkStatusListener: NetworkStatusListener,
        forceCheckConnection: Boolean = false
    ) {
        networkStatusListeners.add(networkStatusListener)
        if (forceCheckConnection) {
            checkConnectivity()
        }
    }

    fun unregisterForNetworkStatusChanges(networkStatusListener: NetworkStatusListener) {
        networkStatusListeners.remove(networkStatusListener)
    }
}
