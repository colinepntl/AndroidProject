package fr.isen.colinepontal.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import fr.isen.colinepontal.androidsmartdevice.databinding.ActivityDeviceBinding


@SuppressLint("MissingPermission")
class DeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bluetoothDevice: BluetoothDevice?=intent.getParcelableExtra("device")
        val bluetoothGatt = bluetoothDevice?.connectGatt(this, false, bluetoothGattCallback)
        bluetoothGatt?.connect()
    }


    override fun onStop(){
        super.onStop()
        bluetoothGatt?. close()
    }


    private val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                runOnUiThread{
                    displayContentConnected()
                }
            }
        }
    }

    private fun displayContentConnected(){
        binding.connectText.text=getString(R.string.device_led_text)
        binding.progressBar.isVisible=false
        binding.led1.isVisible= true
        binding.led2.isVisible= true
        binding.led3.isVisible= true
        binding.titleView.isVisible= true
        binding.textView3.isVisible= true
        binding.textView4.isVisible= true
        binding.checkBox.isVisible= true
        binding.counterText.isVisible= true
    }

}