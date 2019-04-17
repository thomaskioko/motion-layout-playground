package com.thomaskioko.materialmotion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.thomaskioko.materialmotion.DemoData
import com.thomaskioko.materialmotion.model.ExampleTypes
import com.thomaskioko.materialmotion.MainActivity
import com.thomaskioko.materialmotion.R

class ListAdapter(private val data: Array<DemoData>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list, parent, false) as ConstraintLayout
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.layoutFileId = data[position].layout
        holder.activity = data[position].activity
        holder.exampleType = data[position].type
        holder.title.text = data[position].title
        holder.description.text = data[position].description
    }

    class ViewHolder(layout: ConstraintLayout) : RecyclerView.ViewHolder(layout) {
        var title = layout.findViewById(R.id.tv_title) as TextView
        var description = layout.findViewById(R.id.tv_description) as TextView
        var layoutFileId = 0
        var activity: Class<*>? = null
        var exampleType: ExampleTypes? = null

        init {
            layout.setOnClickListener {
                val context = it?.context as MainActivity
                activity?.let {
                    context.start(it, layoutFileId, exampleType, layoutPosition)
                }

            }
        }
    }

}