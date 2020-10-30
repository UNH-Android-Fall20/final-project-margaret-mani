package edu.newhaven.virtualfarmersmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  private val TAG = javaClass.name

  private val db = FirebaseFirestore.getInstance()

  private lateinit var categoryOne: TextView
  private lateinit var categoryTwo: TextView
  private lateinit var categoryThree: TextView
  private lateinit var categoryFour: TextView
  private lateinit var categoryFive: TextView
  private lateinit var addButton: Button

  private lateinit var categoryList: MutableList<String>

  private lateinit var bottom_navigation_menu: BottomNavigationView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    categoryOne = findViewById(R.id.tv_categoryOne)
    categoryTwo = findViewById(R.id.tv_categoryTwo)
    categoryThree = findViewById(R.id.tv_categoryThree)
    categoryFour = findViewById(R.id.tv_categoryFour)
    categoryFive = findViewById(R.id.tv_categoryFive)
    addButton = findViewById<Button>(R.id.b_addProduct)
    bottom_navigation_menu = findViewById(R.id.bottom_navigation_view)

    addButton.setOnClickListener {
        val intent = Intent(this, AddNewProduct::class.java)
        startActivity(intent)
    }

    db.collection("categories").get()
      .addOnSuccessListener { documents ->
        this.categoryList = mutableListOf()
        for (document in documents) {
          document.getString("category")?.let { categoryList.add(it) }
          Log.d(TAG, "${document.id} => ${document.getString("category")}")
        }
        categoryOne.setText(categoryList[0])
        categoryTwo.setText(categoryList[1])
        categoryThree.setText(categoryList[2])
        categoryFour.setText(categoryList[3])
        categoryFive.setText(categoryList[4])
      }

      bottom_navigation_menu.selectedItemId = R.id.nav_home
      bottom_navigation_menu.setOnNavigationItemSelectedListener { item ->
        var message = ""
        when(item.itemId) {
          R.id.nav_sell_home -> {
            val intent = Intent(this, SellersHomePage::class.java)
            startActivity(intent)
          }
          R.id.nav_home -> message = "Home"
          R.id.nav_settings -> message = "Setting"
          R.id.nav_logout -> {
            val intent = Intent(this, ProductListingForBuyer::class.java)
            startActivity(intent)
          }
        }
        Toast.makeText(this, "$message clicked!!", Toast.LENGTH_SHORT).show()
        return@setOnNavigationItemSelectedListener true
      }

  }
}

