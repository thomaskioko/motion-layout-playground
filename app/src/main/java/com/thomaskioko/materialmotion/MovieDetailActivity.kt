package com.thomaskioko.materialmotion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thomaskioko.materialmotion.adapter.EpisodeAdapter
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        recycler_view_episodes.apply {
            adapter = EpisodeAdapter()
            layoutManager = LinearLayoutManager(this@MovieDetailActivity)
        }
    }
}
