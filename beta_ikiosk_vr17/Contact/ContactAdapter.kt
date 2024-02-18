package com.example.beta_ikiosk_vr17.Contact

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.beta_ikiosk_vr17.databinding.ItemContactBinding

class ContactAdapter(val taskList: ArrayList<ContactModel>, val context: Context) :
    RecyclerView.Adapter<ContactAdapter.MyHoler>() {

    class MyHoler(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHoler {

        val binding = ItemContactBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyHoler(binding)
    }

    override fun onBindViewHolder(holder: MyHoler, position: Int) {

        val contact = taskList[position]
        with(holder) {
            binding.tvContactGroup.text = contact.contact_group
        }
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}