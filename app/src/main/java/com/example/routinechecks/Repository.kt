package com.example.routinechecks

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class Repository(private val routineDao: RoutineDao) {

    val allRoutines: LiveData<List<Routine>> = routineDao.getAllRoutines()

    @WorkerThread
    suspend fun insert(routine: Routine) {
        routineDao.addRoutine(routine)
    }
}