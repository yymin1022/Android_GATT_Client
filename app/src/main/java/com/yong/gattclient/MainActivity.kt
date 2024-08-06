package com.yong.gattclient

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yong.gattclient.adapter.BleScanRecyclerAdapter

class MainActivity : AppCompatActivity(), BleScanRecyclerAdapter.OnItemClickListener {
    private val LOG_TAG = "GATT Client"

    private var btnStartScan: Button? = null
    private var btnStopScan: Button? = null
    private var recyclerBleResult: RecyclerView? = null

    private var bleAdapter = BluetoothAdapter.getDefaultAdapter()
    private var bleDevices = mutableListOf<BluetoothDevice>()
    private var bleRecyclerAdapter: BleScanRecyclerAdapter? = null

    private var isBleScanning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnStartScan = findViewById(R.id.main_btn_start_scan)
        btnStopScan = findViewById(R.id.main_btn_stop_scan)

        btnStartScan!!.setOnClickListener(btnListener)
        btnStopScan!!.setOnClickListener(btnListener)

        bleRecyclerAdapter = BleScanRecyclerAdapter(bleDevices, this)
        recyclerBleResult = findViewById(R.id.main_recycler_ble_list)

        recyclerBleResult!!.adapter = bleRecyclerAdapter
        recyclerBleResult!!.layoutManager = LinearLayoutManager(applicationContext)
    }

    private val btnListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.main_btn_start_scan -> startBleScan()
            R.id.main_btn_stop_scan -> stopBleScan()
        }
    }

    private fun startBleScan() {
        if(!isBleScanning) {
            Log.i(LOG_TAG, "BLE Scan Started")
            isBleScanning = true
            bleAdapter.startLeScan(bleScanCallback)
        }
    }

    private fun stopBleScan() {
        if(isBleScanning) {
            Log.i(LOG_TAG, "BLE Scan Stopped")
            isBleScanning = false
            bleAdapter.stopLeScan(bleScanCallback)
        }
    }

    private val bleScanCallback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
        if(!bleDevices.contains(device) && device.name != null) {
            bleDevices.add(device)
            bleRecyclerAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onItemClick(device: BluetoothDevice) {
        stopBleScan()
    }
}