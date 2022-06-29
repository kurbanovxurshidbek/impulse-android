package uz.impulse.impulse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.impulse.impulse.data.local.entity.Contact
import uz.impulse.impulse.databinding.ItemContactViewBinding
import uz.impulse.impulse.fragment.ContactsFragment

class ContactItemAdapter(private var fragment: ContactsFragment, private var items: List<Contact>) :
    BaseAdapter() {

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
                ivDelete.setOnClickListener {
                    fragment.deleteContactFromDatabase(contact, position)
                }
            }
        }
    }
}