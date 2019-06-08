package com.example.routinechecks

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "routines_table")
data class Routine(
    @PrimaryKey val title: String,
    val description: String? = null,
    val frequency: String,
    val missedRoutines: Int,
    val completedRoutines: Int,
    val startTime: Date? = null
)