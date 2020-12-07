package edu.newhaven.virtualfarmersmarket

import android.content.Intent
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.product_listing_buyer_view_holder.view.*
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

        holder.itemView.bt_productListingVhBuy.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Buy button on this item is clicked" + model.product,
                Toast.LENGTH_SHORT
            ).show()
        }

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val storageReference = Firebase.storage.getReferenceFromUrl(model.imageLoc)
        GlideApp
            .with(holder.productImagePL)
            .load(storageReference)
            .placeholder(circularProgressDrawable)
            .into(holder.productImagePL)

        //holder.distanceLogo.visibility = View.INVISIBLE;
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