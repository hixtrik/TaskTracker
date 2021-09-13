package com.hixtrik.tasktracker.ui.taskDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class TaskDetailViewModel @Inject constructor(
    private val taskDao: TaskDao,
    private val state: SavedStateHandle
) : ViewModel() {
    val task: MutableLiveData<Task> = MutableLiveData<Task>(state.get<Task>("task"))
    var service = state.get<String>("service") ?: task.value?.service ?: ""
        set(value) {
            field = value
            state.set("service", value)
        }
    var workToDo = state.get<String>("workToDo") ?: task.value?.workToDo ?: ""
        set(value) {
            field = value
            state.set("workToDo", value)
        }
    var status = state.get<String>("status") ?: task.value?.status ?: ""
        set(value) {
            field = value
            state.set("status", value)
        }
    var employee = state.get<String>("employee") ?: task.value?.employee ?: ""
        set(value) {
            field = value
            state.set("employee", value)
        }
    var startTime = state.get<Long>("startTime") ?: task.value?.startTime
        set(value) {
            field = value
            state.set("startTime", value)
        }
    var finishTime = state.get<Long>("finishTime") ?: task.value?.finishTime
        set(value) {
            field = value
            state.set("finishTime", value)
        }

    fun onTaskUpdateClick() {
        val updateTask = task.value?.copy(
            employee = employee,
            startTime = startTime,
            finishTime = finishTime,
            status = status
        )
        if (updateTask != null) {
            updateTask(updateTask)
        }
    }

    private fun updateTask(task2: Task) = viewModelScope.launch {
        taskDao.update(task2)
        task.setValue(taskDao.getById(task2.id))
    }
}