package com.hixtrik.tasktracker.ui.taskDetail

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hixtrik.tasktracker.R
import com.hixtrik.tasktracker.data.enums.Status
import com.hixtrik.tasktracker.data.task.Task
import com.hixtrik.tasktracker.data.user
import com.hixtrik.tasktracker.databinding.FragmentTaskDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskDetailFragment : Fragment(R.layout.fragment_task_detail) {
    private val viewModel: TaskDetailViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskDetailBinding.bind(view)
        val task = Observer<Task> { _ ->
            binding.apply {
                tvService.text = viewModel.service
                tvWorkDesc.text = viewModel.workToDo
                tvStatus.text = viewModel.status.toString()
                tvEmployee.text = viewModel.employee
                tvStartTime.text = viewModel.task.value?.startTimeFormatted
                tvFinishTime.text = viewModel.task.value?.finishTimeFormatted
                if (viewModel.startTime != null && viewModel.finishTime != null) {
                    val passedTimeMillis = viewModel.finishTime!! - viewModel.startTime!!
                    tvPassedTime.text = convertSecondsToHMmSs(passedTimeMillis / 1000)
                }
            }
        }
        viewModel.task.observe(viewLifecycleOwner, task)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        val opt = Observer<Task> {
            when (viewModel.status) {
                Status.Beklemede -> {
                    menu.findItem(R.id.actWait).isVisible = false
                    if (viewModel.startTime != null) {
                        menu.findItem(R.id.actReturn).isVisible = true
                        menu.findItem(R.id.actStart).isVisible = false
                        menu.findItem(R.id.actFinish).isVisible = false
                    } else {
                        menu.findItem(R.id.actFinish).isVisible = false
                        menu.findItem(R.id.actReturn).isVisible = false
                        menu.findItem(R.id.actStart).isVisible = true
                    }
                }
                Status.Tamamlandı -> {
                    menu.findItem(R.id.actStart).isVisible = false
                    menu.findItem(R.id.actWait).isVisible = false
                    menu.findItem(R.id.actFinish).isVisible = false
                    menu.findItem(R.id.actReturn).isVisible = false
                }
                Status.Yapılıyor -> {
                    menu.findItem(R.id.actStart).isVisible = false
                    menu.findItem(R.id.actReturn).isVisible = false
                    menu.findItem(R.id.actWait).isVisible = true
                    menu.findItem(R.id.actFinish).isVisible = true

                }
            }
        }
        viewModel.task.observe(viewLifecycleOwner, opt)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actStart -> {
                viewModel.startTime = System.currentTimeMillis()
                viewModel.employee = user
                viewModel.status = Status.Yapılıyor
                viewModel.onTaskUpdateClick()
                return true
            }
            R.id.actWait -> {
                viewModel.status = Status.Beklemede
                viewModel.onTaskUpdateClick()
                return true
            }
            R.id.actReturn -> {
                viewModel.status = Status.Yapılıyor
                viewModel.onTaskUpdateClick()
                return true
            }
            R.id.actFinish -> {
                viewModel.finishTime = System.currentTimeMillis()
                viewModel.status = Status.Tamamlandı
                viewModel.onTaskUpdateClick()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }
}

fun convertSecondsToHMmSs(seconds: Long): String {
    val s = seconds % 60
    val m = seconds / 60 % 60
    val h = seconds / (60 * 60) % 24
    return String.format("%d:%02d:%02d", h, m, s)
}