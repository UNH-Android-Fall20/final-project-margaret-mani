package edu.newhaven.virtualfarmersmarket

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductListingBuyerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var productName: TextView = view.findViewById(R.id.tv_productListingVH)
}