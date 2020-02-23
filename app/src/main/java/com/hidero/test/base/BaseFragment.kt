package com.hidero.test.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.hidero.test.ui.activities.MainActivity


abstract class BaseFragment: Fragment() {
    private var mActivity: BaseActivity? = null
    private var mView: View? = null

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
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false)
        }
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(mView!!)

    }
    override fun onDestroyView() {
        if (mView!!.parent != null) {
            (mView!!.parent as ViewGroup).removeView(mView)
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