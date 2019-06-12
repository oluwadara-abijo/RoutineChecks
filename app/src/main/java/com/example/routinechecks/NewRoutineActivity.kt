package com.example.routinechecks

import android.app.*
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_new_routine.*
import java.text.SimpleDateFormat
import java.util.*
import android.app.PendingIntent
import com.google.android.material.snackbar.Snackbar

class NewRoutineActivity : AppCompatActivity() {

    private var frequency: String = "Daily"

    private lateinit var mRoutine: Routine

    private var isNewRoutine: Boolean = true

    private var timePicked: String = ""
    private var datePicked: String = ""

    private var hourPicked: Int = 0
    private var minutePicked: Int = 0
    private var dayPicked: Int = 0
    private var monthPicked: Int = 0
    private var yearPicked: Int = 0

    companion object {
        const val EXTRA_ROUTINE = "mRoutine"
        //Routine frequency options
        const val FREQ_HOURLY = "Hourly"
        const val FREQ_DAILY = "Daily"
        const val FREQ_WEEKLY = "Weekly"
        const val FREQ_MONTHLY = "Monthly"
        const val FREQ_YEARLY = "Yearly"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_routine)

        //Get the current Routine to be edited
        val intent = intent
        if (intent.hasExtra(EXTRA_ROUTINE)) {
            isNewRoutine = false
            mRoutine = intent.getParcelableExtra(EXTRA_ROUTINE)
            populateUI(mRoutine)
        }

        setupSpinner()

        //Set click listener on save button
        saveButton.setOnClickListener { saveRoutine() }

        //Set click listener on start time edit text
        startTimeInput.setOnClickListener { pickStartTime() }

        //Set click listener on start date edit text
        startDateInput.setOnClickListener { pickStartDate() }

    }

    private fun saveRoutine() {
        val title = routineNameInput.text.toString()
        val description = routineDescriptionInput.text.toString()

        if (title.isBlank() || startDateInput.text.toString().isBlank() || startTimeInput.text.toString().isBlank()) {
            Snackbar.make(saveButton, "Fill in all required fields", Snackbar.LENGTH_SHORT).show()
        } else {
            val startTime = getRoutineDate(timePicked, datePicked)
            mRoutine = if (isNewRoutine) {
                Routine(title = title, description = description, frequency = frequency, startTime = startTime)
            } else {
                Routine(mRoutine.id, title, description, frequency, startTime = startTime)
            }

            setReminder(this)
            val intent = Intent()
            intent.putExtra(EXTRA_ROUTINE, mRoutine)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

    }

    private fun populateUI(routine: Routine) {
        routineNameInput.setText(routine.title)
        routineDescriptionInput.setText(routine.description)
        saveButton.text = getString(R.string.update)
    }

    private fun frequencyPosition(frequency: String): Int {
        var position = 2
        when (frequency) {
            FREQ_HOURLY -> position = 1
            FREQ_DAILY -> position = 2
            FREQ_WEEKLY -> position = 3
            FREQ_MONTHLY -> position = 4
            FREQ_YEARLY -> position = 5
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
                    frequencySpinner.setSelection(frequencyPosition(mRoutine.frequency))
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

    private fun formatTime(hour: Int, min: Int): String {
        //Format time string to contain two digits
        val twoDigitMinute: String = if (min < 10) {
            "0$min"
        } else {
            min.toString()
        }
        val twoDigitHour: String = if (hour < 10) {
            "0$hour"
        } else {
            hour.toString()
        }
        return "$twoDigitHour:$twoDigitMinute"
    }

    private fun pickStartTime() {

        //Set default values for the picker to be current time
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        //Create a new instance of TimePickerDialog and show it
        val timePickerDialog = TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hourSet, minuteSet ->
                //Set time chosen on edit text
                hourPicked = hourSet
                minutePicked = minuteSet
                timePicked = formatTime(hourSet, minuteSet)
                startTimeInput.setText(timePicked)
            },
            hour,
            minute,
            true
        )
        timePickerDialog.setTitle("")
        timePickerDialog.show()
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        //Format time string to contain two digits for day and month
        val monthDigit = month + 1
        val twoDigitDay: String = if (day < 10) {
            "0$day"
        } else {
            day.toString()
        }
        val twoDigitMonth: String = if (monthDigit < 10) {
            "0$monthDigit"
        } else {
            monthDigit.toString()
        }
        return "$twoDigitDay-$twoDigitMonth-$year"
    }

    private fun pickStartDate() {

        //Set default values for the picker to be current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        //Create a new instance of DatePickerDialog and show it
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, yearSet, monthSet, daySet ->
                //Set date chosen on edit text
                dayPicked = daySet
                monthPicked = monthSet
                yearPicked = yearSet
                datePicked = formatDate(daySet, monthSet, yearSet)
                startDateInput.setText(datePicked)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.setTitle("")
        datePickerDialog.show()
    }

    private fun getRoutineDate(timeSet: String, dateSet: String): Date {
        val dateString = dateSet + "T" + timeSet
        val dateFormat = SimpleDateFormat("dd-MM-yyyy'T'HH:mm", Locale.getDefault())
        return dateFormat.parse(dateString)
    }

    private fun getRoutineFrequency(routine: Routine): Long {
        var freq: Long = AlarmManager.INTERVAL_DAY
        when (routine.frequency) {
            FREQ_HOURLY -> freq = AlarmManager.INTERVAL_HOUR
            FREQ_DAILY -> freq = AlarmManager.INTERVAL_DAY
            FREQ_WEEKLY -> freq = AlarmManager.INTERVAL_DAY * 7
            FREQ_MONTHLY -> freq = AlarmManager.INTERVAL_DAY * 30
            FREQ_YEARLY -> freq = AlarmManager.INTERVAL_DAY * 365
        }
        return freq
    }

    private fun setReminder(context: Context) {
        val alarmMgr: AlarmManager?
        lateinit var alarmIntent: PendingIntent
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent =
            Intent(context, RoutineService::class.java).let { intent ->
                PendingIntent.getBroadcast(context, 0, intent, 0)
            }
        val extraIntent = Intent("my.action.routine")
        extraIntent.putExtra(RoutineService.EXTRA_ROUTINE, mRoutine)
        sendBroadcast(extraIntent)

        // Set the alarm to start at chosen start time
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_MONTH, dayPicked)
            set(Calendar.MONTH, monthPicked)
            set(Calendar.YEAR, yearPicked)
            set(Calendar.HOUR_OF_DAY, hourPicked)
            set(Calendar.MINUTE, minutePicked - 5)
            set(Calendar.SECOND, 0)
        }

        //specify alarm frequency
        alarmMgr.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            getRoutineFrequency(mRoutine),
            alarmIntent
        )
    }

}
