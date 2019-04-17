package com.thomaskioko.materialmotion

import com.thomaskioko.materialmotion.model.ExampleTypes

data class DemoData(
    val title: String,
    val type: ExampleTypes,
    val layout: Int = 0,
    val activity: Class<*> = DemoActivity::class.java
)