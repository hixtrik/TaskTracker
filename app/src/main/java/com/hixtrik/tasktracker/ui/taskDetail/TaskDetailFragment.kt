package com.hixtrik.tasktracker.ui.taskDetail

import android.app.AlertDialog
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
import com.hixtrik.tasktracker.data.task.Task
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
        val task = Observer<Task> { newTask ->
            binding.apply {
                tvService.text = viewModel.service
                tvWorkDesc.text = viewModel.workToDo
                tvStatus.text = viewModel.status
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
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actStart -> {
                if (viewModel.employee == "") {
                    viewModel.startTime = System.currentTimeMillis()
                    viewModel.employee = "Taha Arıcan"
                    viewModel.status = "Yapılıyor"
                    viewModel.onTaskUpdateClick()
                } else {
                    val builder = AlertDialog.Builder(context)
                    builder.setMessage(getString(R.string.dialog))
                    builder.setPositiveButton("Tamam") { dialogInterface, _ ->
                        dialogInterface.dismiss()
                    }
                    builder.create()
                    builder.show()
                }
            }
            R.id.actWait -> {
                viewModel.status = "Beklemede"
                viewModel.onTaskUpdateClick()
            }
            R.id.actFinish -> {
                viewModel.finishTime = System.currentTimeMillis()
                viewModel.status = "Tamamlandı"
                viewModel.onTaskUpdateClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

fun convertSecondsToHMmSs(seconds: Long): String {
    val s = seconds % 60
    val m = seconds / 60 % 60
    val h = seconds / (60 * 60) % 24
    return String.format("%d:%02d:%02d", h, m, s)
}