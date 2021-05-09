package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.databinding.FragmentPuzzleBinding

class PuzzleFragment : Fragment() {
    val args: PuzzleFragmentArgs by navArgs()
    private lateinit var puzzleViewModel: PuzzleViewModel
    private lateinit var binding: FragmentPuzzleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_puzzle,
            container,
            false
        )

        val viewModel: PuzzleViewModel by viewModels {
            PuzzleFragmentViewModelFactory(args.taskProvider)
        }
        puzzleViewModel = viewModel

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = puzzleViewModel

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PuzzleFragment()
    }
}