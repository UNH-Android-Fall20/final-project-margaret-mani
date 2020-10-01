package edu.newhaven.virtualfarmersmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

  private val TAG = javaClass.name

  private val db = FirebaseFirestore.getInstance()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

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

    Log.i (TAG, "onCreate the app created!")

  }
}

