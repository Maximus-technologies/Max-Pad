package com.max.database.dao

import androidx.room.Query
import com.max.database.entity.DemoTable

interface DemoDao {
    @Query("SELECT * FROM max_demo ORDER BY name ASC")
    fun getAll(): List<DemoTable>
}