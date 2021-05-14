package com.magicthegathering.cards.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.magicthegathering.cards.R
import com.magicthegathering.cards.databinding.FragmentSearchBinding
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.presentation.adapters.CardLoadStateAdapter
import com.magicthegathering.cards.presentation.adapters.CardsListAdapter
import com.magicthegathering.cards.presentation.dependency_injection.DependencyInjection
import com.magicthegathering.cards.presentation.utils.onClickKeyboardDoneButton
import com.magicthegathering.cards.presentation.viewmodels.CardsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var _cardsViewModel: CardsViewModel
    private lateinit var _cardListAdapter: CardsListAdapter

    private var _searchJob: Job? = null

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

        val binding: FragmentSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        val clickListener: (MagicCard) -> Unit = {
            _cardsViewModel.setCurrentCard(it)
            this.findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment()
            )
        }
        initAdapter(binding, clickListener)
        initEditText(binding)

        _cardsViewModel.currentQuery.observe(viewLifecycleOwner, { query ->
            startSearch(query)
        })

        return binding.root
    }

    /**
     * Initializes the RecyclerView Adapter and LoadStateListener
     *
     * @param   binding The [FragmentSearchBinding] received
     */
    private fun initAdapter(binding: FragmentSearchBinding, clickListener: (MagicCard) -> Unit) {
        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerViewSearchResults.addItemDecoration(decoration)

        _cardListAdapter = CardsListAdapter(clickListener)
        binding.recyclerViewSearchResults.adapter = _cardListAdapter.withLoadStateHeaderAndFooter(
            header = CardLoadStateAdapter { _cardListAdapter.retry() },
            footer = CardLoadStateAdapter { _cardListAdapter.retry() }
        )
        _cardListAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.recyclerViewSearchResults.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    context,
                    "\uD83D\uDE28 Aw Snap! ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun initEditText(binding: FragmentSearchBinding) {
        val editText = binding.editTextSearch
        editText.onClickKeyboardDoneButton {
            setCurrentQuery(binding)
        }
//        editText.setOnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                binding.editTextSearch.hint = ""
//            } else {
//                binding.editTextSearch.hint = getString(R.string.search_by_name)
//            }
//        }
    }

    /**
     * Starts a search for
     */
    private fun startSearch(query: String) {
        _searchJob?.cancel()
        _searchJob = lifecycleScope.launch {
            _cardsViewModel.searchCards(query).collectLatest {
                _cardListAdapter.submitData(it)
            }
        }
    }

    /**
     * Provides the current query to [CardsViewModel]
     *
     * @param   binding     The [FragmentSearchBinding] received
     */
    private fun setCurrentQuery(binding: FragmentSearchBinding) {
        binding.editTextSearch.clearFocus()

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)

        val query = binding.editTextSearch.text.toString()
        _cardsViewModel.setCurrentQuery(query)
    }

}