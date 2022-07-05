package uz.impulse.impulse.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.impulse.impulse.databinding.ItemAidHeadViewBinding
import uz.impulse.impulse.databinding.ItemAidViewBinding
import uz.impulse.impulse.fragment.FirstAidFragment
import uz.impulse.impulse.model.FirstAidItem

class FirstAidItemAdapter(
    private var fragment: FirstAidFragment,
    private var items: List<FirstAidItem>
) : BaseAdapter() {
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
        if (holder is MyViewHolder) {
            holder.onBind(position)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class MyViewHolder(var binding: ItemAidViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            val item = items[position]
            binding.apply {
                ivIcon.setImageResource(item.icon!!)
                tvName.text = item.name

                llFirstAidItem.setOnClickListener {
                    fragment.openIllnessPage(position)
                }
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