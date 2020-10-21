package edu.newhaven.virtualfarmersmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

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

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    categoryOne = findViewById(R.id.tv_categoryOne)
    categoryTwo = findViewById(R.id.tv_categoryTwo)
    categoryThree = findViewById(R.id.tv_categoryThree)
    categoryFour = findViewById(R.id.tv_categoryFour)
    categoryFive = findViewById(R.id.tv_categoryFive)
    addButton = findViewById<Button>(R.id.b_addProduct)

    addButton.setOnClickListener {
        val intent = Intent(this, AddNewProduct::class.java)
        startActivity(intent)
    }

    val docRef = db.collection("categories").document("4GvfeP3OcHm5K5zi5x07")
    docRef.get()
      .addOnSuccessListener { document ->
        if (document != null) {
          Log.d(TAG, "DocumentSnapshot data: ${document.data}")
        } else {
          Log.d(TAG, "No such document")
        }
      }
      .addOnFailureListener { exception ->
        Log.d(TAG, "get failed with ", exception)
      }

    var i: Int = 0
    db.collection("categories").get()
      .addOnSuccessListener { documents ->
        this.categoryList = mutableListOf()
        for (document in documents) {
          document.getString("category")?.let { categoryList.add(it) }
          Log.d(TAG, "${document.id} => ${document.getString("category")}")
          i += 1
        }
        categoryOne.setText(categoryList[0])
        categoryTwo.setText(categoryList[1])
        categoryThree.setText(categoryList[2])
        categoryFour.setText(categoryList[3])
        categoryFive.setText(categoryList[4])
      }
    //Log.d(TAG, categoryList[0].toString())
    /*
    categoryOne.setText(categoryList[0])
    categoryTwo.setText(categoryList[1])
    categoryThree.setText(categoryList[2])
    categoryFour.setText(categoryList[3])
    categoryFive.setText(categoryList[4])
    */
  }
}

