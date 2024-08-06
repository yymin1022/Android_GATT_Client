package com.yong.gattclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ControlActivity : AppCompatActivity() {
    private var bleCommandService: BleCommandService? = null
    private var isServiceBinded = false

    private var bleDeviceAddress: String? = null

    private val bleCommandServiceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val serviceBinder = service as BleCommandService.LocalBinder
            bleCommandService = serviceBinder.getService()
            isServiceBinded = true
            bleDeviceAddress?.let {
                bleCommandService?.connectBle(it)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            bleCommandService = null
            isServiceBinded = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_control)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bleDeviceAddress = intent.getStringExtra("BLE_DEVICE_ADDRESS")
        val serviceIntent = Intent(applicationContext, BleCommandService::class.java)
        startService(serviceIntent)
        bindService(serviceIntent, bleCommandServiceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()

        if(isServiceBinded) {
            unbindService(bleCommandServiceConnection)
            isServiceBinded = false
        }
    }
}