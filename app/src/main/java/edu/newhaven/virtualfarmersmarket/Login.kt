package edu.newhaven.virtualfarmersmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.pb_progress
import kotlinx.android.synthetic.main.activity_registration.*

private lateinit var txtEmail: EditText
private lateinit var txtPassword: EditText
private lateinit var newUser: TextView
private lateinit var pbProgress: ProgressBar
private lateinit var auth: FirebaseAuth
var firebaseUserID: String = ""
private lateinit var bLogin: Button


class Login : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    auth = FirebaseAuth.getInstance()
    pbProgress = findViewById(R.id.pb_progress)
    txtEmail = findViewById(R.id.txt_email)
    txtPassword = findViewById(R.id.txt_password)
    bLogin = findViewById(R.id.b_login)
    newUser = findViewById(R.id.tv_registerButton)

    b_login.setOnClickListener {
      val email: String = txtEmail.text.toString()
      val password: String = txtPassword.text.toString()

      if (email.isEmpty()) {
        Toast.makeText(
          this@Login, "Email required",
          Toast.LENGTH_SHORT
        ).show()
      } else if (password.isEmpty()) {   //checking for user name
        Toast.makeText(
          this@Login, "Password required",
          Toast.LENGTH_SHORT
        ).show()
        return@setOnClickListener
      } else if (password.length < 6) {    //check password length
        Toast.makeText(
          this@Login, "The password must be at least 6 characters",
          Toast.LENGTH_SHORT
        ).show()
        return@setOnClickListener
      } else {
        pb_progress.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener { task ->
            if (task.isSuccessful) {
              firebaseUserID = auth.currentUser!!.uid
              Toast.makeText(
                this@Login,"Login successful", Toast.LENGTH_LONG).show()
              val intent = Intent(this, MainActivity::class.java)
              startActivity(intent)
            } else {
              Toast.makeText(
                this@Login,
                "User not logged in", Toast.LENGTH_LONG).show()
              pb_progress.visibility = View.GONE
            }
          }
      }
    }
    newUser.setOnClickListener{
      val intent = Intent(this, Registration::class.java)
      startActivity(intent)
    }

  }
}
