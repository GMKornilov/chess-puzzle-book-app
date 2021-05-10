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
import com.google.android.material.snackbar.Snackbar

class PuzzleFragment : Fragment() {
    private val args: PuzzleFragmentArgs by navArgs()
    private val puzzleViewModel: PuzzleViewModel by viewModels {
        PuzzleFragmentViewModelFactory(args.taskProvider)
    }
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = puzzleViewModel

        binding.chessboardView.addBoardListener(puzzleViewModel)

        puzzleViewModel.undoEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                binding.chessboardView.undo()
            }
        })
        puzzleViewModel.turnEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                binding.chessboardView.lastMove = it
            }
        })
        puzzleViewModel.exceptionEvent.observe(viewLifecycleOwner, {event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PuzzleFragment()
    }
}