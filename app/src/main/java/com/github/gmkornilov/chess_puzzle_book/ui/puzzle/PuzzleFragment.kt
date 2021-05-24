package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.data.model.EloUtils
import com.github.gmkornilov.chess_puzzle_book.databinding.FragmentPuzzleBinding
import com.google.android.material.snackbar.Snackbar

class PuzzleFragment : Fragment() {
    private val args: PuzzleFragmentArgs by navArgs()
    private val puzzleFragmentViewModel: PuzzleFragmentViewModel by viewModels {
        PuzzleFragmentViewModelFactory(args.taskProvider)
    }
    private lateinit var binding: FragmentPuzzleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (EloUtils.elo == null) {
            var elo = 1200
            val sp = activity?.getSharedPreferences("elo", Context.MODE_PRIVATE)
            if (sp != null) {
                elo = sp.getInt("elo", 1200)
            }
            EloUtils.elo = puzzleFragmentViewModel.elo.value ?: elo
        }

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_puzzle,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = puzzleFragmentViewModel

        binding.chessboardView.addBoardListener(puzzleFragmentViewModel)

        puzzleFragmentViewModel.fenLoadedEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                binding.chessboardView.fen = it
            }
        })
        puzzleFragmentViewModel.undoEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                binding.chessboardView.undo()
            }
        })
        puzzleFragmentViewModel.turnEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                binding.chessboardView.lastMove = it
            }
        })
        puzzleFragmentViewModel.exceptionEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(requireView(), "Ошибка интернет-соединения", Snackbar.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }

    override fun onResume() {
        val vmFen = puzzleFragmentViewModel.currentFen.value
        if (vmFen != null) {
            binding.chessboardView.fen = vmFen
        }
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        val elo = EloUtils.elo!!
        val sp = activity?.getSharedPreferences("elo", Context.MODE_PRIVATE) ?: return

        val editor = sp.edit()
        editor.putInt("elo", elo)
        editor.apply()
    }
}