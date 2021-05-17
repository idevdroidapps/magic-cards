package com.magicthegathering.cards.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magicthegathering.cards.R
import com.magicthegathering.cards.databinding.FragmentDetailsBinding
import com.magicthegathering.cards.presentation.viewmodels.CardsViewModel

class DetailsFragment : BottomSheetDialogFragment() {

    private val _cardsViewModel: CardsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding: FragmentDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        binding.card = _cardsViewModel.currentCard.value
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}