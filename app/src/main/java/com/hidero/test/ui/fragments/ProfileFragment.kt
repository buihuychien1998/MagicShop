package com.hidero.test.ui.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.util.showToast
import kotlinx.android.synthetic.main.fragment_profile.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolBar.inflateMenu(R.menu.profile_menu)
        toolBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        btnBack.setOnClickListener {
            context?.showToast("Back")
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_home -> {

            }
            R.id.menu_message -> {

            }
            R.id.menu_account -> {

            }
            R.id.menu_help -> {

            }
            R.id.menu_feed_back -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }

}
