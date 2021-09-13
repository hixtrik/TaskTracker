package com.hixtrik.tasktracker.data.task

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.text.DateFormat

@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val roomNo: String,
    val service: String,
    val workToDo: String,
    val description: String,
    val status: String,
    var employee: String,
    var startTime: Long? = null,
    var finishTime: Long? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val startTimeFormatted: String
        get() = if (startTime != null) DateFormat.getDateTimeInstance().format(startTime) else ""
    val finishTimeFormatted: String
        get() = if (finishTime != null) DateFormat.getDateTimeInstance().format(finishTime) else ""
}