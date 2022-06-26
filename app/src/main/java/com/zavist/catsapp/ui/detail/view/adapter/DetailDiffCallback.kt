package com.zavist.catsapp.ui.detail.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.zavist.catsapp.ui.detail.model.DetailUi

class DetailDiffCallback : DiffUtil.ItemCallback<DetailUi>() {
    override fun areItemsTheSame(oldItem: DetailUi, newItem: DetailUi): Boolean {
        return when(oldItem) {
            is DetailUi.BreedDetail -> newItem is DetailUi.BreedDetail && oldItem.id == newItem.id
            is DetailUi.Speciality -> newItem is DetailUi.Speciality && oldItem.name == newItem.name
        }
    }

    override fun areContentsTheSame(oldItem: DetailUi, newItem: DetailUi): Boolean {
        return when(oldItem) {
            is DetailUi.BreedDetail -> newItem is DetailUi.BreedDetail && oldItem == newItem
            is DetailUi.Speciality -> newItem is DetailUi.Speciality && oldItem == newItem
        }
    }

}
