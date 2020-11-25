package edu.newhaven.virtualfarmersmarket

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_product_listing_for_buyer.*

const val PERMISSION_REQUEST_CODE = 0

class ProductListingForBuyer : AppCompatActivity(), ProductListingAdapter.OnDataChanged {

    private val TAG = javaClass.name

    private val dbProductListingBuyer = FirebaseFirestore.getInstance()

    private lateinit var productListingAdapter: ProductListingAdapter

    private lateinit var categoryFilterView: TextView

    private lateinit var bottomNavigationMenuPL: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_listing_for_buyer)

        val intent = intent
        val categoryFilter = intent.getStringExtra("CategoryClicked")

        bottomNavigationMenuPL = findViewById(R.id.bottom_navigation_viewPL)
        categoryFilterView = findViewById(R.id.tv_categoryNamePL)

        val ref: CollectionReference = dbProductListingBuyer.collection("products")

        val query: Query = ref
            .whereEqualTo("category", categoryFilter)
            .orderBy ("product")

        val options: FirestoreRecyclerOptions<Product> = FirestoreRecyclerOptions.Builder<Product>()
            .setQuery(query, Product::class.java)
            .build()

        categoryFilterView.setText(categoryFilter)

        productListingAdapter = ProductListingAdapter(options, this)

        rv_product_listing_buyer.adapter = productListingAdapter
        rv_product_listing_buyer.layoutManager = LinearLayoutManager(this)

        bottomNavigationMenuPL.menu.getItem(0).isCheckable = false
        bottomNavigationMenuPL.setOnNavigationItemSelectedListener { item ->
            var message = ""
            when(item.itemId) {
                R.id.nav_sell_home -> {
                    val intent = Intent(this, SellersHomePage::class.java)
                    startActivity(intent)
                }
                R.id.nav_home -> {
                    val intent = Intent(this, BuyersHomePage::class.java)
                    startActivity(intent)
                }
                R.id.nav_settings -> message = "Setting"
                R.id.nav_logout -> message = "Logout"
            }
            Toast.makeText(this, "$message clicked!!", Toast.LENGTH_SHORT).show()
            return@setOnNavigationItemSelectedListener true
        }

    }

    private fun updateDistances() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation
                .addOnSuccessListener { loc: Location? ->
                    //Log.d(TAG, loc.latitude.toString())
                    //Log.d(TAG, loc.longitude.toString())
                    Log.d(TAG, "Last know location is $loc")
                    productListingAdapter.updateAllDistances(loc)
                }
        } else {
            requestPermissions()
        }

    }

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Log.d("PermissionsRequest", "Permission Granted for ${permissions[0]}")
                    updateDistances()

                } else {
                    Log.d("PermissionsRequest", "Permission Denied")
                }
                return
            }
        }
    }

    override fun onStart() {
        super.onStart()
        productListingAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        productListingAdapter.stopListening()
    }

    override fun dataChanged() {
        updateDistances()
    }
}