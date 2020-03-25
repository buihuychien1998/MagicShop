package com.hidero.test.ui.fragments

import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hidero.test.R
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.util.DRAWABLE_END
import com.hidero.test.util.hideKeyBoard
import com.hidero.test.util.showKeyBoard
import kotlinx.android.synthetic.main.fragment_search.*


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_search
    }

    override fun initView(view: View) {

        val adapter =
            ArrayAdapter<String>(context!!, R.layout.item_suggestion, R.id.tvSuggestion, resources.getStringArray(R.array.query_suggestions))
        actvSearch.apply {
            requestFocus()
            activity?.showKeyBoard()
            threshold = 1
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
            addTextChangedListener(object : TextWatcher{
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

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

            })
            setOnEditorActionListener{ _, actionId, event->
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //search Here
                    performSearch()
                }
                false
            }

        }

        btnBack.setOnClickListener {
            findNavController().navigateUp()
        }


    }

    private fun performSearch() {
        actvSearch.clearFocus()
        activity?.hideKeyBoard()
    }
}
