package com.example.routinechecks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //ViewModel member variable
    private lateinit var mViewModel: RoutineViewModel

    //Request code
    companion object {
        const val newRoutineActivityRequestCode = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Set up recycler view
        val recyclerView = routinesList
        val adapter = RoutineListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Get ViewModel from Provider
        mViewModel = ViewModelProviders.of(this).get(RoutineViewModel::class.java)

        //Get all routines in database
        mViewModel.allRoutines.observe(this, Observer { routines ->
            routines?.let { adapter.setWords(routines) }
        })

        //Start new routine activity when FAB is clicked
        newRoutine.setOnClickListener {
            val intent = Intent(this, NewRoutineActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == newRoutineActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.let {
                val routine: Routine = it.getParcelableExtra(NewRoutineActivity.EXTRA_ROUTINE)
                mViewModel.addRoutine(routine)
            }
        }
    }
}
