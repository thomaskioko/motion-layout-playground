package com.thomaskioko.materialmotion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TweetsAdapter : RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_tweet_view, parent, false))
    }

    override fun getItemCount(): Int = 50

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
