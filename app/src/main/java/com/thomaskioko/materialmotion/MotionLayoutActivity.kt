package com.thomaskioko.materialmotion

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.thomaskioko.materialmotion.adapter.EpisodeAdapter
import com.thomaskioko.materialmotion.adapter.TweetsAdapter
import com.thomaskioko.materialmotion.model.ExampleTypes
import kotlinx.android.synthetic.main.activity_movie_detail.*
import kotlinx.android.synthetic.main.activity_movie_detail.motion_layout


class MotionLayoutActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        val layout = intent.getIntExtra("layoutId", 0)
        val exampleType = intent.getIntExtra("type", 0)
        setContentView(layout)

        handleDataPopulation(exampleType)

    }

    private fun handleDataPopulation(exampleType: Int) {
        when(exampleType){
            ExampleTypes.TWITTER_LIST.ordinal -> populateTweetData()
            ExampleTypes.MOVIE_EPISODES.ordinal -> populateMovieData()
            ExampleTypes.SPLASH_SCREEN.ordinal -> introAnimation()
        }
    }

    private fun populateTweetData(){
        recycler_view_list.apply {
            adapter = TweetsAdapter()
            layoutManager = LinearLayoutManager(this@MotionLayoutActivity)
        }
    }

    private fun populateMovieData(){
        recycler_view_list.apply {
            adapter = EpisodeAdapter()
            layoutManager = LinearLayoutManager(this@MotionLayoutActivity)
        }
    }

    private fun introAnimation(){
        //Start the animation
        motion_layout.transitionToEnd()
    }
}