package edu.newhaven.virtualfarmersmarket

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ProductViewHolder (view: View) : RecyclerView.ViewHolder(view) {
  var name: TextView = view.findViewById(R.id.tv_prodName)

}