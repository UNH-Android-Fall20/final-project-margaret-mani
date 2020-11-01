package edu.newhaven.virtualfarmersmarket

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductListingBuyerViewHolder(view: View): RecyclerView.ViewHolder(view) {
    var productName: TextView = view.findViewById(R.id.tv_productListingVhProductName)
    var productDescription: TextView = view.findViewById(R.id.tv_productListingVhDescription)
    var productPrice: TextView = view.findViewById(R.id.tv__productListingVhPrice)
    var availableQuantity: TextView = view.findViewById(R.id.tv_productListingVhQuantity)
    var categoryName: TextView = view.findViewById(R.id.tv_productListingVhCategoryName)
}