package com.example.beta_ikiosk_vr17.Temperature

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta_ikiosk_vr17.databinding.ItemTempBinding

class TempAdapter(val taskList: ArrayList<TempModel>, val context: Context) :
    RecyclerView.Adapter<TempAdapter.MyHoler>() {

    class MyHoler(val binding: ItemTempBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoler {

        val binding = ItemTempBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHoler(binding)
    }

    override fun onBindViewHolder(holder: MyHoler, position: Int) {

        val contact = taskList[position]
        with(holder) {
            binding.tvTemp.text = contact.temperature
            binding.tvDate.text = contact.date
            binding.tvTime.text = contact.time
            binding.tvDiagnosis.text = contact.diagnosis
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}