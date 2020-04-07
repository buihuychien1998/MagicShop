package com.hidero.test.ui.fragments

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hidero.test.R
import com.hidero.test.databinding.FragmentSearchBinding
import com.hidero.test.ui.adapters.BookPagedListAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.SearchViewModel
import com.hidero.test.ui.viewmodels.SharedViewModel
import com.hidero.test.util.*
import kotlinx.android.synthetic.main.fragment_search.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    private val viewModel by lazy {
        create()
    }

    private lateinit var bookAdapter: BookPagedListAdapter
    private val shareViewModel: SharedViewModel by activityViewModels()

    override fun getLayoutId() = R.layout.fragment_search

    override fun initViews(view: View) {
        initAdapter()
        val adapter =
            ArrayAdapter<String>(
                requireContext(), R.layout.item_suggestion, R.id.tvSuggestion,
                resources.getStringArray(R.array.query_suggestions)
            )
        binding.actvSearch.apply {
            requestFocus()
            requireActivity().showKeyBoard()
            threshold = 0
            setAdapter(adapter)
            setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP && compoundDrawables[DRAWABLE_END] != null) {
                    if (motionEvent.rawX >= (right - compoundDrawables[DRAWABLE_END].bounds.width())) {
                        // your action here
                        setText("")
                    }
                }
                false
            }
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(editable: Editable?) {
                    if (editable?.length == 0 || editable == null) {
                        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
                    } else {
                        setCompoundDrawablesWithIntrinsicBounds(
                            null,
                            null,
                            ContextCompat.getDrawable(context, R.drawable.ic_clear),
                            null
                        )
                    }
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //search Here
                    performSearch(text.toString())
                }
                false
            }

        }
        binding.handlers = this
        if (viewModel.currentPos.value != null) {
            subscribeUi()
            (binding.rvSearch.layoutManager as GridLayoutManager).scrollToPositionWithOffset(
                viewModel.currentPos.value!!,
                0
            )
            viewModel.currentPos.value = null
        }
    }

    fun bindingEvent(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                findNavController().navigateUp()
            }
            R.id.btnSearch -> {
                performSearch(actvSearch.text.toString())
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.currentPos.value =
            (binding.rvSearch.layoutManager as GridLayoutManager).findFirstCompletelyVisibleItemPosition()
    }

    private fun initAdapter() {
        bookAdapter = BookPagedListAdapter { viewModel.retry() }.apply {
            onItemClickListener = object : BookPagedListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    shareViewModel.select(getItemPosition(position))
                    findNavController().navigate(R.id.action_searchFragment_to_detailProductFragment)
                }

            }
        }
        val gridLayoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = bookAdapter.getItemViewType(position)
                    return if (viewType == bookAdapter.DATA_VIEW_TYPE) 1    // DATA_VIEW_TYPE will occupy 1 out of 3 span
                    else 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
                }
            }
        }
        gridLayoutManager.onSaveInstanceState()
        with(rvSearch) {
            layoutManager = gridLayoutManager
            adapter = bookAdapter
        }
    }


    private fun subscribeUi() {
        viewModel.run {
            bookList.observe(this@SearchFragment, Observer {
                bookAdapter.submitList(it)
            })

            networkState.observe(this@SearchFragment, Observer { state ->
                if (!listIsEmpty()) {
                    bookAdapter.setNetworkState(state)
                }
            })
        }
    }

    private fun performSearch(input: String) {
        if (TextUtils.isEmpty(input)) {
            requireContext().showToast(resources.getString(R.string.empty_actv))
        } else {
            viewModel.keyword.value = input
            subscribeUi()
            binding.rvSearch.scrollToPosition(0)
            binding.rvSearch.smoothScrollToPosition(0)
            actvSearch.clearFocus()
            requireActivity().hideKeyBoard()
        }
    }


    private fun create(): SearchViewModel {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel() as T
            }
        })[SearchViewModel::class.java]
    }
}
