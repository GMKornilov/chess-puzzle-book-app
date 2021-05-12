package com.github.gmkornilov.chess_puzzle_book.ui.puzzle_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.github.gmkornilov.chess_puzzle_book.R

class TaskListFragment : Fragment() {
    private val args: TaskListFragmentArgs by navArgs()

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyTaskRecyclerViewAdapter(args.tasks.tasks, ::callback)
            }
        }
        return view
    }

    private fun callback(index: Int) {
        Log.d("Select", args.tasks.tasks[index].toString())
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
    }
}