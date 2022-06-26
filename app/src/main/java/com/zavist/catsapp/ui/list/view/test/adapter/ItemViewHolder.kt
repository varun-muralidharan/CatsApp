package com.zavist.catsapp.ui.list.view.test.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.zavist.catsapp.databinding.CatItemBinding
import com.zavist.catsapp.ui.list.model.Breed

abstract class ItemViewHolder<T: Any> constructor(private val binding: ViewBinding): RecyclerView.ViewHolder(binding.root) {

    abstract fun bind(item: T?)

}

class SampleViewHolder(val binding: CatItemBinding): ItemViewHolder<Breed>(binding) {
    override fun bind(item: Breed?) {
        TODO("Not yet implemented")
    }

    companion object {
        fun from(parent: ViewGroup): SampleViewHolder {
            return SampleViewHolder(
                CatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

}