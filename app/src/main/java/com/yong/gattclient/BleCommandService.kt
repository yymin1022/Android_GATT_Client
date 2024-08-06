package com.yong.gattclient

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class BleCommandService: Service() {
    private val binder = LocalBinder()

    inner class LocalBinder: Binder() {
        fun getService(): BleCommandService = this@BleCommandService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
}