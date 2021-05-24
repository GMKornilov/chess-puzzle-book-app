package com.github.gmkornilov.chess_puzzle_book.ui.puzzle_list

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.data.providers.ListTaskProvider

class PuzzleListFragment : Fragment() {
    private val args: PuzzleListFragmentArgs by navArgs()

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
        val view = inflater.inflate(R.layout.fragment_puzzle_list_list, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.list)
        val noTasksTextView = view.findViewById<TextView>(R.id.noTaskText)

        if (args.tasks.tasks.isEmpty()) {
            recyclerView.visibility = View.GONE
            noTasksTextView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            noTasksTextView.visibility = View.GONE
        }
        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MyTaskRecyclerViewAdapter(args.tasks.tasks, ::callback)
        }
        return view
    }

    private fun callback(index: Int) {
        Log.d("Select", args.tasks.tasks[index].toString())
        val provider = ListTaskProvider(args.tasks.tasks, index)
        val bundle = Bundle()
        bundle.putParcelable("taskProvider", provider)
        findNavController().navigate(R.id.nav_generated_puzzle, bundle)
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
    }
}