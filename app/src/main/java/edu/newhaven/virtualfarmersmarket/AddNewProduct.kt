package edu.newhaven.virtualfarmersmarket

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.core.content.FileProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_new_product.*
import java.io.File
import android.content.Intent


class AddNewProduct : AppCompatActivity() {

  private val TAG = javaClass.name
  private val db = FirebaseFirestore.getInstance()
  private var catSelection = ""
  private val fileName = "photo.jpg"
  private val RequestCode = 42
  private lateinit var photoFile: File

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_add_new_product)

      spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(adapterView:  AdapterView<*>?, view: View?, position: Int, id: Long) {
          Toast.makeText(this@AddNewProduct, "You selected ${adapterView?.getItemAtPosition(position).toString()}",
            Toast.LENGTH_LONG).show()
          catSelection = adapterView?.getItemAtPosition(position).toString()
        }
      }

      b_takePicture.setOnClickListener {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(fileName)

        val fileProvider =FileProvider.getUriForFile(this, "edu.newhaven.virtualfarmersmarket.fileprovider", photoFile)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) !=null) {
          startActivityForResult(takePictureIntent, RequestCode)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
       }
      fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK){
          val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
          iv_prodPicture.setImageBitmap(takenImage)
        } else{
            super.onActivityResult(requestCode, resultCode, data)
        }
      }

      db.collection("products")

        b_addItNow.setOnClickListener {

          var edtField: EditText? = null
          val loggedInUser = "JoeUser"

          val products = mutableMapOf(
            "product" to txtProdName.text.toString(),
            "description" to txtDescription.text.toString(),
            "price" to txtPrice.text.toString(),
            "quantity" to txtQuantity.text.toString(),
            "zip" to txtZip.text.toString(),
            "category" to catSelection,
            "user" to loggedInUser
          )

          fun nowClearIt(view: View){
            txtProdName?.text?.clear()
            txtDescription?.text?.clear()
            txtPrice?.text?.clear()
            txtQuantity?.text?.clear()
            txtZip?.text?.clear()
          }


          db.collection("products")
            .add(products)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    TAG,
                    "DocumentSnapshot written with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }

       }
  }

  private fun getPhotoFile(fileName: String): File {
    val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(fileName, ".jpg", storageDirectory)
  }

}
