package edu.newhaven.virtualfarmersmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_product_details_buyer.*

class ProductDetailsBuyer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details_buyer)

        val product = intent.getSerializableExtra("SelectedProduct") as? Product ?: return

        tv_product_name_PDB.text = product.product
        tv_category_PDB.text = product.category
        tv_product_description_PDB.text = product.description
        tv_product_price_PDB.text = product.price.replace("$","")
        tv_quantity_PDB.text = product.quantity
        tv_distance_PDB.text = "Located ${product.distance} Mi from your Location"

    }
}