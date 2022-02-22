package com.juarez.inpialbergues.ui.houses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juarez.inpialbergues.data.models.House
import com.juarez.inpialbergues.databinding.ItemHouseBinding
import com.juarez.inpialbergues.utils.loadPhoto

class HousesAdapter(
    private val onClickListener: (house: House) -> Unit,
    private val onEditListener: (house: House) -> Unit,
) :
    ListAdapter<House, HousesAdapter.ViewHolder>(Comparator) {

    class ViewHolder(val binding: ItemHouseBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHouseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val house = getItem(position)
        with(holder) {
            binding.txtItemHouseName.text = house.name
            binding.txtItemHouseAddress.text = house.address
            binding.imgItemHouse.loadPhoto(house.url)
            binding.btnEditHouse.setOnClickListener { onEditListener(house) }
            itemView.setOnClickListener { onClickListener(house) }
        }
    }

    object Comparator : DiffUtil.ItemCallback<House>() {
        override fun areItemsTheSame(oldItem: House, newItem: House): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: House, newItem: House): Boolean {
            return oldItem == newItem
        }
    }
}