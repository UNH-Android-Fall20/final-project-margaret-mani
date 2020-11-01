package edu.newhaven.virtualfarmersmarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
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

    private lateinit var categoryFilterView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing_for_buyer)

        val intent = intent
        val categoryFilter = intent.getStringExtra("CategoryClicked")

        categoryFilterView = findViewById(R.id.tv_categoryNamePL)

        val query: Query = db_productListingBuyer
            .collection("products")
            .whereEqualTo("category", categoryFilter)
            .orderBy ("price")

        val options: FirestoreRecyclerOptions<Product> = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        categoryFilterView.setText(categoryFilter)

        adapter = object : FirestoreRecyclerAdapter<Product, ProductListingBuyerViewHolder>(options){
            override fun onBindViewHolder(
                holder: ProductListingBuyerViewHolder,
                position: Int,
                model: Product
            ) {
                holder.productName.text = model.product
                holder.productDescription.text = model.description
                holder.productPrice.text = model.price.replace("$","")
                holder.availableQuantity.text = model.quantity
                holder.categoryName.text = model.category
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