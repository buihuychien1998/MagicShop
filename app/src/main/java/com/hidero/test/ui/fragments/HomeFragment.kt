package com.hidero.test.ui.fragments


import android.graphics.Color
import android.view.View
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.hidero.test.R
import com.hidero.test.data.valueobject.NetworkState
import com.hidero.test.databinding.FragmentHomeBinding
import com.hidero.test.ui.adapters.BookPagedListAdapter
import com.hidero.test.ui.adapters.OnItemClickListener
import com.hidero.test.ui.adapters.SliderAdapterExample
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.HomeViewModel
import com.hidero.test.ui.viewmodels.SharedViewModel
import com.hidero.test.util.*
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var adapter: SliderAdapterExample
    private lateinit var viewModel: HomeViewModel
    private val shareViewModel: SharedViewModel by activityViewModels()
    private lateinit var bookAdapter: BookPagedListAdapter

    override fun getLayoutId() = R.layout.fragment_home

    override fun initViews(view: View) {
        setUpSlider()
        viewModel = create()
        binding.handlers = viewModel
        subscribeUi()
        initAdapter()

    }


    private fun initAdapter() {
        bookAdapter = BookPagedListAdapter { viewModel.retry() }.apply {
            onItemClickListener = object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    shareViewModel.select(getItemPosition(position))
                    findNavController().navigate(R.id.action_homeFragment_to_detailProductFragment)
                }
            }

        }
        val gridLayoutManager = GridLayoutManager(context, SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val viewType = bookAdapter.getItemViewType(position)
                    return if (viewType == DATA_VIEW_TYPE) 1    // DATA_VIEW_TYPE will occupy 1 out of 3 span
                    else 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
                }
            }
        }
        with(rvProduct) {
            adapter = bookAdapter
            layoutManager = gridLayoutManager
        }
    }


    private fun subscribeUi() {
        viewModel.run {
            bookList.observe(viewLifecycleOwner, Observer { data ->
                Timer().schedule(object : TimerTask() {
                    override fun run() { // this code will be executed after 2 seconds
                        requireActivity().runOnUiThread {
                            bookAdapter.submitList(data)
                        }
                    }
                }, DELAY_LOAD)

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

            navigateTo.observe(viewLifecycleOwner, EventObserver { view ->
                handleEvents(view.id)
            })
        }

    }

    private fun handleEvents(id: Int) {
        when (id) {
            R.id.btnSearch -> {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
            }
        }
    }

    private fun setUpSlider() {
        adapter =
            SliderAdapterExample(requireContext())
        binding.sliderView.sliderAdapter = adapter
        renewItems(adapter)
        binding.sliderView.apply {
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


