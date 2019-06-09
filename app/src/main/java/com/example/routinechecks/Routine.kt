package com.example.routinechecks

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "routines_table")
data class Routine(
    @PrimaryKey val title: String,
    val description: String? = null,
    val frequency: String,
    val missedRoutines: Int = 0,
    val completedRoutines: Int = 0,
    val startTime: Date? = null
) : Parcelable