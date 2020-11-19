package edu.newhaven.virtualfarmersmarket

import android.app.Activity
import android.app.AlertDialog
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
import android.graphics.ImageDecoder
import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import kotlinx.android.synthetic.main.activity_sellers_home_page.*




private var catSelection = ""
private val FILE_NAME = "photo.jpg"
private val REQUEST_CODE = 42
private lateinit var photoFile: File
private val IMAGE_GALLERY_REQUEST_CODE = 123

private lateinit var storageReference:StorageReference
private var myImgLocation = ""

class AddNewProduct : AppCompatActivity() {
  private val TAG = javaClass.name
  private val db = FirebaseFirestore.getInstance()
  private lateinit var categoryBank: MutableList<String>
  private lateinit var categoryList: Array<String?>
  private var arrayAdapter: ArrayAdapter<String?>? = null

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.activity_add_new_product)

      //init
      storageReference = FirebaseStorage.getInstance().getReference("image")

      db.collection("categories").get()
        .addOnSuccessListener { documents ->
          this.categoryBank = mutableListOf()
          for (document in documents) {
            document.getString("category")?.let { categoryBank.add(it) }
            Log.d(TAG, "${document.id} => ${document.getString("category")}")
          }

          val howMany = categoryBank.size
          categoryList = Array(howMany){null}
          var i = 0

          for (item in categoryBank) {
            categoryList[i] = item
            i += 1
          }
          arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, categoryList)
          spCategories.adapter = arrayAdapter
        }


    spCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        //spinner selection Toast
        override fun onNothingSelected(p0: AdapterView<*>?) {
        }

        override fun onItemSelected(adapterView:  AdapterView<*>?, view: View?, position: Int, id: Long) {
          Toast.makeText(this@AddNewProduct, "You selected ${adapterView?.getItemAtPosition(position).toString()}",
            Toast.LENGTH_LONG).show()
          catSelection = adapterView?.getItemAtPosition(position).toString()
        }
      }

      //gallery module
      b_gImage.setOnClickListener {
        prepOpenImageGallery()
      }

      //photo module
      b_takePicture.setOnClickListener {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        val fileProvider =FileProvider.getUriForFile(this, "edu.newhaven.virtualfarmersmarket.fileprovider", photoFile)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        if (takePictureIntent.resolveActivity(this.packageManager) !=null) {
            startActivityForResult(takePictureIntent, REQUEST_CODE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
      }

      b_sellerPage.setOnClickListener {
        val intent = Intent(this, SellersHomePage::class.java)
        startActivity(intent)
      }

      //communication with firebase adding the product to the list
      db.collection("products")

        b_addItNow.setOnClickListener {

          var edtField: EditText? = null
          val loggedInUser = firebaseUserID
          val productStatus = "Added"

          val products = mutableMapOf(
            //takenImage to firebase
            "product" to txtProdName.text.toString(),
            "description" to txtDescription.text.toString(),
            "price" to txtPrice.text.toString(),
            "quantity" to txtQuantity.text.toString(),
            "zip" to txtZip.text.toString(),
            "category" to catSelection,
            "user" to loggedInUser,
            "imageLoc" to myImgLocation,
            "status" to productStatus
          )

          val intent = Intent(this, AddNewProduct::class.java)
          startActivity(intent)

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

  private fun prepOpenImageGallery() {
    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply{
      this.type = "image/*"
      Log.d(TAG, "get image")
      startActivityForResult(this, IMAGE_GALLERY_REQUEST_CODE)
    }
  }

  //Additional photo functions for transferring the taken photo back to app
  private fun getPhotoFile(fileName: String): File {
    val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(fileName, ".jpg", storageDirectory)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
    super.onActivityResult(requestCode, resultCode, data)
    if (resultCode == Activity.RESULT_OK){

      if (requestCode == REQUEST_CODE) {
        //image from camera
        val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
        iv_prodPicture.setImageBitmap(takenImage)
        //val uploadTask = storageReference.putFile(takenImage)
        val myImgLocation = takenImage.toString().substring(0, takenImage.toString().indexOf("&token"))

      } else if (requestCode == IMAGE_GALLERY_REQUEST_CODE)  {
        //image from gallery
        if (data!!.data != null){
          val image = data.data!!
          iv_prodPicture.setImageURI(image)
          val uploadTask = storageReference.putFile(image)
          //val myImgLocation = image.toString().substring(0, image.toString().indexOf("&token"))
        }
      } else {
        super.onActivityResult(requestCode, resultCode, data)
      }
      val uploadTask = storageReference.putFile(data!!.data!!)

    }
  }

}
