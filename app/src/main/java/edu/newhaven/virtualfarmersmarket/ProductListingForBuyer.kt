package edu.newhaven.virtualfarmersmarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_product_listing_for_buyer.*


class ProductListingForBuyer : AppCompatActivity() {

    private val TAG = javaClass.name
    private val db_productListingBuyer = FirebaseFirestore.getInstance()
    private var adapter : FirestoreRecyclerAdapter<Product, ProductListingBuyerViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing_for_buyer)

        val query: Query = db_productListingBuyer
            .collection("products")
            //.whereEqualTo("category","Fruits")
            .orderBy ("product")

        val options: FirestoreRecyclerOptions<Product> = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        adapter = object : FirestoreRecyclerAdapter<Product, ProductListingBuyerViewHolder>(options){
            override fun onBindViewHolder(
                holder: ProductListingBuyerViewHolder,
                position: Int,
                model: Product
            ) {
                holder.productName.text = model.product
            }

            override fun onCreateViewHolder(
                group: ViewGroup,
                viewType: Int
            ): ProductListingBuyerViewHolder {
                val view = LayoutInflater.from(group.context)
                    .inflate(R.layout.product_listing_buyer_view_holder, group, false)
                return ProductListingBuyerViewHolder(view)
            }
        }

        rv_product_listing_buyer.adapter = adapter
        rv_product_listing_buyer.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }
}