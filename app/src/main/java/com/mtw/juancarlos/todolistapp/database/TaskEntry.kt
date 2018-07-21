package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

// TODO (2) Annotate the class with Entity. Use "task" for the table name
@Entity(tableName = "task")
data class TaskEntry (
        @PrimaryKey (autoGenerate = true)
        var id:Long = 0 ,
        var description:String,
        var priority:Int,
        @ColumnInfo(name = "updated_at")
        var updatedAt:Date
)
// TODO (3) Annotate the id as PrimaryKey. Set autoGenerate to true.
// TODO (4) Crear interface TaskDao
// TODO (5) Crear clase AppDatabase : RoomDatabase
// TODO (6) Crear TypeConverter
// TODO (7) Habilitar AllowMainThreadQuery