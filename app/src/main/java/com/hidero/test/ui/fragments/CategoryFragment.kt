package com.hidero.test.ui.fragments


import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hidero.test.R
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.databinding.FragmentCategoryBinding
import com.hidero.test.ui.adapters.CategoryPagedListAdapter
import com.hidero.test.ui.adapters.OnItemClickListener
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.CategoryViewModel
import com.hidero.test.ui.viewmodels.SharedViewModel
import com.hidero.test.util.OscillatingScrollListener
import com.hidero.test.util.SpringAddItemAnimator
import com.hidero.test.util.smoothScrollToPositionWithSpeed

/**
 * A simple [Fragment] subclass.
 */
class CategoryFragment : BaseFragment<FragmentCategoryBinding>() {
    private lateinit var viewModel: CategoryViewModel
    override fun getLayoutId() = R.layout.fragment_category
    private lateinit var bookAdapter: CategoryPagedListAdapter
    private val shareViewModel: SharedViewModel by activityViewModels()
    override fun initViews(view: View) {
        viewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        binding.handlers = viewModel
        bookAdapter = CategoryPagedListAdapter { viewModel.retry() }.apply {
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    shareViewModel.select(getItemPosition(position))
                    findNavController().navigate(CategoryFragmentDirections.actionCategoryFragmentToDetailProductFragment())
                }
            }

        }
        viewModel.apply {
            fetchAuthor()
            fetchGenre()
//            if (currentPos.value != null) {
//                subscribeUi()
//                (binding.rvCategory.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
//                    currentPos.value!!,
//                    0
//                )
//                currentPos.value = null
//            }
        }
        binding.btnFilter.setOnClickListener {
            it.startAnimation(AnimationUtils.loadAnimation(context, R.anim.button_click))
            viewModel.subscribeUi()
        }
        binding.rvCategory.apply{
            adapter = bookAdapter
            smoothScrollToPositionWithSpeed(bookAdapter.itemCount)
            addOnScrollListener(
                OscillatingScrollListener(resources.getDimensionPixelSize(R.dimen.dp16))
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        viewModel.currentPos.value =
//            (binding.rvCategory.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    private fun CategoryViewModel.subscribeUi() {
        genreId.value = binding.spinnerSpecies.selectedItemPosition
        authorId.value = binding.spinnerAuthor.selectedItemPosition
        books.observe(viewLifecycleOwner, Observer {
            bookAdapter.submitList(it)
        })
        networkState.observe(viewLifecycleOwner, Observer { state ->
            binding.apply {
                progressBar.visibility =
                    if (listIsEmpty() && state == NetworkState.LOADING) View.VISIBLE else View.GONE
                tvError.visibility =
                    if (listIsEmpty() && state == NetworkState.ERROR) View.VISIBLE else View.GONE
            }
            if (!listIsEmpty()) {
                bookAdapter.setNetworkState(state)
            }
        })
    }


}
