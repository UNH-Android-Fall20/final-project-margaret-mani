package edu.newhaven.virtualfarmersmarket

import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions

class ProductListingAdapter(options: FirestoreRecyclerOptions<Product>) : FirestoreRecyclerAdapter<Product, ProductListingBuyerViewHolder>(options){

    private val TAG = javaClass.name

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductListingBuyerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_listing_buyer_view_holder, parent, false)
        return ProductListingBuyerViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ProductListingBuyerViewHolder,
        position: Int,
        model: Product
    ) {
        // options: FirestoreRecyclerOptions<Product>, private var resources: Resources
        //val resID = resources.getIdentifier(model.category, "drawable", "edu.newhaven.virtualfarmersmarket")
        holder.productName.text = model.product
        holder.productPrice.text = model.price.replace("$","")
        holder.sellerDistance.text = "${model.distance} miles"
    }

    fun updateAllDistances(loc: Location?) {
        snapshots.forEach {
            val destination = Location("")
            //destination.latitude =
            /*
            destination.

             */
        }
    }
}