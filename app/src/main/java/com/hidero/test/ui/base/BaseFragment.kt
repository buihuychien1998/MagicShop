package com.hidero.test.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.ui.activities.MainActivity


abstract class BaseFragment : Fragment() {
    private var baseActivity: BaseActivity? = null
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layout = inflater.inflate(getLayoutId(), container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        if (findNavController().currentDestination?.id != findNavController().graph.startDestination) {
            (baseActivity as MainActivity).visibilityBottomNav(false)
        } else {
            (baseActivity as MainActivity).visibilityBottomNav(true)
        }

    }

    override fun onDestroyView() {
        if (layout.parent != null) {
            (layout.parent as ViewGroup).removeView(layout)
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
    abstract fun initViews(view: View)


}