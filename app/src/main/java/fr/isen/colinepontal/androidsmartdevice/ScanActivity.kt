package fr.isen.colinepontal.androidsmartdevice

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.colinepontal.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private var mScanning = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bluetoothAdapter= BluetoothAdapter.getDefaultAdapter()
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (bluetoothAdapter?.isEnabled==true){
            val toast1=Toast.makeText(applicationContext,"L'appareil est connecté", Toast.LENGTH_LONG)
            toast1.show()
            binding.progressBar2.isVisible = false
            binding.itemList.isVisible = false
            binding.itemList.layoutManager = LinearLayoutManager(this)
            binding.itemList.adapter = ScanAdapter(arrayListOf("Device 1", "Device 2", "Device 3"))


            binding.scanTitle2.setOnClickListener {
                togglePlayPauseAction()
            }
            binding.playPauseAction.setOnClickListener {
                togglePlayPauseAction()
            }
            scanDeviceWithPermissions()
        }else{
            handleBLENotAvailable()
        }


        }
    private val REQUEST_ENABLE_BLUETOOTH = 1
    private val REQUEST_LOCATION_PERMISSION = 2

    private fun togglePlayPauseAction() {
        mScanning = !mScanning
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

    private fun scanDeviceWithPermissions(){
        if (allPermissionsGranted()){
            scanBLEDevices()
        }else{
            requestPermissions()
            //request permissions
        }
    }

    private fun scanBLEDevices(){
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            handleBLENotAvailable()
            return
        }
        togglePlayPauseAction()
    }

    private fun allPermissionsGranted():Boolean{
        val allPermissions=getAllPermissions()
        return allPermissions.all{permission ->
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED

        }
    }

    private fun getAllPermissions(): Array<String>{
        return arrayOf(
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            getAllPermissions(),
            REQUEST_LOCATION_PERMISSION
        )
    }


    private fun handleBLENotAvailable(){
        binding.scanTitle2.text=getString(R.string.ble_scan_missing)
        val toast_err = Toast.makeText(
            applicationContext,
            "Votre appareil n'est pas connecté",
            Toast.LENGTH_LONG
        )
        toast_err.show()
        binding.progressBar2.isVisible = false
        binding.itemList.isVisible = false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, )
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (allPermissionsGranted()) {
                    scanBLEDevices()
                } else {
                    // Permission refusée
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BLUETOOTH -> {
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth activé
                    scanBLEDevices()
                } else {
                    // Activation Bluetooth refusée
                }
            }
        }
    }


}




