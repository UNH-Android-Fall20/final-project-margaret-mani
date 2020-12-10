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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_product_details_seller.*
import kotlinx.android.synthetic.main.activity_sellers_home_page.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import kotlinx.android.synthetic.main.view_holder.*
import kotlinx.android.synthetic.main.view_holder.view.*


class SellersHomePage : AppCompatActivity() {

  private val TAG = javaClass.name
  private val db = FirebaseFirestore.getInstance()
  private var adapter : FirestoreRecyclerAdapter<Product, ProductViewHolder>? = null
  private lateinit var bottomNavigationViewSellHome: BottomNavigationView
  private lateinit var addButton: FloatingActionButton
  private lateinit var auth: FirebaseAuth
  private var currentStatus : String = "Added"


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sellers_home_page)

    auth = Firebase.auth
    val thisUser = auth.currentUser
    Log.d(TAG, "The user currently is $thisUser")

    bottomNavigationViewSellHome = findViewById(R.id.bottom_navigation_view_sell_home)


    addButton = findViewById(R.id.fab)
    val query: Query = db
      .collection("products")
      .whereEqualTo("user", thisUser?.uid)
      .whereEqualTo ("status", "Added")
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
        holder.status.text = model.status

        b_soldStatus.setOnClickListener{
          currentStatus = "Sold"
          b_soldStatus.visibility = View.INVISIBLE
          tv_statusSold.visibility = View.INVISIBLE
          b_addedStatus.visibility = View.VISIBLE
          tv_currentProducts.visibility = View.VISIBLE

          val newQuery: Query = db
            .collection("products")
            .whereEqualTo("user", thisUser?.uid)
            .whereEqualTo ("status", currentStatus)
            .orderBy ("status")
            .orderBy("product")

          val newOptions = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(newQuery, Product::class.java)
            .build()
          adapter!!.updateOptions(newOptions)
        }

        b_addedStatus.setOnClickListener{
          currentStatus = "Added"
          b_soldStatus.visibility = View.VISIBLE
          tv_statusSold.visibility = View.INVISIBLE
          b_addedStatus.visibility = View.INVISIBLE
          tv_currentProducts.visibility = View.VISIBLE

          val newQuery: Query = db
            .collection("products")
            .whereEqualTo("user", thisUser?.uid)
            .whereEqualTo ("status", currentStatus)
            .orderBy ("status")
            .orderBy("product")

          val newOptions = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(newQuery, Product::class.java)
            .build()
          adapter!!.updateOptions(newOptions)
        }

        holder.itemView.setOnClickListener{
          val intent = Intent(holder.itemView.context, ProductDetailsSeller::class.java).apply {
            putExtra("SelectedProduct", model)
            putExtra("myProdId", myProdId)
          }
          holder.itemView.context.startActivity(intent)
        }

        holder.itemView.b_edit.setOnClickListener{
          val intent = Intent(holder.itemView.context, ProductDetailsSeller::class.java).apply {
            putExtra("SelectedProduct", model)
            putExtra("myProdId", myProdId)
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
          Toast.makeText(holder.itemView.context, "Product sold out", Toast.LENGTH_LONG ).show()

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
          finishAffinity()
          Toast.makeText(
            this@SellersHomePage,"Logged out", Toast.LENGTH_LONG).show()
          val intent = Intent (this, BuyersHomePage::class.java)
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

  override fun onResume() {
    super.onResume()
    bottomNavigationViewSellHome.getMenu().getItem(1).setChecked(true);
  }
}
