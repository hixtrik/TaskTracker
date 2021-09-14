package com.hixtrik.tasktracker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hixtrik.tasktracker.data.enums.Status
import com.hixtrik.tasktracker.data.task.Task
import com.hixtrik.tasktracker.data.task.TaskDao
import com.hixtrik.tasktracker.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao()
            applicationScope.launch {
                dao.insert(
                    Task(
                        "100",
                        "Temizlik",
                        "Alan Temizliği",
                        "alana temizliği açıklaması", Status.Beklemede,
                        "Ali Veli"
                    )
                )
                dao.insert(
                    Task(
                        "205",
                        "Taşıma",
                        "Hasta Taşıma",
                        "hasta taşıma açıklaması",
                        Status.Beklemede,
                        ""
                    )
                )
            }
        }
    }
}