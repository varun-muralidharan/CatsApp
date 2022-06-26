package com.zavist.catsapp.ui.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zavist.catsapp.R
import com.zavist.catsapp.databinding.BreedDetailBinding
import com.zavist.catsapp.ui.detail.model.DetailUi

class DetailViewHolder(
    val binding: BreedDetailBinding,
    val favoriteClick: ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailUi.BreedDetail) {
        with(binding) {
            catName.text = item.name
            catDescription.text = item.description
            catImage.load(item.imageUrl) {
                crossfade(true)
                fallback(R.drawable.fallback_image)
                error(R.drawable.fallback_image)
            }
            catFavorite.setImageResource(
                if (item.favoriteId != -1)
                    R.drawable.ic_baseline_favorite_24
                else R.drawable.ic_baseline_favorite_border_24
            )
            catFavorite.setOnClickListener {
                favoriteClick(item, bindingAdapterPosition)
            }
        }
    }

    companion object {
        private const val TAG = "[CAT]DetailViewHolder"

        fun from(parent: ViewGroup, favoriteClick: ClickListener): DetailViewHolder {
            return DetailViewHolder(
                BreedDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                favoriteClick
            )
        }
    }

}
