package com.magicthegathering.cards.presentation.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.presentation.viewholders.CardViewHolder

class CardsListAdapter(private val clickListener: (MagicCard) -> Unit) : PagingDataAdapter<MagicCard, RecyclerView.ViewHolder>(CARD_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CardViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            (holder as CardViewHolder).bind(it, clickListener)
        }
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<MagicCard>() {
            override fun areItemsTheSame(oldItem: MagicCard, newItem: MagicCard): Boolean =
                oldItem === newItem

            override fun areContentsTheSame(oldItem: MagicCard, newItem: MagicCard): Boolean =
                oldItem.id == newItem.id
        }
    }

}