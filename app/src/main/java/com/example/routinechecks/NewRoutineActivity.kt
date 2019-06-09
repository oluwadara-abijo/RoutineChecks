package com.example.routinechecks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_new_routine.*

class NewRoutineActivity : AppCompatActivity() {

    private var frequency: String = ""

    companion object {
        const val EXTRA_ROUTINE = "routine"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_routine)

        setupSpinner()

        //Set click listener on save button
        saveButton.setOnClickListener { createNewRoutine() }

    }

    private fun createNewRoutine() {
        val title = routineNameInput.text.toString()
        val description = routineDescriptionInput.text.toString()
        val routine = Routine(title, description, frequency)

        val intent = Intent()
        intent.putExtra(EXTRA_ROUTINE, routine)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun setupSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this, R.array.frequency_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                frequencySpinner.adapter = adapter
            }
        frequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    frequency = parent.getItemAtPosition(position).toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                frequency = "Daily"
            }
        }
    }

}
