package com.github.gmkornilov.chess_puzzle_book.ui.info

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.gmkornilov.chess_puzzle_book.R
import com.github.gmkornilov.chess_puzzle_book.data.api.LoadedTasks
import com.github.gmkornilov.chess_puzzle_book.databinding.FragmentInfoBinding
import com.google.android.material.snackbar.Snackbar

class InfoFragment : Fragment() {
    private val infoFragmentViewModel: InfoFragmentViewModel by viewModels {
        InfoFragmentViewModelFactory(resources)
    }
    private lateinit var binding: FragmentInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_info,
            container,
            false
        )

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = infoFragmentViewModel

        binding.lastGames.minValue = 1
        binding.lastGames.maxValue = 50
        binding.lastGames.value = 1
        binding.lastGames.wrapSelectorWheel = false

        infoFragmentViewModel.loadedEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                if (it.error.isNotEmpty()) {
                    if (it.error.contains("doesn't exist on lichess")) {
                        Snackbar.make(requireView(), "Данного пользователя не существует", Snackbar.LENGTH_SHORT).show()
                    } else {
                        Snackbar.make(requireView(), it.error, Snackbar.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("Loaded tasks", it.result.toString())
                    val loadedTasks = LoadedTasks(it.result)
                    val bundle = Bundle()
                    bundle.putParcelable("tasks", loadedTasks)
                    findNavController().navigate(R.id.nav_task_list, bundle)
                }
            }
        })
        infoFragmentViewModel.exceptionEvent.observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let {
                Snackbar.make(requireView(), it.message!!, Snackbar.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}