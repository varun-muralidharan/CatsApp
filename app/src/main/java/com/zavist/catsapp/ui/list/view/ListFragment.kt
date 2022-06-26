package com.zavist.catsapp.ui.list.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.zavist.catsapp.R
import com.zavist.catsapp.databinding.FragmentListBinding
import com.zavist.catsapp.ui.list.model.ListViewModel
import com.zavist.catsapp.ui.list.view.adapter.BreedDiffCallbackTemp
import com.zavist.catsapp.ui.list.view.adapter.CatsAdapter
import com.zavist.catsapp.ui.model.FavoriteViewModel
import com.zavist.catsapp.util.CLog
import com.zavist.catsapp.util.navigateSafe
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private lateinit var catsAdapter: CatsAdapter

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: ListViewModel by activityViewModels()
    private val favoriteModel: FavoriteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CLog.d(TAG, "onCreateView", "")
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CLog.d(TAG, "onViewCreated", "")
        activity?.setTitle(R.string.app_name)

        catsAdapter = CatsAdapter(
            diffCallback = BreedDiffCallbackTemp(),
            itemClick = { item, _ ->
                val bundle = bundleOf(
                    "id" to item.id,
                    "fav_id" to item.favoriteId,
                    "title" to item.name
                )
                findNavController().navigateSafe(R.id.action_ListFragment_to_DetailFragment, bundle)
            },
            favoriteClick = { item, position ->
                favoriteModel.modifyFavorite(item)
            }
        )

        with(binding.recyclerView) {
            adapter = catsAdapter
//            layoutManager = LinearLayoutManager(context)
            layoutManager = GridLayoutManager(context, getSpanCount())
            itemAnimator = DefaultItemAnimator().apply {
                supportsChangeAnimations = false
            }

        }

//        val itemadapter = ItemAdapter<Breed,SampleViewHolder>(
//            BreedDiffCallback(),
//            {_,_ -> },
//            {_,_ -> },
//            { parent -> SampleViewHolder.from(parent) }
//        )

        setupViewModel()
        viewModel.load()
    }

    private fun getSpanCount(): Int = resources.getInteger(R.integer.span_count)

    private fun setupViewModel() {
        favoriteModel.sync()
        with(viewModel) {
            breeds.observe(viewLifecycleOwner) {
                catsAdapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "[CAT]ListFragment"
    }
}