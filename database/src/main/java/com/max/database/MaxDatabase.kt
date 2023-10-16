package com.max.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.max.database.dao.DemoDao
import com.max.database.entity.DemoTable

@Database(entities = [DemoTable::class], version = 1, exportSchema = false)
abstract class MaxDatabase : RoomDatabase() {
    abstract fun demoDao(): DemoDao

    companion object {
        @Volatile
        private var INSTANCE: MaxDatabase? = null

        fun getDatabase(context: Context): MaxDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MaxDatabase::class.java,
                    "${BuildConfig.mr}"
                )
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}