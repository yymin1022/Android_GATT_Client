package com.yong.gattclient

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.UUID

class BleCommandService: Service() {
    private val LOG_TAG = "GATT Service"
    private val UUID_CHARACTERISTIC = ""
    private val UUID_SERVICE = ""

    private val binder = LocalBinder()

    private var bleGatt: BluetoothGatt? = null

    inner class LocalBinder: Binder() {
        fun getService(): BleCommandService = this@BleCommandService
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun connectBle(deviceAddr: String): Boolean {
        val bleDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(deviceAddr)
        bleGatt = bleDevice.connectGatt(this, true, gattCallback)
        return true
    }

    fun writeMessage(data: String) {
        bleGatt?.let { gatt ->
            val service = gatt.getService(UUID.fromString(UUID_SERVICE))
            val characteristic = service?.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC))
            if (characteristic != null) {
                val value = data.toByteArray(Charsets.UTF_8)
                characteristic.value = value
                gatt.writeCharacteristic(characteristic)
            }
        }
    }

    private val gattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i(LOG_TAG, "Device Connected")
                gatt.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i(LOG_TAG, "Device Disconnected")
                bleGatt = null
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            Log.i(LOG_TAG, "Discovering Service")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(LOG_TAG, "Discovered Service")
                val service = gatt.getService(UUID.fromString(UUID_SERVICE))
                val characteristic = service?.getCharacteristic(UUID.fromString(UUID_CHARACTERISTIC))
                characteristic?.let {
                    gatt.setCharacteristicNotification(it, true)
                    val descriptor = it.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG))
                    descriptor?.let { desc ->
                        desc.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(desc)
                    }
                }
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val data = characteristic.value?.toString(Charsets.UTF_8)
            Log.i(LOG_TAG, "Message Received: [$data]")
        }
    }

    companion object {
        private const val CLIENT_CHARACTERISTIC_CONFIG = ""
    }
}