package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.animation.*
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
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
                val flipOutAnimation = AnimatorInflater.loadAnimator(context, R.animator.flip_out)
                flipOutAnimation.setTarget(binding.chessboardView)
                flipOutAnimation.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        binding.chessboardView.fen = it
                    }

                    override fun onAnimationStart(animation: Animator?) = Unit
                    override fun onAnimationCancel(animation: Animator?) = Unit
                    override fun onAnimationRepeat(animation: Animator?) = Unit
                })

                val flipInAnimation = AnimatorInflater.loadAnimator(context, R.animator.flip_in)
                flipInAnimation.setTarget(binding.chessboardView)

                AnimatorSet().apply {
                    play(flipOutAnimation).before(flipInAnimation)
                    start()
                }
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