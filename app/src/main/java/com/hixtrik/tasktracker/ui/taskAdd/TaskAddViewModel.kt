package com.hixtrik.tasktracker.ui.taskAdd

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hixtrik.tasktracker.data.enums.Status
import com.hixtrik.tasktracker.data.task.Task
import com.hixtrik.tasktracker.data.task.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

//┌──────────────────────────┐
//│ Created by Taha ARICAN   │
//│ aricantaha06@gmail.com   │            
//│ 10.09.2021               │
//└──────────────────────────┘
@HiltViewModel
class TaskAddViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {
    val task = state.get<Task>("task")
    var roomNo = state.get<String>("roomNo") ?: task?.roomNo ?: ""
        set(value) {
            field = value
            state.set("roomNo", value)
        }
    var service = state.get<String>("service") ?: task?.service ?: ""
        set(value) {
            field = value
            state.set("service", value)
        }
    var workToDo = state.get<String>("workToDo") ?: task?.workToDo ?: ""
        set(value) {
            field = value
            state.set("workToDo", value)
        }
    var description = state.get<String>("description") ?: task?.description ?: ""
        set(value) {
            field = value
            state.set("description", value)
        }

    fun onTaskAddClick() {
        val newTask = Task(
            roomNo = roomNo,
            service = service,
            workToDo = workToDo,
            description = description,
            employee = "",
            status = Status.Beklemede
        )
        createTask(newTask)
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
    }

}