package com.magicthegathering.cards.presentation.adapters

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.magicthegathering.cards.R

@BindingAdapter("listItemImage")
fun ImageView.listItemImage(imageUrl: String?){
    imageUrl?.let { url ->
        val options = RequestOptions()
            .placeholder(R.drawable.cardback)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .fitCenter()
        try {
            Glide
                .with(this.context)
                .load(url)
                .apply(options)
                .into(this)
        } catch (e: Exception) {
            Log.e("Glide", "Large Thumbnail Failed in Glide")
        }
    }
}