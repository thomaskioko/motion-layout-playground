package com.thomaskioko.materialmotion.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thomaskioko.materialmotion.R

class EpisodeAdapter : RecyclerView.Adapter<EpisodeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_episode_layout, parent, false))
    }

    override fun getItemCount(): Int = 50

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
