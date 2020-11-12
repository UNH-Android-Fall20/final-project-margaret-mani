package edu.newhaven.virtualfarmersmarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_product_listing_for_buyer.*


class ProductListingForBuyer : AppCompatActivity() {

    //private val TAG = javaClass.name
    private val dbProductListingBuyer = FirebaseFirestore.getInstance()

    private lateinit var productListingAdapter: ProductListingAdapter

    private lateinit var categoryFilterView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing_for_buyer)

        val intent = intent
        val categoryFilter = intent.getStringExtra("CategoryClicked")

        categoryFilterView = findViewById(R.id.tv_categoryNamePL)

        val ref: CollectionReference = dbProductListingBuyer.collection("products")

        val query: Query = ref
            .whereEqualTo("category", categoryFilter)
            .orderBy ("product")

        val options: FirestoreRecyclerOptions<Product> = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        categoryFilterView.setText(categoryFilter)

        productListingAdapter = ProductListingAdapter(options)

        rv_product_listing_buyer.adapter = productListingAdapter
        rv_product_listing_buyer.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        productListingAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        productListingAdapter.stopListening()
    }
}