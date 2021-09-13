package com.hixtrik.tasktracker.ui.tasks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hixtrik.tasktracker.R
import com.hixtrik.tasktracker.data.task.Task
import com.hixtrik.tasktracker.databinding.FragmentTasksBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class TasksFragment : Fragment(R.layout.fragment_tasks), TasksAdapter.OnItemClickListener {
    private val viewModel: TasksViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTasksBinding.bind(view)
        val taskAdapter = TasksAdapter(this)
        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it?.toMutableList())
        }
        binding.apply {
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.tasksEvent.collect { event ->
                when (event) {
                    is TasksViewModel.TasksEvent.NavigateToTaskDetail -> {
                        val action =
                            TasksFragmentDirections.actionNavTasksToTaskDetailFragment(task = event.task)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onItemClick(task: Task) {
        viewModel.onTaskSelected(task)
    }
}