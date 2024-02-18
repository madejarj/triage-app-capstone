package com.example.beta_ikiosk_vr17

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// General
import android.widget.Toast
import android.text.TextUtils

// Firebase
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// Layouts
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        btn_login.setOnClickListener{
            when {
                TextUtils.isEmpty(et_login_email.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this@SignInActivity,"Please enter email.",Toast.LENGTH_SHORT).show()
                }

                TextUtils.isEmpty(et_login_password.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@SignInActivity,
                        "Please enter password.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {

                    val email: String = et_login_email.text.toString().trim { it <= ' ' }
                    val password: String = et_login_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                            if (task.isSuccessful) {
                                val firebaseUser: FirebaseUser = task.result!!.user!!
                                Toast.makeText(
                                    this@SignInActivity,
                                    "You were logged in successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(
                                    this@SignInActivity, HomeActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id",
                                    FirebaseAuth.getInstance().currentUser!!.uid)
                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@SignInActivity,
                                    task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        )
                }
            }
        }
    }
}
