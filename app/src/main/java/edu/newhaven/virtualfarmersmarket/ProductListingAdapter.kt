package edu.newhaven.virtualfarmersmarket

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.math.RoundingMode
import java.text.DecimalFormat

class ProductListingAdapter(options: FirestoreRecyclerOptions<Product>,
                            private val onDataChanged: OnDataChanged) :
    FirestoreRecyclerAdapter<Product, ProductListingBuyerViewHolder>(options){

    interface OnDataChanged {
        fun dataChanged()
    }

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

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ProductDetailsBuyer::class.java).apply {
                putExtra("SelectedProduct", model)
            }
            holder.itemView.context.startActivity(intent)
        }

        // options: FirestoreRecyclerOptions<Product>, private var resources: Resources
        //val resID = resources.getIdentifier(model.category, "drawable", "edu.newhaven.virtualfarmersmarket")
        holder.productName.text = model.product
        holder.productPrice.text = model.price.replace("$","")
        holder.sellerDistance.text = model.distance
    }

    override fun onDataChanged() {
        super.onDataChanged()
        onDataChanged.dataChanged()
    }

    fun updateAllDistances(loc: Location?) {
        snapshots.forEach {
            val destination = Location("")
            destination.latitude = it.latitude
            destination.longitude = it.longitude
            //destination.latitude = it.actualLocation.latitude
            //destination.longitude = it.actualLocation.longitude
            val distanceMeters = loc?.distanceTo(destination)
            val distanceMiles = distanceMeters?.times(0.000621371)
            if (distanceMiles != null) {
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.CEILING
                it.distance = "${df.format(distanceMiles)} miles" // rounded to 1 decimal place
            }
        }
        notifyDataSetChanged()
    }
}