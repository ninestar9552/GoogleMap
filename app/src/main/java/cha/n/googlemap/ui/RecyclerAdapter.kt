package cha.n.googlemap.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cha.n.googlemap.R
import cha.n.googlemap.data.model.keyword.Document
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_list_search_result.view.*

class RecyclerAdapter :
    ListAdapter<Document, RecyclerAdapter.ItemViewHolder>(DocumentDiffCallback()) {

    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_search_result, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder, position: Int
    ) {
        val item = getItem(position)
//        val item = list[position]

        holder.containerView.tvItemPlaceName.text = item.place_name
        if (item.road_address_name != "") {
            holder.containerView.tvItemAddressType.text = "[도로]"
            holder.containerView.tvItemAddressName.text = item.road_address_name
        } else {
            holder.containerView.tvItemAddressType.text = "[지번]"
            holder.containerView.tvItemAddressName.text = item.address_name
        }

        holder.containerView.clSearchResultItem.setOnClickListener {v ->
            listener?.let { it.onItemClick(v, item, position) }
        }
    }

    inner class ItemViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        listener = onItemClickListener
    }
}

interface OnItemClickListener {
    fun onItemClick(v: View, item: Any, position: Int)
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class DocumentDiffCallback : DiffUtil.ItemCallback<Document>() {
    override fun areItemsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Document, newItem: Document): Boolean {
        return oldItem == newItem
    }
}