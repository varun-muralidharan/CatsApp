package com.zavist.catsapp.ui.list.view.adapter

import androidx.recyclerview.widget.DiffUtil
import com.zavist.catsapp.ui.list.model.Breed

class BreedDiffCallback : DiffUtil.ItemCallback<Breed>() {
    override fun areItemsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Breed, newItem: Breed): Boolean {
        return oldItem == newItem
    }
}