package com.example.routinechecks

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Routine::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
public abstract class RoutineRoomDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: RoutineRoomDatabase? = null

        fun getDatabase(context: Context): RoutineRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoutineRoomDatabase::class.java,
                    "ROUTINE_DATABASE"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}