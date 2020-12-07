package edu.newhaven.virtualfarmersmarket

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import edu.newhaven.virtualfarmersmarket.Mailer.sendMail
import kotlinx.android.synthetic.main.activity_product_details_buyer.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class ProductDetailsBuyer : AppCompatActivity() {

    private lateinit var btnNotifySeller: Button
    private lateinit var progressBar: ProgressBar
    private val TAG = javaClass.name
    private val dbProductDetailsBuyer = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details_buyer)

        val product = intent.getSerializableExtra("SelectedProduct") as? Product ?: return

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val storageReference = Firebase.storage.getReferenceFromUrl(product.imageLoc)
        GlideApp
            .with(this)
            .load(storageReference)
            .placeholder(circularProgressDrawable)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(iv_product_image_PDB)

        tv_product_name_PDB.text = product.product
        tv_category_PDB.text = product.category
        tv_product_description_PDB.text = product.description
        tv_product_price_PDB.text = product.price.replace("$", "")
        tv_quantity_PDB.text = product.quantity
        tv_distance_PDB.text = "Located ${product.distance} Mi from your Location"

        var sellerEmail: String = ""
        dbProductDetailsBuyer.collection("users")
            .whereEqualTo("userID", product.user)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    sellerEmail = document.getString("emailAddress").toString()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }

        btnNotifySeller = findViewById(R.id.btn_notify_seller)
        progressBar = findViewById(R.id.progressbarPDB)
        progressBar.visibility = View.INVISIBLE;

        btnNotifySeller.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            val emailDialogBuilder = AlertDialog.Builder(this)

            emailDialogBuilder.setMessage("Please Confirm if you are really interested " +
                    "in this product, if not, click on Cancel.")
                .setCancelable(false)
                .setPositiveButton("Confirm", DialogInterface.OnClickListener {
                    dialog, id ->
                    GlobalScope.launch { // or however you do background threads
                        sendMail(sellerEmail)
                        progressBar.visibility = View.INVISIBLE
                    }
                    dialog.cancel()
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                        dialog, id -> dialog.cancel()
                    progressBar.visibility = View.INVISIBLE
                })

            val alert = emailDialogBuilder.create()
            alert.setTitle("Are you sure?")
            alert.show()

        }
    }
}