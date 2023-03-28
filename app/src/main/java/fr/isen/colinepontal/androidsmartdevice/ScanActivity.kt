package fr.isen.colinepontal.androidsmartdevice

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.colinepontal.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {



    private val bluetoothAdapter: BluetoothAdapter? by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.all { it.value }) {
                scanBLEDevices()
            }
        }
    private lateinit var binding: ActivityScanBinding
    private var mScanning = false

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var scanAdapter: ScanAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (bluetoothAdapter?.isEnabled == true) {
            val toast1 =
                Toast.makeText(applicationContext, "L'appareil est connecté", Toast.LENGTH_LONG)
            toast1.show()

            scanDeviceWithPermissions()
        } else {
            handleBLENotAvailable()
        }

    }

    @SuppressLint("MissingPermission")
    override fun onStop(){
        super.onStop()
        if(bluetoothAdapter?.isEnabled==true && allPermissionsGranted()){
            mScanning=false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan((leScanCallback))
        }
    }

    private fun initToggleActions(){
        binding.scanTitle2.setOnClickListener {
            scanBLEDevices()
        }
        binding.playPauseAction.setOnClickListener {
            scanBLEDevices()
        }
        binding.itemList.layoutManager = LinearLayoutManager(this)
        scanAdapter = ScanAdapter(arrayListOf()){
            val intent= Intent(this, DeviceActivity::class.java)
            intent.putExtra("device",it)
            startActivity(intent)
        }
        binding.itemList.adapter = scanAdapter
    }

    private fun scanDeviceWithPermissions() {
        if (allPermissionsGranted()) {
            initToggleActions()
            scanBLEDevices()
        } else {
            requestPermissionLauncher.launch(getAllPermissions())
        }
    }


    @SuppressLint("MissingPermission")
    private fun scanBLEDevices() {
        if (!mScanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mScanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
                togglePlayPauseAction()
            }, SCAN_PERIOD)
            mScanning = true
            bluetoothAdapter?.bluetoothLeScanner?.startScan(leScanCallback)
        } else {
            mScanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
        togglePlayPauseAction()
    }

    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            Log.d("Scan","result: $result")
            scanAdapter.addDevice(result.device)
            scanAdapter.notifyDataSetChanged()
        }
    }





    private fun allPermissionsGranted(): Boolean {
        val allPermissions = getAllPermissions()
        return allPermissions.all {
            //permission ->
            ContextCompat.checkSelfPermission(
                this,
                it
            ) == PackageManager.PERMISSION_GRANTED

        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    private fun handleBLENotAvailable() {
        binding.scanTitle2.text = getString(R.string.ble_scan_missing)
        val toast_err = Toast.makeText(
            applicationContext,
            "Votre appareil n'est pas connecté",
            Toast.LENGTH_LONG
        )
        toast_err.show()
        binding.progressBar2.isVisible = false
        binding.itemList.isVisible = false
    }

    private fun togglePlayPauseAction() {
        if (mScanning) {
            binding.scanTitle2.text = getString(R.string.ble_scan_title_pause)
            binding.playPauseAction.setImageResource(R.drawable.oojs_ui_icon_pause_progressive_svg)
            binding.progressBar2.isVisible = true
            binding.itemList.isVisible = true
        } else {
            binding.scanTitle2.text = getString(R.string.ble_scan_title_play)
            binding.playPauseAction.setImageResource(R.drawable._024px_oojs_ui_icon_play_ltr_progressive_svg)
            binding.progressBar2.isVisible = false
            binding.itemList.isVisible = false
        }
    }

    companion object {
        private val SCAN_PERIOD: Long = 10000
    }


}