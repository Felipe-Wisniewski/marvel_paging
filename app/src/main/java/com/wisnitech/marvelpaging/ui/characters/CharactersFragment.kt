package com.wisnitech.marvelpaging.ui.characters

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.wisnitech.marvelpaging.R
import com.wisnitech.marvelpaging.databinding.FragmentCharactersBinding
import com.wisnitech.marvelpaging.model.CharacterWeb
import com.wisnitech.marvelpaging.repository.service.Status
import com.wisnitech.marvelpaging.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_characters.*
import org.koin.android.viewmodel.ext.android.viewModel

class CharactersFragment : Fragment() {

    private val viewModel: CharactersViewModel by viewModel()
    private lateinit var binding: FragmentCharactersBinding
    private val charactersAdapter: CharactersAdapter by lazy { CharactersAdapter(this::onItemClick) }
    private var querySearch = ""
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersBinding.inflate(inflater).apply {
            lifecycleOwner = viewLifecycleOwner
            homeViewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        coordinateMotion()
        searchCharacterFrag()
        loadCharacters()
        observeShowBalance()
        setupSwipeRefresh()
        setupSearch()
    }

    private fun coordinateMotion() {
        val listener = AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val seekPosition = -verticalOffset / binding.appbarLayout.totalScrollRange.toFloat()

            motion_layout_home_toolbar.progress = seekPosition
            //set appbar background color
        }

        appbar_layout.addOnOffsetChangedListener(listener)
    }

    private fun loadCharacters() {
        rv_home_operations.run {
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            itemAnimator = DefaultItemAnimator()
            adapter = charactersAdapter
        }

        viewModel.charactersStatus.observe(viewLifecycleOwner, Observer {
            swipe_refresh_operations.isRefreshing = it == Status.LOADING
        })
    }

    private fun searchCharacterFrag() {
        viewModel.searchCharacters(querySearch).observe(viewLifecycleOwner, Observer {
            charactersAdapter.submitList(it)
        })
    }

    private fun observeShowBalance() {
        viewModel.showBalance.observe(viewLifecycleOwner, Observer {
            val icon = ContextCompat.getDrawable(
                requireContext(),
                if (it) R.drawable.ic_eye_show_enabled else R.drawable.ic_eye_hide_enabled
            )
            btn_hide_balance.setImageDrawable(icon)
        })

        btn_hide_balance.setOnClickListener { viewModel.showBalance() }
    }

    private fun onItemClick(character: CharacterWeb) {
        Toast.makeText(requireContext(), "Character: ${character.name}", Toast.LENGTH_SHORT).show()
    }

    private fun setupSwipeRefresh() {
        swipe_refresh_operations.setOnRefreshListener {
            searchCharacterFrag()
        }
    }

    private fun setupSearch() {
        search_operations_label.setOnClickListener {
            viewModel.isSearching(true)

            search_view_input.apply {
                performClick()
                requestFocus()
            }
        }

        search_view_input.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                querySearch = it.toString()
                searchCharacterFrag()
            }
        }

        search_view.setEndIconOnClickListener {
            search_view_input.setText("")

            if (querySearch != "") {
                querySearch = ""
                searchCharacterFrag()
            }

            viewModel.isSearching(false)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setStatusBarColor(
            ContextCompat.getColor(
                requireActivity(),
                R.color.background_card_toolbar
            )
        )
        viewModel.getBalance()
    }

    companion object {
        fun newInstance() = CharactersFragment()
    }
}