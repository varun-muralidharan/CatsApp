package com.zavist.catsapp.ui.list.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.zavist.catsapp.R
import com.zavist.catsapp.databinding.CatItemBinding
import com.zavist.catsapp.ui.list.model.Breed

class CatsViewHolderTemp(
    private val binding: CatItemBinding,
    private val itemClick: ClickListener,
    private val favoriteClick: ClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Breed?) {
        with(binding) {
            item?.let {
                catItem.setOnClickListener { itemClick(item, bindingAdapterPosition) }
                catImage.load(item.imageUrl) {
                    crossfade(true)
                    fallback(R.drawable.fallback_image)
                    error(R.drawable.fallback_image)
                }
                catFavorite.setOnClickListener { favoriteClick(item, bindingAdapterPosition) }
                catFavorite.setImageResource(
                    if (item.favoriteId != -1)
                        R.drawable.ic_baseline_favorite_24
                    else R.drawable.ic_baseline_favorite_border_24
                )
            }
            catName.text = item?.name ?: "Loading cat name"
            catDescription.text = item?.description ?: "Loading cat description"
        }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            itemClick: ClickListener,
            favoriteClick: ClickListener
        ): CatsViewHolderTemp {
            return CatsViewHolderTemp(
                CatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                itemClick,
                favoriteClick
            )
        }
    }

}