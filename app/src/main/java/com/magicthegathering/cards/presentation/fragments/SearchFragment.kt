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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.magicthegathering.cards.R
import com.magicthegathering.cards.data.db.ClearCacheWorker
import com.magicthegathering.cards.databinding.FragmentSearchBinding
import com.magicthegathering.cards.domain.entities.MagicCard
import com.magicthegathering.cards.presentation.adapters.CardLoadStateAdapter
import com.magicthegathering.cards.presentation.adapters.CardsListAdapter
import com.magicthegathering.cards.presentation.utils.onClickKeyboardDoneButton
import com.magicthegathering.cards.presentation.viewmodels.CardsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {

    private val _cardsViewModel: CardsViewModel by activityViewModels()
    private lateinit var _cardListAdapter: CardsListAdapter
    private lateinit var _binding: FragmentSearchBinding

    private var _searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        initAdapter()
        initEditText()

        _binding.retryButton.setOnClickListener {
            val query = _binding.editTextSearch.text?.trim().toString()
            startSearch(query)
        }

        _cardsViewModel.currentQuery.observe(viewLifecycleOwner, { query ->
            startSearch(query)
        })

        _cardsViewModel.startClearCacheWorker.observe(viewLifecycleOwner, { start ->
            if (start) {
                _cardsViewModel.setClearCacheWorker(false)
                activity?.let {
                    val oneTimeWorkRequest = OneTimeWorkRequestBuilder<ClearCacheWorker>()
                        .setInitialDelay(24, TimeUnit.HOURS)
                        .build()
                    WorkManager.getInstance(it.applicationContext).enqueue(oneTimeWorkRequest)
                }
            }
        })

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            val sharedPref = it.getPreferences(Context.MODE_PRIVATE)
            val query = sharedPref.getString(LAST_SEARCH_QUERY, null)
            _binding.editTextSearch.setText(query)
            _cardsViewModel.setCurrentQuery(query)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activity?.let {
            val sharedPref = it.getPreferences(Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                putString(LAST_SEARCH_QUERY, _cardsViewModel.currentQuery.value?.trim().toString())
                commit()
            }
        }
    }

    /**
     * Initializes the RecyclerView Adapter and LoadStateListener
     *
     */
    private fun initAdapter() {

        val clickListener: (MagicCard) -> Unit = {
            _cardsViewModel.setCurrentCard(it)
            this.findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToDetailsFragment()
            )
        }
        _cardListAdapter = CardsListAdapter(clickListener)

        val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        _binding.recyclerViewSearchResults.addItemDecoration(decoration)
        _binding.recyclerViewSearchResults.adapter = _cardListAdapter.withLoadStateHeaderAndFooter(
            header = CardLoadStateAdapter { _cardListAdapter.retry() },
            footer = CardLoadStateAdapter { _cardListAdapter.retry() }
        )

        _cardListAdapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            _binding.recyclerViewSearchResults.isVisible =
                loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            _binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            _binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

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

    private fun initEditText() {
        val editText = _binding.editTextSearch
        editText.onClickKeyboardDoneButton {
            setCurrentQuery()
        }
    }

    /**
     * Starts a search for
     */
    private fun startSearch(query: String?) {
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
     */
    private fun setCurrentQuery() {
        _binding.editTextSearch.clearFocus()

        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(_binding.root.windowToken, 0)

        val query = _binding.editTextSearch.text.toString()
        _cardsViewModel.setCurrentQuery(query)
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
    }

}