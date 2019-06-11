package com.example.routinechecks

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class Repository(private val routineDao: RoutineDao) {

    val allRoutines: LiveData<List<Routine>> = routineDao.getAllRoutines()

    @WorkerThread
    fun addRoutine(routine: Routine) {
        routineDao.addRoutine(routine)
    }

    @WorkerThread
    fun updateRoutine(routine: Routine) {
        routineDao.updateRoutine(routine)
    }
}