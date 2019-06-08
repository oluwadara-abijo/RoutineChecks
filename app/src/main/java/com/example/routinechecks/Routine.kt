package com.example.routinechecks

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "routines_table")
data class Routine(
    @PrimaryKey val title: String, val description: String,
    val frequency: String,
    val startTime: Date? = null
)