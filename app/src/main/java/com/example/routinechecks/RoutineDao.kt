package com.example.routinechecks

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

interface RoutineDao {

    @Query("SELECT * from routines_table")
    fun getAllRoutines(): LiveData<List<Routine>>

    @Query("SELECT title from routines_table")
    fun getAllTitles(): List<String>

    @Insert
    fun addRoutine(routine: Routine)

    @Query ("UPDATE routines_table SET completedRoutines = completedRoutines + 1 WHERE title = :title")
    fun markAsDone (title: String)

    @Query ("UPDATE routines_table SET missedRoutines = missedRoutines + 1 WHERE title = :title")
    fun markAsMissed (title: String)
}