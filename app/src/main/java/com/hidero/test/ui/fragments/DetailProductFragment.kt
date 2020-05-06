package com.hidero.test.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import com.hidero.test.R
import com.hidero.test.data.valueobject.Book
import com.hidero.test.data.valueobject.FavouriteBook
import com.hidero.test.databinding.FragmentDetailProductBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.CartViewModel
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.ui.viewmodels.FavouriteViewModel
import com.hidero.test.ui.viewmodels.SharedViewModel
import com.hidero.test.util.createShareIntent
import com.hidero.test.util.showToast

/**
 * A simple [Fragment] subclass.
 */
class DetailProductFragment : BaseFragment<FragmentDetailProductBinding>() {
    private val shareViewModel: SharedViewModel by activityViewModels()
    override fun getLayoutId() = R.layout.fragment_detail_product
    private val viewModel by lazy { ViewModelProvider(this)[CartViewModel::class.java] }
    private val favouriteViewModel by lazy { create() }
    private lateinit var book: Book
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun initViews(view: View) {
        binding.handlers = viewModel
        binding.viewModel = favouriteViewModel
        viewModel.fetchCart(viewModel.account.value?.username)
        shareViewModel.selected.observe(viewLifecycleOwner) {
            binding.data = it
            book = it
        }
        subscribeUi()

    }

    private fun subscribeUi() {
        viewModel.navigateTo.observe(viewLifecycleOwner, EventObserver {
            handleEvent(it)
        })

        favouriteViewModel.navigateTo.observe(viewLifecycleOwner, EventObserver {
            handleEvent(it)
        })
        viewModel.insertResponse.observe(viewLifecycleOwner, Observer {
            if (it == "Success") {
                requireContext().showToast("Thêm vào giỏ hàng thành công")
            } else {
                requireContext().showToast("Bạn chưa đăng nhập!")
            }
        })
    }

    fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnBack -> {
                findNavController().navigateUp()
            }

            R.id.btnAddCart -> {
                if (viewModel.account.value?.username == null) {
                    requireContext().showToast("Bạn phải đăng nhập để mua sản phẩm này")
                } else {
                    viewModel.apply {
                        insertCart(account.value?.username, book.bookId, 1, book.cost)
                        fetchCart(account.value?.username)
                    }
                }
            }
            R.id.btnFavorite -> {
                if (favouriteViewModel.account.value?.username != null) {
                    val tmp = FavouriteBook(
                        book.bookId,
                        book.bookName,
                        book.bookImage,
                        book.author,
                        favouriteViewModel.account.value?.username
                    )
                    favouriteViewModel.insert(tmp)
                    Snackbar.make(
                        binding.root,
                        "Đã thêm vào danh sách yêu thích",
                        Snackbar.LENGTH_SHORT
                    ).show()
                } else {
                    requireContext().showToast("Bạn chưa đăng nhập")
                }

            }

            R.id.btnShare -> {
                requireActivity().createShareIntent(book.bookName)
            }

        }

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
