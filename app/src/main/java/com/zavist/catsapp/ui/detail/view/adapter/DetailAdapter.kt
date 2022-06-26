package com.zavist.catsapp.ui.detail.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zavist.catsapp.ui.detail.model.DetailUi

typealias ClickListener = (DetailUi.BreedDetail, Int) -> Unit

class DetailAdapter(
    diffCallback: DiffUtil.ItemCallback<DetailUi>,
    val favoriteClick: ClickListener
): ListAdapter<DetailUi, RecyclerView.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            Type.DETAIL.ordinal -> DetailViewHolder.from(parent, favoriteClick)
            else -> SpecialityViewHolder.from(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is DetailUi.BreedDetail -> Type.DETAIL.ordinal
            else -> Type.SPECIALITY.ordinal
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder) {
            is SpecialityViewHolder -> {
                require(item is DetailUi.Speciality)
                holder.bind(item)
            }
            is DetailViewHolder -> {
                require(item is DetailUi.BreedDetail)
                holder.bind(item)
            }
        }
    }
}

enum class Type { DETAIL, SPECIALITY }
