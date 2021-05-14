package com.magicthegathering.cards.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.magicthegathering.cards.R
import com.magicthegathering.cards.databinding.FragmentSearchBinding
import com.magicthegathering.cards.presentation.dependency_injection.DependencyInjection
import com.magicthegathering.cards.presentation.viewmodels.CardsViewModel

class SearchFragment : Fragment() {

    private lateinit var _cardsViewModel: CardsViewModel
    private lateinit var _binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.let {
            _cardsViewModel =
                ViewModelProvider(it, DependencyInjection.provideCardsViewModelFactory()).get(
                    CardsViewModel::class.java
                )
        }

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        return _binding.root
    }

}