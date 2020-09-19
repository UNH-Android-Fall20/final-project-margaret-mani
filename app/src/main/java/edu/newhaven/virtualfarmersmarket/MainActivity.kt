package edu.newhaven.virtualfarmersmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

  private val TAG = javaClass.name

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    Log.i (TAG, "onCreate fire")

  }
}

