package edu.newhaven.virtualfarmersmarket

import android.location.Location
import kotlin.random.Random

data class Product (
    val product: String = "",
    val category: String = "",
    val description: String = "",
    val price: String = "",
    val quantity: String = "",
    val idNumber: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
    //val actualLocation: Location
) {
    //val distance = Random.nextInt(1, 20)
    var distance: String = "0.0"
}
