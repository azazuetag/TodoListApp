package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.*

@Dao
interface TaskDAO {

    @Query("select * from task order by priority")
    fun loadAllTask(): List<TaskEntry>

    @Query("SELECT * FROM task WHERE id=:id")
    fun loadTaskById(id: Long):TaskEntry

    @Insert
    fun insertTask(taskEntry: TaskEntry)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)

}