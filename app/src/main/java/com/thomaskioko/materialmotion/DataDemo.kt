package com.thomaskioko.materialmotion

data class DemoData(val title: String, val type: ExampleTypes, val layout: Int = 0, val activity: Class<*> = DemoActivity::class.java) {
    constructor(title: String, type: ExampleTypes, activity: Class<*> = DemoActivity::class.java) : this(title, type, 0, activity)
}