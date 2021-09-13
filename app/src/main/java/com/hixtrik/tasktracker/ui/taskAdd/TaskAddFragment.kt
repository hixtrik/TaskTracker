package com.hixtrik.tasktracker.ui.taskAdd

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hixtrik.tasktracker.R
import com.hixtrik.tasktracker.databinding.FragmentTaskAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskAddFragment : Fragment(R.layout.fragment_task_add), AdapterView.OnItemSelectedListener {
    private val viewModel: TaskAddViewModel by viewModels()
    private lateinit var binding: FragmentTaskAddBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTaskAddBinding.bind(view)
        binding.apply {
            edtRoomNo.addTextChangedListener {
                viewModel.roomNo = it.toString()
            }
            edtDescription.addTextChangedListener {
                viewModel.description = it.toString()
            }
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.service_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnService.adapter = adapter
                spnService.onItemSelectedListener = this@TaskAddFragment
            }
            btnTaskAdd.setOnClickListener {
                viewModel.service = spnService.selectedItem.toString()
                viewModel.workToDo = spnWorkToDo.selectedItem.toString()
                viewModel.onTaskAddClick()
                findNavController().popBackStack()
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        ArrayAdapter.createFromResource(
            requireContext(),
            when (p2) {
                0 -> {
                    R.array.cleaning_array
                }
                1 -> {
                    R.array.transport_array
                }
                2 -> {
                    R.array.building_land_array
                }
                else -> R.array.cleaning_array
            },
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spnWorkToDo.adapter = adapter
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}