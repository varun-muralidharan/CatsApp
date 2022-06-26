package com.zavist.catsapp.ui.detail.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zavist.catsapp.databinding.CatSpecialityItemBinding
import com.zavist.catsapp.ui.detail.model.DetailUi

class SpecialityViewHolder(val binding: CatSpecialityItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DetailUi.Speciality) {

        with(binding) {
            specialityName.text = item.name
            slider.value = item.score.toFloat()
            slider.isClickable = false
        }

    }

    companion object {
        private const val TAG = "[CAT]SpecialityViewHolder"

        fun from(
            parent: ViewGroup
        ): SpecialityViewHolder {
            return SpecialityViewHolder(
                CatSpecialityItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

}
