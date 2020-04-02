package com.hidero.test.ui.fragments


import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hidero.test.R
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.ui.adapters.BookPagedListAdapter
import com.hidero.test.ui.adapters.SliderAdapterExample
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.HomeViewModel
import com.hidero.test.ui.viewmodels.SharedViewModel
import com.hidero.test.util.SPAN_COUNT
import com.hidero.test.util.renewItems
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {
    private lateinit var adapter: SliderAdapterExample

    private lateinit var viewModel: HomeViewModel

    private val shareViewModel: SharedViewModel by activityViewModels()
    private lateinit var bookAdapter: BookPagedListAdapter

    override fun getLayoutId() = R.layout.fragment_home

    override fun initViews(view: View) {
        setUpSlider()
        btnSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
        viewModel = create()
        subscribeUi()
        initAdapter()

    }


    private fun initAdapter() {
        bookAdapter = BookPagedListAdapter { viewModel.retry() }

        bookAdapter.onItemClickListener = object : BookPagedListAdapter.OnItemClickListener {
            override fun onItemClick(book: Book, position: Int) {
                shareViewModel.select(book)
                findNavController().navigate(R.id.action_homeFragment_to_detailProductFragment)
            }
        }
        val gridLayoutManager = GridLayoutManager(context, SPAN_COUNT)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = bookAdapter.getItemViewType(position)
                return if (viewType == bookAdapter.DATA_VIEW_TYPE) 1    // DATA_VIEW_TYPE will occupy 1 out of 3 span
                else 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        }
        with(rvProduct) {
            adapter = bookAdapter
            layoutManager = gridLayoutManager
        }

        txt_error.setOnClickListener { viewModel.retry() }

    }

    private fun subscribeUi() {
        viewModel.bookList.observe(viewLifecycleOwner, Observer {
            bookAdapter.submitList(it)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer { state ->
            progress_bar.visibility =
                if (viewModel.listIsEmpty() && state == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility =
                if (viewModel.listIsEmpty() && state == NetworkState.ERROR) View.VISIBLE else View.GONE
            if (!viewModel.listIsEmpty()) {
                bookAdapter.setNetworkState(state)
            }
        })
    }

    private fun setUpSlider() {
        adapter =
            SliderAdapterExample(this@HomeFragment.context)
        sliderView.sliderAdapter = adapter
        renewItems(adapter)
        sliderView.apply {
            setIndicatorAnimation(IndicatorAnimations.FILL) //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            setSliderTransformAnimation(SliderAnimations.CUBEINROTATIONTRANSFORMATION)
            autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
            indicatorSelectedColor = Color.WHITE
            indicatorUnselectedColor = Color.GRAY
            scrollTimeInSec = 3
            isAutoCycle = true
            startAutoCycle()
        }
    }



    private fun create(): HomeViewModel {
        return ViewModelProvider(viewModelStore, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel() as T
            }
        })[HomeViewModel::class.java]
    }

}


