package edu.newhaven.virtualfarmersmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_user_settings.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import android.util.Log
import android.widget.TextView

private var auth: FirebaseAuth = FirebaseAuth.getInstance()
private lateinit var docID: String

class UserSettings : AppCompatActivity() {

  private val singleUser = Firebase.firestore.collection("users")
  private val db = Firebase.firestore
  private var myData = FirebaseFirestore.getInstance()

  private val TAG = javaClass.name
  private val thisUser = auth.currentUser

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_settings)

    Log.d(TAG, "We got to here")

    val preferredName = findViewById<TextView>(R.id.tv_userFirstName)
    val phoneNbr = findViewById<TextView>(R.id.tv_phoneNumber)
    val mySearchLimit = findViewById<TextView>(R.id.tv_searchLimit)

    //get data
    db.collection("users")
      .whereEqualTo("userID", firebaseUserID)
      .get()
      .addOnSuccessListener { documents ->
        for (document in documents) {
          Log.d(TAG, "${document.id} => ${document.data}")
          docID = document.id
          preferredName.text = document.getString("preferredName")
          phoneNbr.text = document.getString("phoneNbr")
          mySearchLimit.text = document.getString("searchLimit")
        }
      }
      .addOnFailureListener { exception ->
        Log.w(TAG, "Error getting documents: ", exception)
      }
    //update preferred name
    b_updateMe.setOnClickListener {
      Toast.makeText(this, "where the update stuff goes", Toast.LENGTH_LONG).show()

      val newPreferredName : String = et_newUserPreferredName.text.toString()
      val  map : MutableMap<String, Any> = mutableMapOf<String, Any>()
      map["preferredName"] = newPreferredName

      Log.d(TAG, "$firebaseUserID => $map")
      myData
        .collection("users")
        .document(docID)
        .update(map)

      val intent = Intent(this, UserSettings::class.java)
      startActivity(intent)
    }

    //update the phone number
    b_updatePhone.setOnClickListener {

      Toast.makeText(this, "where the update stuff goes", Toast.LENGTH_LONG).show()

      val newPhone : String = et_newPhoneNumber.text.toString()
      val  map : MutableMap<String, Any> = mutableMapOf<String, Any>()
      map["phoneNbr"] = newPhone

      Log.d(TAG, "$firebaseUserID => $map")
      myData
        .collection("users")
        .document(docID)
        .update(map)

      val intent = Intent(this, UserSettings::class.java)
      startActivity(intent)
    }

    //update search limit
    b_updateSearch.setOnClickListener {

      Toast.makeText(this, "where the update stuff goes", Toast.LENGTH_LONG).show()

      val newSearch : String = et_newSearchLimit.text.toString()
      val  map : MutableMap<String, Any> = mutableMapOf<String, Any>()
      map["searchLimit"] = newSearch

      Log.d(TAG, "$firebaseUserID => $map")
      myData
        .collection("users")
        .document(docID)
        .update(map)

      val intent = Intent(this, UserSettings::class.java)
      startActivity(intent)
    }

    b_homepage.setOnClickListener{
      val intent = Intent(this, BuyersHomePage::class.java)
      startActivity(intent)
    }

  }
}
