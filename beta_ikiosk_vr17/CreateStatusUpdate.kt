package com.example.beta_ikiosk_vr17

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta_ikiosk_vr17.Status.StatusAdapter
import com.example.beta_ikiosk_vr17.Status.StatusModel
import com.example.beta_ikiosk_vr17.databinding.ActivityCreateStatusUpdateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreateStatusUpdate : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    lateinit var binding: ActivityCreateStatusUpdateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateStatusUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        var currentUser = auth.currentUser

        loadAllData(currentUser!!.uid.toString())

        binding.btnAdd.setOnClickListener {
            var covid = binding.etStatusCovid.text.toString().trim()
            var vaccine = binding.etStatusVaccine.text.toString().trim()
            var date = binding.etStatusDate.text.toString().trim()

            if (covid.isEmpty()) {
                binding.etStatusCovid.setError("COVID status cannot be empty!")
                return@setOnClickListener
            }

            if (vaccine.isEmpty()) {
                binding.etStatusVaccine.setError("Vaccine status cannot be empty!")
                return@setOnClickListener
            }

            if (date.isEmpty()) {
                binding.etStatusDate.setError("Status date cannot be empty!")
                return@setOnClickListener
            }

            val bulletinData = StatusModel(covid, vaccine, date, currentUser!!.uid.toString())
            db.collection("status_history")
                .add(bulletinData)
                .addOnSuccessListener {
                    Toast.makeText(this@CreateStatusUpdate, "Status update has been saved successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateStatusUpdate, "Unsuccessful! Please try again.", Toast.LENGTH_SHORT).show()
                    Log.e("HA", "Error saving : Err :" + it.message)
                }
        }

        //swipe refresh
        binding.refreshBulletin.setOnRefreshListener {
            if (binding.refreshBulletin.isRefreshing) {
                binding.refreshBulletin.isRefreshing = false
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
        startActivity(Intent(this@CreateStatusUpdate, SignInActivity::class.java))
        finish()
    }

    fun goToHome() {
        startActivity(Intent(this@CreateStatusUpdate, HomeActivity::class.java))
        finish()
    }

    //for laoding all task from server
    fun loadAllData(userID: String) {

        val taskList = ArrayList<StatusModel>()

        var ref = db.collection(
            "status_history")
        ref.whereEqualTo(
            "userID", userID)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(this@CreateStatusUpdate, "No status update found!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                for (doc in it) {
                    val BulletinModel = doc.toObject(StatusModel::class.java)
                    taskList.add(BulletinModel)
                }

                binding.rvToDoList.apply {
                    layoutManager =
                        LinearLayoutManager(this@CreateStatusUpdate, RecyclerView.VERTICAL, false)
                    adapter = StatusAdapter(taskList, this@CreateStatusUpdate)
                }
            }
    }
}
