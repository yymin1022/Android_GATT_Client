package com.yong.gattclient

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var btnStartScan: Button? = null
    private var btnStopScan: Button? = null
    private var recyclerBleResult: RecyclerView? = null

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
        recyclerBleResult = findViewById(R.id.main_recycler_ble_list)

        btnStartScan!!.setOnClickListener(btnListener)
        btnStopScan!!.setOnClickListener(btnListener)
    }

    private val btnListener = View.OnClickListener { view ->
        when(view.id) {
            R.id.main_btn_start_scan -> startBleScan()
            R.id.main_btn_stop_scan -> stopBleScan()
        }
    }

    private fun startBleScan() {
        if(!isBleScanning) {
            isBleScanning = true
        }
    }

    private fun stopBleScan() {
        if(isBleScanning) {
            isBleScanning = false
        }
    }
}