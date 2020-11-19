package edu.newhaven.virtualfarmersmarket

import kotlin.random.Random

data class Product (
    val product: String = "",
    val category: String = "",
    val description: String = "",
    val price: String = "",
    val quantity: String = "",
    val idNumber: String = "",
    val zipCode: String = ""
) {
    //val distance = Random.nextInt(1, 20)
    val distance: String = "0.0"
}
