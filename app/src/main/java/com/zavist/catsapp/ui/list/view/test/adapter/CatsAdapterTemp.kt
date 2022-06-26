package com.zavist.catsapp.ui.list.view.test.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zavist.catsapp.ui.list.model.Breed

typealias ItemClickListener = (Breed, Int) -> Unit

class ItemAdapter<T : Any, VH : ItemViewHolder<T>>(
    diffCallback: DiffUtil.ItemCallback<T>,
    private val itemClick: ItemClickListener,
    private val favoriteClick: ItemClickListener,
    private val method: (ViewGroup) -> VH
) : PagingDataAdapter<T, VH>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return when (viewType) {
            Type.DETAIL.ordinal -> method(
                parent
//                CatItemBinding.inflate(
//                    LayoutInflater.from(parent.context),
//                    parent,
//                    false
//                )
            )
            else -> TODO()
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

}

enum class Type {
    DETAIL
}