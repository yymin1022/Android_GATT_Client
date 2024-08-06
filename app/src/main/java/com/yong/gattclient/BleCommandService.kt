package com.yong.gattclient

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BleCommandService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}