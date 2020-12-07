package edu.newhaven.virtualfarmersmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_product_details_buyer.*

class ProductDetailsSeller : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_product_details_seller)

    val product = intent.getSerializableExtra("SelectedProduct") as? Product ?: return

    val circularProgressDrawable = CircularProgressDrawable(this)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()

    val storageReference = Firebase.storage.getReferenceFromUrl(product.imageLoc)
    GlideApp
      .with(this)
      .load(storageReference)
      .placeholder(circularProgressDrawable)
      .into(iv_product_image_PDB)


    tv_product_name_PDB.text = product.product
    tv_category_PDB.text = product.category
    tv_product_description_PDB.text = product.description
    tv_product_price_PDB.text = product.price.replace("$","")
    tv_quantity_PDB.text = product.quantity



  }
}
