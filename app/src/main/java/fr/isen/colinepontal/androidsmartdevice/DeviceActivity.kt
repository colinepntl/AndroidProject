package fr.isen.colinepontal.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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


        binding.led1.setOnClickListener {
            changeLedColor(binding.led1)
            //turnOnLEDs()
        }
        binding.led2.setOnClickListener {
            changeLedColor(binding.led2)
            //turnOnLEDs()
        }
        binding.led3.setOnClickListener {
            changeLedColor(binding.led3)
            //turnOnLEDs()
        }

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



    private fun changeLedColor(imageView: ImageView) {
        if (imageView.colorFilter == null) {
            imageView.setColorFilter(Color.parseColor("#FFD700"))
        } else {
            imageView.colorFilter = null
        }
    }

    /*
    private fun turnOnLEDs() {
    val ledCharacteristicUuid = UUID.fromString("xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx") // UUID de la caractéristique de commande d'allumage des LED
    val ledOnData = byteArrayOf(0x01)

    val ledCharacteristic = bluetoothGatt?.getService(serviceUuid)?.getCharacteristic(ledCharacteristicUuid)
    ledCharacteristic?.value = ledOnData
    bluetoothGatt?.writeCharacteristic(ledCharacteristic)
}
     */

}