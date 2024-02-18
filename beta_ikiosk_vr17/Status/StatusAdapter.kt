package com.example.beta_ikiosk_vr17.Status

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta_ikiosk_vr17.databinding.ItemStatusBinding

class StatusAdapter(val taskList: ArrayList<StatusModel>, val context: Context) :
    RecyclerView.Adapter<StatusAdapter.MyHoler>() {

    class MyHoler(val binding: ItemStatusBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoler {

        val binding = ItemStatusBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHoler(binding)
    }

    override fun onBindViewHolder(holder: MyHoler, position: Int) {

        val contact = taskList[position]
        with(holder) {
            binding.tvStatusCovid.text = contact.status_covid
            binding.tvStatusVaccine.text = contact.status_vaccine
            binding.tvStatusDate.text = contact.status_date
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}