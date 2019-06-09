package com.example.routinechecks

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoutineViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val allRoutines: LiveData<List<Routine>>

    init {
        val routineDao = RoutineRoomDatabase.getDatabase(application).routineDao()
        repository = Repository(routineDao)
        allRoutines = repository.allRoutines
    }

    fun addRoutine(routine: Routine) = viewModelScope.launch(Dispatchers.IO) {
        repository.addRoutine(routine)
    }
}