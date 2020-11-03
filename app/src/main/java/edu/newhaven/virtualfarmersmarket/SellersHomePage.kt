package edu.newhaven.virtualfarmersmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.content.Intent
import android.widget.Adapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_sellers_home_page.*
import kotlinx.android.synthetic.main.view_holder.*
import java.text.FieldPosition


class SellersHomePage : AppCompatActivity() {
  private val TAG = javaClass.name
  private val db = FirebaseFirestore.getInstance()
  private var adapter : FirestoreRecyclerAdapter<Product, ProductViewHolder>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sellers_home_page)

    val query: Query = db
      .collection("products")
      .whereEqualTo("user","JoeUser")
      //.whereEqualTo ("status", "Added")
      .orderBy ("product")

    val options: FirestoreRecyclerOptions<Product> = FirestoreRecyclerOptions.Builder<Product>()
      .setQuery(query, Product::class.java)
      .build()

    //insert options into adapter
    adapter = object : FirestoreRecyclerAdapter<Product, ProductViewHolder>(options){
      override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int,
        model: Product
      ) {
        holder.name.text = model.product
        holder.price.text = model.price
        holder.quantity.text = model.quantity
      }

      override fun onCreateViewHolder(group: ViewGroup, i: Int): ProductViewHolder {
        val view = LayoutInflater.from(group.context)
          .inflate(R.layout.view_holder, group, false)
        return ProductViewHolder(view)
      }
    }

    rv_seller_products.adapter = adapter
    rv_seller_products.layoutManager = LinearLayoutManager (this)

    b_addProduct.setOnClickListener {
      val intent = Intent(this, AddNewProduct::class.java)
      startActivity(intent)
    }

  }

  override fun onStart(){
    super.onStart()
    adapter?.startListening()
  }

  override fun onStop(){
    super.onStop()
    adapter?.stopListening()
  }
}
