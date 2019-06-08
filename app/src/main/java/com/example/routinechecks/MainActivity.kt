package com.example.routinechecks

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Start new routine activity when FAB is clicked
        newRoutine.setOnClickListener {
            val intent = Intent(this, NewRoutineActivity::class.java)
            startActivity(intent)
        }
    }
}
