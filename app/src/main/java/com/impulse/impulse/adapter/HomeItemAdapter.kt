package com.impulse.impulse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.impulse.impulse.R
import com.impulse.impulse.databinding.ItemHomeSituationBinding
import com.impulse.impulse.fragment.HomeFragment
import com.impulse.impulse.model.HomeItem

class HomeItemAdapter(private var fragment: HomeFragment, private var items: ArrayList<HomeItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(ItemHomeSituationBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is MyViewHolder) {
            holder.onBind(fragment, item)
        }
    }

    override fun getItemCount(): Int = items.size

    class MyViewHolder(var binding: ItemHomeSituationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(fragment: HomeFragment, item: HomeItem) {
            binding.apply {
                tvText.text = item.tvText
                Glide.with(fragment).load(item.ivRightIcon).into(ivRightIcon)
                llHome.setOnClickListener {
                    fragment.openFirstAidFragment()
                }
            }
        }
    }
}