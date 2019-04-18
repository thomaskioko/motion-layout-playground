package com.thomaskioko.materialmotion.model

import com.thomaskioko.materialmotion.MotionLayoutActivity

data class DemoData(
    val title: String,
    val description: String,
    val type: ExampleTypes,
    val layout: Int = 0,
    val activity: Class<*> = MotionLayoutActivity::class.java
)