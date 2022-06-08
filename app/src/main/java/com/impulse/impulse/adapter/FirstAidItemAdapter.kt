package com.impulse.impulse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.impulse.impulse.databinding.ItemAidHeadViewBinding
import com.impulse.impulse.databinding.ItemAidViewBinding
import com.impulse.impulse.model.FirstAidItem

class FirstAidItemAdapter(private var items: List<FirstAidItem>) : BaseAdapter() {
    private val ITEM_HEADER = 0
    private val ITEM_VIEW = 1

    override fun getItemViewType(position: Int): Int {
        if (isHeader(position)) return ITEM_HEADER
        return ITEM_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == ITEM_HEADER) {
            return MyMyHolderHeader(ItemAidHeadViewBinding.inflate(LayoutInflater.from(parent.context)))
        }
        return MyViewHolder(ItemAidViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is MyViewHolder) {
            holder.onBind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class MyViewHolder(var binding: ItemAidViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: FirstAidItem) {
            binding.apply {
                ivIcon.setImageResource(item.icon!!)
                tvName.text = item.name
            }
        }
    }

    class MyMyHolderHeader(var binding: ItemAidHeadViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    private fun isHeader(position: Int): Boolean {
        return position == 0
    }
}