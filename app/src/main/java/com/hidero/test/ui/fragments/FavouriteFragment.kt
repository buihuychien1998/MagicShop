package com.hidero.test.ui.fragments

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.databinding.FragmentFavouriteBinding
import com.hidero.test.ui.adapters.FavoriteAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.FavouriteViewModel

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : BaseFragment<FragmentFavouriteBinding>() {
    private lateinit var viewModel: FavouriteViewModel

    lateinit var adapter: FavoriteAdapter

    override fun getLayoutId() = R.layout.fragment_favourite

    override fun initViews(view: View) {
        viewModel = create()
        binding.handlers = this
        adapter = FavoriteAdapter(viewModel)
        binding.rvFavourite.adapter = adapter
        viewModel.account.value?.username?.let { username ->
            viewModel.select(username).observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }

    }

    fun onClick(view: View) {
        findNavController().navigateUp()
    }

    private fun create(): FavouriteViewModel {
        return ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return FavouriteViewModel(requireActivity().application) as T
            }
        })[FavouriteViewModel::class.java]
    }
}
