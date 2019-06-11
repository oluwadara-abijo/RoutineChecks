package com.example.routinechecks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_new_routine.*

class NewRoutineActivity : AppCompatActivity() {

    private var frequency: String = "Daily"

    private var mRoutine: Routine? = null

    private var isNewRoutine: Boolean = true

    companion object {
        const val EXTRA_ROUTINE = "mRoutine"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_routine)

        //Get the current Routine to be edited
        val intent = intent
        if (intent.hasExtra(EXTRA_ROUTINE)) {
            isNewRoutine = false
            mRoutine = intent.getParcelableExtra(EXTRA_ROUTINE)
            populateUI(mRoutine!!)
        }

        setupSpinner()

        //Set click listener on save button
        saveButton.setOnClickListener { saveRoutine() }

    }

    private fun saveRoutine() {
        val title = routineNameInput.text.toString()
        val description = routineDescriptionInput.text.toString()
        mRoutine = if (isNewRoutine) {
            Routine(title = title, description = description, frequency = frequency)
        } else {
            Routine(mRoutine!!.id, title, description, frequency)
        }

        val intent = Intent()
        Log.d(">>>", mRoutine.toString())
        intent.putExtra(EXTRA_ROUTINE, mRoutine)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun populateUI(routine: Routine) {
        routineNameInput.setText(routine.title)
        routineDescriptionInput.setText(routine.description)
        saveButton.text = getString(R.string.update)
    }

    private fun frequencyPosition(frequency: String): Int {
        var position = 2
        when (frequency) {
            "Hourly" -> position = 1
            "Daily" -> position = 2
            "Weekly" -> position = 3
            "Monthly" -> position = 4
            "Yearly" -> position = 5
        }
        return position
    }

    private fun setupSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(this, R.array.frequency_array, android.R.layout.simple_spinner_item)
            .also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                frequencySpinner.adapter = adapter
                if (!isNewRoutine) {
                    frequencySpinner.setSelection(frequencyPosition(mRoutine!!.frequency))
                }
            }
        frequencySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (parent != null) {
                    frequency = if (position == 0) {
                        "Daily"
                    } else {
                        parent.getItemAtPosition(position).toString()
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                frequency = "Daily"
            }
        }
    }

}
