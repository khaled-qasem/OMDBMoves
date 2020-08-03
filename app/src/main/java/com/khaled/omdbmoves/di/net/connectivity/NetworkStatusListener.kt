package com.khaled.omdbmoves.di.net.connectivity

/**
 * Listener for any class to subscribe to network status changes
 */
interface NetworkStatusListener {

    fun onNetworkStatusChanged(isConnected: Boolean)
}
