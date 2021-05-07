package com.github.gmkornilov.chess_puzzle_book.ui.puzzle

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.databinding.FragmentPuzzleBinding

class PuzzleFragment : Fragment() {
    private lateinit var puzzleViewModel: PuzzleViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentPuzzleBinding>(inflater, R.layout.fragment_puzzle, container, false)

        puzzleViewModel =
            ViewModelProvider(this).get(PuzzleViewModel::class.java)
        binding.viewmodel = puzzleViewModel


        return inflater.inflate(R.layout.fragment_puzzle, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                PuzzleFragment()
    }
}