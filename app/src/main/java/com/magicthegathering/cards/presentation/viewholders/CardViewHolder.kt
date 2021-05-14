package com.magicthegathering.cards.presentation.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.magicthegathering.cards.databinding.ListItemCardBinding
import com.magicthegathering.cards.domain.entities.MagicCard

class CardViewHolder(private val binding: ListItemCardBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(
        item: MagicCard,
        clickListener: (MagicCard) -> Unit
    ) {
        binding.card = item
        binding.root.setOnClickListener {
            binding.card?.apply { clickListener(this) }
        }
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): CardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemCardBinding.inflate(layoutInflater, parent, false)
            return CardViewHolder(binding)
        }
    }
}