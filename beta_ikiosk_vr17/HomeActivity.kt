package com.example.beta_ikiosk_vr17

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val ccg = create_contact_group.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CreateContactGroup::class.java))
        }

        val ctr = create_temp_report.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CreateTempReport::class.java))
        }

        val ubs = update_status.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CreateStatusUpdate::class.java))
        }

        val dhd = check_dhd.setOnClickListener {
            startActivity(Intent(this@HomeActivity, CheckHealthDeclaration::class.java))
        }
    }
}
