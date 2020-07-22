package com.hidero.test.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.ui.activities.MainActivity


abstract class BaseFragment<DB: ViewDataBinding> : Fragment() {
    lateinit var binding: DB
    var baseActivity: BaseActivity<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity<*>
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Specify the fragment view as the lifecycle owner of the binding.
        // This is used so that the binding can observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner
        initViews(view)
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

    override fun onResume() {
        super.onResume()
        if (findNavController().currentDestination?.id != findNavController().graph.startDestination) {
            (baseActivity as MainActivity).visibilityBottomNav(false)
        } else {
            (baseActivity as MainActivity).visibilityBottomNav(true)
        }
    }


}