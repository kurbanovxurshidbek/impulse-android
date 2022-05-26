package com.impulse.impulse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impulse.impulse.R
import com.impulse.impulse.model.ContactNestedItem

class ContactsNestedItemAdapter(private var items: ArrayList<ContactNestedItem>) :
    BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact_nested_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is MyViewHolder) {
            holder.tvPhoneNumber.text = item.phoneNumber
            holder.tvMessage.text = item.message
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPhoneNumber: TextView = view.findViewById(R.id.tvPhoneNumber)
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
    }
}