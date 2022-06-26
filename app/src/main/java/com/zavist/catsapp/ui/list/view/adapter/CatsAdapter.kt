package com.zavist.catsapp.ui.list.view.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.zavist.catsapp.ui.list.model.Breed

typealias ClickListener = (Breed, Int) -> Unit

class CatsAdapter(
    diffCallback: DiffUtil.ItemCallback<Breed>,
    private val itemClick: ClickListener,
    private val favoriteClick: ClickListener
) : PagingDataAdapter<Breed, CatsViewHolderTemp>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsViewHolderTemp {
        return CatsViewHolder.from(parent, itemClick, favoriteClick)
    }

    override fun onBindViewHolder(holder: CatsViewHolderTemp, position: Int) {
        holder.bind(getItem(position))
    }

}