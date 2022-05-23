package com.impulse.impulse.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.impulse.impulse.R
import com.impulse.impulse.model.HomeItem

class HomeItemAdapter(private var context: Context, private var items: ArrayList<HomeItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home_situation, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is MyViewHolder) {
            holder.tvText.text = item.tvText
            Glide.with(context).load(item.ivRightIcon).into(holder.ivRightIcon)
        }
    }

    override fun getItemCount(): Int = items.size

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvText: TextView = view.findViewById(R.id.tvText)
        val ivRightIcon: ImageView = view.findViewById(R.id.ivRightIcon)
    }
}