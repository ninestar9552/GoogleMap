package cha.n.googlemap.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import cha.n.googlemap.data.model.keyword.Document

/**
 * [BindingAdapter]s for the [Document]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Document>?) {
    items?.let {
        (listView.adapter as RecyclerAdapter).submitList(items)
    }
}