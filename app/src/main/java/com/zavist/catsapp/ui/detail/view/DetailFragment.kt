package com.zavist.catsapp.ui.detail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.zavist.catsapp.R
import com.zavist.catsapp.databinding.FragmentDetailBinding
import com.zavist.catsapp.ui.detail.model.DetailViewModel
import com.zavist.catsapp.ui.detail.view.adapter.DetailAdapter
import com.zavist.catsapp.ui.detail.view.adapter.DetailDiffCallback
import com.zavist.catsapp.ui.model.FavoriteViewModel
import com.zavist.catsapp.util.CLog
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    lateinit var detailAdapter: DetailAdapter

    private val viewModel: DetailViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CLog.d(TAG, "onCreateView", "")
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CLog.d(TAG, "onViewCreated", "")
        arguments?.getString("title")?.let {
            activity?.setTitle(it)
        }
        setupViewModel()

        detailAdapter = DetailAdapter(
            DetailDiffCallback(),
            favoriteClick = { item, _ -> favoriteViewModel.modifyFavorite(item) }
        )

        with(binding.recyclerView) {
            adapter = detailAdapter
            layoutManager = GridLayoutManager(context, getSpanCount())
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }
        }
    }

    private fun setupViewModel() {
        with(viewModel) {
            breed.observe(viewLifecycleOwner) { breed ->
                detailAdapter.submitList(breed)
            }
            load(arguments?.getString("id"), arguments?.getInt("fav_id"))
        }

    }

    private fun getSpanCount(): Int = resources.getInteger(R.integer.span_count)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "[CAT]DetailFragment"
    }
}