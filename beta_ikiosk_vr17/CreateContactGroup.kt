package com.example.beta_ikiosk_vr17

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta_ikiosk_vr17.Contact.ContactAdapter
import com.example.beta_ikiosk_vr17.Contact.ContactModel
import com.example.beta_ikiosk_vr17.databinding.ActivityCreateContactGroupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateContactGroup : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    lateinit var binding: ActivityCreateContactGroupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateContactGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        var currentUser = auth.currentUser

        loadAllData(currentUser!!.uid.toString())

        binding.btnAdd.setOnClickListener {
            var contact = binding.etContact.text.toString().trim()

            if (contact.isEmpty()) {
                binding.etContact.setError("Contact group cannot be empty!")
                return@setOnClickListener
            }

            val contactData = ContactModel(contact, currentUser!!.uid.toString())
            db.collection("contact_group")
                .add(contactData)
                .addOnSuccessListener {
                    Toast.makeText(this@CreateContactGroup, "Contact group has been saved successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateContactGroup, "Unsuccessful! Please try again.", Toast.LENGTH_SHORT).show()
                    Log.e("HA", "Error saving : Err :" + it.message)
                }
        }

        //swipe refresh
        binding.refreshContact.setOnRefreshListener {
            if (binding.refreshContact.isRefreshing) {
                binding.refreshContact.isRefreshing = false
            }
            loadAllData(currentUser!!.uid)
        }

        binding.btnLogout.setOnClickListener {
            //for logout
            auth.signOut()
            goToLogin()
        }

        binding.btnBack.setOnClickListener {
            goToHome()
        }
    }

    fun goToLogin() {
        startActivity(Intent(this@CreateContactGroup, SignInActivity::class.java))
        finish()
    }

    fun goToHome() {
        startActivity(Intent(this@CreateContactGroup, HomeActivity::class.java))
        finish()
    }

    //for laoding all task from server
    fun loadAllData(userID: String) {

        val taskList = ArrayList<ContactModel>()

        var ref = db.collection(
            "contact_group")
        ref.whereEqualTo(
            "userID", userID)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(this@CreateContactGroup, "No contact group found!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                for (doc in it) {
                    val ContactModel = doc.toObject(ContactModel::class.java)
                    taskList.add(ContactModel)
                }

                binding.rvToDoList.apply {
                    layoutManager =
                        LinearLayoutManager(this@CreateContactGroup, RecyclerView.VERTICAL, false)
                    adapter = ContactAdapter(taskList, this@CreateContactGroup)
                }
            }
    }
}
