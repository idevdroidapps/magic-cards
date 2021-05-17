package com.magicthegathering.cards.presentation.adapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.magicthegathering.cards.R

@BindingAdapter("itemImage")
fun ImageView.itemImage(imageUrl: String?){
    imageUrl?.let { url ->
        val options = RequestOptions()
            .placeholder(R.drawable.cardback)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
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

@BindingAdapter("manaCost")
fun TextView.manaCost(cmc: Int?){
    cmc?.let {
        val textString = "Mana Cost: $it"
        text = textString
    }
}

@BindingAdapter("artBy")
fun TextView.artBy(artist: String?){
    artist?.let {
        val textString = "Art By: $it"
        text = textString
    }
}