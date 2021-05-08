package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.databinding.FragmentPuzzleBinding

class PuzzleFragment : Fragment() {
    val args: PuzzleFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentPuzzleBinding>(inflater, R.layout.fragment_puzzle, container, false)

        val puzzleViewModel : PuzzleViewModel by viewModels {
            PuzzleFragmentViewModelFactory(args.taskProvider)
        }
        binding.viewmodel = puzzleViewModel

        if (puzzleViewModel.task.value == null) {
            puzzleViewModel.getTask()
        }

        return inflater.inflate(R.layout.fragment_puzzle, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                PuzzleFragment()
    }
}