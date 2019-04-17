package com.thomaskioko.materialmotion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.thomaskioko.materialmotion.adapter.ListAdapter
import com.thomaskioko.materialmotion.model.ExampleTypes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = ListAdapter(mAdapterData)
            setHasFixedSize(true)
        }
    }

    fun start(activity: Class<*>, layoutFileId: Int, types: ExampleTypes?) {
        val intent = Intent(this, activity).apply {
            putExtra("layoutId", layoutFileId)
            putExtra("type", types?.ordinal ?: ExampleTypes.MOVIE_EPISODES.ordinal)
        }
        startActivity(intent)
    }
}
