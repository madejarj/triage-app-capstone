package com.example.beta_ikiosk_vr17

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beta_ikiosk_vr17.Temperature.TempAdapter
import com.example.beta_ikiosk_vr17.Temperature.TempModel
import com.example.beta_ikiosk_vr17.databinding.ActivityCreateTempReportBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_create_temp_report.*

class CreateTempReport : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    lateinit var binding: ActivityCreateTempReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateTempReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        var currentUser = auth.currentUser

        loadAllData(currentUser!!.uid.toString())

        binding.btnAdd.setOnClickListener {
            var temperature = binding.etTemp.text.toString().trim()
            var date = binding.etDate.text.toString().trim()
            var time = binding.etTime.text.toString().trim()
            var diagnosis = binding.etDiagnosis.text.toString().trim()

            if (temperature.isEmpty()) {
                binding.etTemp.setError("Temperature cannot be empty!")
                return@setOnClickListener
            }

            if (date.isEmpty()) {
                binding.etTemp.setError("Date cannot be empty!")
                return@setOnClickListener
            }

            if (time.isEmpty()) {
                binding.etTemp.setError("Time cannot be empty!")
                return@setOnClickListener
            }

            if (diagnosis.isEmpty()) {
                binding.etTemp.setError("Diagnosis cannot be empty!")
                return@setOnClickListener
            }

            val temperatureData = TempModel(temperature, date, time, diagnosis, currentUser!!.uid.toString())
            db.collection("temp_report")
                .add(temperatureData)
                .addOnSuccessListener {
                    Toast.makeText(this@CreateTempReport, "Temperature report has been saved successfully!", Toast.LENGTH_LONG).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@CreateTempReport, "Unsuccessful! Please try again.", Toast.LENGTH_SHORT).show()
                    Log.e("HA", "Error saving : Err :" + it.message)
                }
        }

        //swipe refresh
        binding.refreshTemperature.setOnRefreshListener {
            if (binding.refreshTemperature.isRefreshing) {
                binding.refreshTemperature.isRefreshing = false
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
        startActivity(Intent(this@CreateTempReport, SignInActivity::class.java))
        finish()
    }

    fun goToHome() {
        startActivity(Intent(this@CreateTempReport, HomeActivity::class.java))
        finish()
    }

    //for laoding all task from server
    fun loadAllData(userID: String) {

        val taksList = ArrayList<TempModel>()

        var ref = db.collection(
            "temp_report")
        ref.whereEqualTo(
            "userID", userID)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    Toast.makeText(this@CreateTempReport, "No temperature report found!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
                for (doc in it) {
                    val TempModel = doc.toObject(TempModel::class.java)
                    taksList.add(TempModel)
                }

                binding.rvToDoList.apply {
                    layoutManager =
                        LinearLayoutManager(this@CreateTempReport, RecyclerView.VERTICAL, false)
                    adapter = TempAdapter(taksList, this@CreateTempReport)
                }
            }
    }
}
