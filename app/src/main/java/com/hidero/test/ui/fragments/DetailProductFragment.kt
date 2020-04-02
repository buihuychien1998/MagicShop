package com.hidero.test.ui.fragments


import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail_product.*

/**
 * A simple [Fragment] subclass.
 */
class DetailProductFragment : BaseFragment() {
    private val shareViewModel: SharedViewModel by activityViewModels()
    override fun getLayoutId(): Int {
        return R.layout.fragment_detail_product
    }

    @SuppressLint("SetTextI18n")
    override fun initViews(view: View) {
//        val book = arguments?.getInt("bookId")
        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        shareViewModel.selected.observe(viewLifecycleOwner) {
            it.apply {
                tvNameProduct.text = bookName
                tvCost.text = "$cost đ"
                Glide.with(requireContext()).load(bookImage).placeholder(R.drawable.ic_no_image)
                    .error(R.drawable.ic_error).into(imageSlider)
                tvSpecification.text = description
            }

        }
    }
}
