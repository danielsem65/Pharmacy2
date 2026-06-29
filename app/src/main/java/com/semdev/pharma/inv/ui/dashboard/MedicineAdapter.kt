package com.semdev.pharma.inv.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.semdev.pharma.inv.data.local.MedicineEntity
import com.semdev.pharma.inv.databinding.ItemMedicineBinding

class MedicineAdapter : ListAdapter<MedicineEntity, MedicineAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMedicineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemMedicineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(medicine: MedicineEntity) {
            binding.textName.text = medicine.name
            binding.textPrice.text = medicine.salesPrice
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<MedicineEntity>() {
        override fun areItemsTheSame(oldItem: MedicineEntity, newItem: MedicineEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MedicineEntity, newItem: MedicineEntity): Boolean {
            return oldItem == newItem
        }
    }
}
