package com.hidero.test.ui.fragments


import android.app.Dialog
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hidero.test.R
import com.hidero.test.data.valueobject.Account
import com.hidero.test.databinding.FragmentCartBinding
import com.hidero.test.ui.adapters.CartAdapter
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.CartViewModel
import com.hidero.test.ui.viewmodels.EventObserver
import com.hidero.test.util.CURRENT_USER
import com.hidero.test.util.SharedPrefs
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class CartFragment : BaseFragment<FragmentCartBinding>() {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    override fun getLayoutId() = R.layout.fragment_cart


    override fun initViews(view: View) {
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        cartAdapter = CartAdapter(viewModel)
        binding.rvCart.adapter = cartAdapter
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvCart)
        binding.handlers = viewModel
        subscribeUi()
    }

    private fun subscribeUi() {
        try {
            viewModel.run {
                fetchCart(SharedPrefs.instance[CURRENT_USER, Account::class.java]?.username)
                cart.observe(viewLifecycleOwner, Observer {
                    cartAdapter.submitList(it)
                })
                navigateTo.observe(viewLifecycleOwner, EventObserver {
                    handleEvent(it)
                })
            }
        } catch (ex: Exception) {
            Timber.e(ex)
        }


    }

    private fun handleEvent(view: View) {
        when (view.id) {
            R.id.btnCheckout -> {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            }
            R.id.btnTrash -> {
                viewModel.deleteAllCart(viewModel.account.value?.username)
            }
        }
    }

    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                try {
                    AlertDialog.Builder(requireContext()).create().apply {
                        setTitle("?")
                        setMessage("Bạn muốn xóa sản phẩm này?")
                        setButton(Dialog.BUTTON_POSITIVE, "Có") { _, _ ->
                            run {
                                cartAdapter.removeItem(viewHolder.adapterPosition)
                            }
                        }
                        setButton(Dialog.BUTTON_NEGATIVE, "Kó") { _, _ ->
                            run {
                                cartAdapter.notifyItemChanged(viewHolder.adapterPosition)
                                dismiss()
                            }
                        }
                        show()
                    }

                } catch (ex: Exception) {
                    Timber.e(ex)
                }

            }

        }


}
