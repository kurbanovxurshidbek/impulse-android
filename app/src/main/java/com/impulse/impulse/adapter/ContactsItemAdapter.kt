package com.impulse.impulse.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.impulse.impulse.R
import com.impulse.impulse.model.Contact
import com.impulse.impulse.model.ContactNestedItem
import com.impulse.impulse.utils.SpacesItemDecoration

class ContactsItemAdapter(var context: Context, private var items: ArrayList<Contact>) :
    BaseAdapter() {
    private var nestedList = ArrayList<ContactNestedItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact_view, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        if (holder is MyViewHolder) {
            if (items.isEmpty()) {
                holder.llContact.visibility = View.GONE
                holder.llNoContact.visibility = View.VISIBLE

            } else {
                holder.llNoContact.visibility = View.GONE
                holder.llContact.visibility = View.VISIBLE
            }
            Glide.with(context).load(item.photo).into(holder.ivProfile)
            holder.tvName.text = item.name
            holder.tvRelation.text = item.relation
            val isExpandable = item.isExpandable
            holder.llExpandable.visibility = if (isExpandable) View.VISIBLE else View.GONE

            if (isExpandable) {
                holder.ivArrow.rotation = 270f
            } else {
                holder.ivArrow.rotation = 90f
            }

            val adapter = ContactsNestedItemAdapter(nestedList)
            val rvChild = holder.rvChild

            rvChild.apply {
                layoutManager = LinearLayoutManager(holder.itemView.context)
                setHasFixedSize(true)
                setAdapter(adapter)
            }

            holder.llContact.setOnClickListener {
                nestedList = item.nestedList
                notifyItemChanged(position)
                item.isExpandable = !item.isExpandable
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llContact: LinearLayout = view.findViewById(R.id.llContact)
        val ivProfile: ShapeableImageView = view.findViewById(R.id.ivProfile)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvRelation: TextView = view.findViewById(R.id.tvRelation)
        val ivArrow: ImageView = view.findViewById(R.id.ivArrow)
        val llExpandable: LinearLayout = view.findViewById(R.id.llExpandable)
        val rvChild: RecyclerView = view.findViewById(R.id.rvChild)
        val llNoContact: LinearLayout = view.findViewById(R.id.llNoContact)

    }
}