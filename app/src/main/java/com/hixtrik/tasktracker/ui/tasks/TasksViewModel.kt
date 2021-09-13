package com.hixtrik.tasktracker.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hixtrik.tasktracker.data.task.Task
import com.hixtrik.tasktracker.data.task.TaskDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    taskDao: TaskDao,
) : ViewModel() {
    val tasks = taskDao.getTasks().asLiveData()
    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()
    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToTaskDetail(task))
    }

    sealed class TasksEvent {
        data class NavigateToTaskDetail(val task: Task) : TasksEvent()
    }
}