package com.khaled.omdbmoves.di.net.connectivity

interface
ConnectivityListener {

    val isConnected: Boolean

    fun registerForConnectivityChange(listener: ConnectivityChangeListener): Boolean

    fun unRegisterForConnectivityChange(listener: ConnectivityChangeListener): Boolean

    interface ConnectivityChangeListener {
        fun onConnectivityChanged(isConnected: Boolean)
    }
}