package com.magicthegathering.cards.presentation.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.magicthegathering.cards.presentation.viewholders.CardLoadStateViewHolder

class CardLoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<CardLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: CardLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): CardLoadStateViewHolder {
        return CardLoadStateViewHolder.create(parent, retry)
    }
}