package com.impulse.impulse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.impulse.impulse.database.model.Contact
import com.impulse.impulse.databinding.ItemContactViewBinding
import com.impulse.impulse.fragment.ContactsFragment

class ContactItemAdapter(var items: List<Contact>, var listener: OnItemClickListener) :
    BaseAdapter() {
    private var isExpandable = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactViewHolder(ItemContactViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactViewHolder) {
            val contact = items[position]
            holder.onBind(contact, position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ContactViewHolder(var binding: ItemContactViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(contact: Contact, position: Int) {
            binding.apply {
                tvName.text = contact.name
                tvPhoneNumber.text = contact.number

                llContact.setOnClickListener {
                    if (isExpandable) {
                        ivArrow.rotation = 90f
                    } else {
                        ivArrow.rotation = 270f
                    }
                    isExpandable = !isExpandable
                    listener.onItemClicked(binding.llPhoneNumber, position, isExpandable)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(layout: LinearLayout, position: Int, isExpandable: Boolean)
    }
}