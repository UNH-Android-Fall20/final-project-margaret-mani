package edu.newhaven.virtualfarmersmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_sellers_home_page.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.android.synthetic.main.view_holder.view.*


class SellersHomePage : AppCompatActivity() {

  private val TAG = javaClass.name
  private val db = FirebaseFirestore.getInstance()
  private var adapter : FirestoreRecyclerAdapter<Product, ProductViewHolder>? = null
  private lateinit var bottomNavigationViewSellHome: BottomNavigationView
  private lateinit var addButton: FloatingActionButton
 // private lateinit var myProdList: MutableList<Product>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sellers_home_page)



    bottomNavigationViewSellHome = findViewById(R.id.bottom_navigation_view_sell_home)
    addButton = findViewById(R.id.fab)
    val query: Query = db
      .collection("products")
      .whereEqualTo("user", firebaseUserID)
      .whereNotEqualTo ("status", "Deleted")
      .orderBy ("status")
      .orderBy("product")

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

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val storageReference = Firebase.storage.getReferenceFromUrl(model.imageLoc)
        GlideApp
          .with(holder.productPicture)
          .load(storageReference)
          .placeholder(circularProgressDrawable)
          .into(holder.productPicture)

        val myProdId = snapshots.getSnapshot(position).id
        holder.name.text = model.product
        holder.price.text = model.price
        holder.quantity.text = model.quantity

        holder.itemView.setOnClickListener{
          val intent = Intent(holder.itemView.context, ProductDetailsSeller::class.java).apply {
            putExtra("SelectedProduct", model)
          }
          holder.itemView.context.startActivity(intent)
        }


        holder.itemView.b_delete.setOnClickListener{
          Toast.makeText(holder.itemView.context, "Product deleted", Toast.LENGTH_LONG ).show()

          val newStatus = "Deleted"
          val  map : MutableMap<String, Any> = mutableMapOf<String, Any>()
          map["status"] = newStatus

          db.collection("products")
            .document(myProdId)
            .update(map)
        }

        holder.itemView.b_sold.setOnClickListener{
          Toast.makeText(holder.itemView.context, "Product deleted", Toast.LENGTH_LONG ).show()

          val newStatus = "Sold"
          val  map : MutableMap<String, Any> = mutableMapOf<String, Any>()
          map["status"] = newStatus

          db.collection("products")
            .document(myProdId)
            .update(map)
        }

      }

      override fun onCreateViewHolder(group: ViewGroup, i: Int): ProductViewHolder {
        val view = LayoutInflater.from(group.context)
          .inflate(R.layout.view_holder, group, false)
        return ProductViewHolder(view)
      }
    }

    rv_seller_products.adapter = adapter
    rv_seller_products.layoutManager = LinearLayoutManager (this)

    addButton.setOnClickListener{
      val intent = Intent(this, AddNewProduct::class.java)
      startActivity(intent)
    }

    bottomNavigationViewSellHome.background = null
    bottomNavigationViewSellHome.menu.getItem(2).isEnabled = false
    bottomNavigationViewSellHome.selectedItemId = R.id.nav_sell_home
    bottomNavigationViewSellHome.setOnNavigationItemSelectedListener { item ->
      var message = ""
      when(item.itemId) {
        R.id.nav_sell_buy_home -> {
          val intent = Intent(this, BuyersHomePage::class.java)
          startActivity(intent)
        }
        R.id.nav_sell_home -> message = "Home"
        R.id.nav_sell_add_new_product -> {
          val intent = Intent(this, AddNewProduct::class.java)
          startActivity(intent)
        }
        R.id.nav_sell_settings -> {
          val intent = Intent(this, UserSettings::class.java)
          startActivity(intent)}
        R.id.nav_sell_logout -> {
          FirebaseAuth.getInstance().signOut()
          firebaseUserID = ""
          Toast.makeText(
            this@SellersHomePage,"Logged out", Toast.LENGTH_LONG).show()
          val intent = Intent (this, Login::class.java)
          startActivity(intent)
        }
      }
      Toast.makeText(this, "$message clicked!!", Toast.LENGTH_SHORT).show()
      return@setOnNavigationItemSelectedListener true
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
