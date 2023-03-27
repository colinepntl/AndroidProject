package fr.isen.colinepontal.androidsmartdevice


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.colinepontal.androidsmartdevice.databinding.ActivityRecyclerViewAdaptaterBinding


class ScanAdapter(var devices: ArrayList<String>) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding= ActivityRecyclerViewAdaptaterBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)
    }

    override fun getItemCount(): Int = devices.size


    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        //holder.DeviceName.text = devices[position]

    }

    class ScanViewHolder(binding: ActivityRecyclerViewAdaptaterBinding) : RecyclerView.ViewHolder(binding.root) {

        //val DeviceName: TextView =itemView.findViewById(R.id.deviceName)
        //val macAddress: TextView =itemView.findViewById(R.id.macAddress)
        //val card: ConstraintLayout =itemView.findViewById(R.id.cardDevice)
    }


}
