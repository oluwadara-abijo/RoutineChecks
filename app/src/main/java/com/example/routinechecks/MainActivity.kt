package com.example.routinechecks

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RoutineListAdapter.ItemClickListener {

    //ViewModel member variable
    private lateinit var mViewModel: RoutineViewModel

    //List of routines
    private lateinit var mRoutines: List<Routine>

    //Request code
    companion object {
        const val newRoutineActivityRequestCode = 1
        const val existingRoutineActivityRequestCode = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRoutines = ArrayList()

        //Set up recycler view
        val recyclerView = routinesList
        val adapter = RoutineListAdapter(mRoutines, this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(recyclerView.context, layoutManager.orientation)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.hasFixedSize()

        //Get ViewModel from Provider
        mViewModel = ViewModelProviders.of(this).get(RoutineViewModel::class.java)

        //Get all routines in database
        mViewModel.allRoutines.observe(this, Observer { routines ->
            routines?.let { adapter.setRoutines(routines) }

        })

        //Start new routine activity when FAB is clicked
        newRoutine.setOnClickListener {
            val intent = Intent(this, NewRoutineActivity::class.java)
            startActivityForResult(intent, newRoutineActivityRequestCode)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val routine = data?.getParcelableExtra(NewRoutineActivity.EXTRA_ROUTINE) as Routine
            when (requestCode) {
                newRoutineActivityRequestCode -> mViewModel.addRoutine(routine)
                existingRoutineActivityRequestCode -> {mViewModel.updateRoutine(routine)}
            }
        }
    }

    override fun onItemClick(routine: Routine, listenerType: RoutineListAdapter.ListenerType) {
        when (listenerType) {
            is RoutineListAdapter.ListenerType.RoutineClickListener -> {
                Toast.makeText(this, "Item clicked", Toast.LENGTH_SHORT).show()
            }
            is RoutineListAdapter.ListenerType.EditClickListener -> {
                val editIntent = Intent(this, NewRoutineActivity::class.java)
                editIntent.putExtra(NewRoutineActivity.EXTRA_ROUTINE, routine)
                startActivityForResult(editIntent, existingRoutineActivityRequestCode)
            }
        }
    }
}
