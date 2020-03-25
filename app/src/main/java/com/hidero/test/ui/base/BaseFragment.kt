package com.hidero.test.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.activities.MainActivity


abstract class BaseFragment : Fragment() {
    private var mActivity: BaseActivity? = null
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as BaseActivity
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(getLayoutId(), container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(mView)
        if (findNavController().currentDestination?.id == R.id.detailProductFragment ||
            findNavController().currentDestination?.id == R.id.searchFragment ||
            findNavController().currentDestination?.id == R.id.settingFragment ||
            findNavController().currentDestination?.id == R.id.profileFragment ||
            findNavController().currentDestination?.id == R.id.checkoutFragment
        ) {
            (mActivity as MainActivity).visibilityBottomNav(false)
        } else {
            (mActivity as MainActivity).visibilityBottomNav(true)
        }

    }

    override fun onDestroyView() {
        if (mView.parent != null) {
            (mView.parent as ViewGroup).removeView(mView)
        }
        super.onDestroyView()
    }

    /**
     * get layout id of activity
     *
     * @return layout id
     */
    abstract fun getLayoutId(): Int

    /**
     * create view
     *
     * @param view
     */
    abstract fun initView(view: View)

}