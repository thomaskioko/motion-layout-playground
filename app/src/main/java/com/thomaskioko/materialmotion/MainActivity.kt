package com.thomaskioko.materialmotion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.thomaskioko.materialmotion.adapter.ListAdapter
import com.thomaskioko.materialmotion.model.DemoData
import com.thomaskioko.materialmotion.model.ExampleTypes
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val mAdapterData: Array<DemoData> = arrayOf(
            DemoData("Tweets", "A demo of Twitter timeline", ExampleTypes.TWITTER_LIST, R.layout.activity_twitter_list),
            DemoData("Breaking Bad", "Breaking bad episode list", ExampleTypes.MOVIE_EPISODES, R.layout.activity_movie_detail),
            DemoData("Screen Intro", "Demo of the famous splash screen", ExampleTypes.SPLASH_SCREEN, R.layout.activity_splash_screen)
    )

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
