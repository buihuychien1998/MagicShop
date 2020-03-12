package com.hidero.test.ui.fragments


import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import com.hidero.test.R
import com.hidero.test.base.BaseFragment
import com.hidero.test.customview.SliderAdapterExample
import com.hidero.test.data.SliderItem
import com.smarteist.autoimageslider.IndicatorAnimations
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {
    private lateinit var adapter: SliderAdapterExample
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun initView(view: View) {
        adapter = SliderAdapterExample(this@HomeFragment.context)
        sliderView.sliderAdapter = adapter
        renewItems(null)
        with(sliderView) {
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


    private fun renewItems(view: View?) {
        val sliderItemList = ArrayList<SliderItem>()
        //dummy data
        for (i in 0..4) {
            val sliderItem = SliderItem()

            if (i % 5 == 0) {
                sliderItem.imageUrl =
                    "https://dongho24h.com/upload/1/articles/l_770674346_Untitled-7.jpg"
            } else if (i % 5 == 1) {
                sliderItem.imageUrl =
                    "https://canthoplus.com/wp-content/uploads/2018/12/giay-ha-anh-can-tho-banner.jpg"
            } else if (i % 5 == 2) {
                sliderItem.imageUrl =
                    "https://khuyenmaiviet.vn/wp-content/uploads/2019/11/76793129_2285972111511566_282390569248882688_o.jpg"
            } else if (i % 5 == 3) {
                sliderItem.imageUrl =
                    "https://hoaanhdao.vn/2019_img/files/san-pham/640x340-mobile%20(1).png"
            } else {
                sliderItem.imageUrl =
                    "https://img.kam.vn/images/414x0/ed264b973dda4ee5b852f77e93f724b1/dong-ho-duy-anh-dong-ho-nhat-sale-off-15.jpg"
            }
//            sliderItem.description = "Slider Item Added Manually"
            sliderItem.description = sliderItem.imageUrl
            sliderItemList.add(sliderItem)
        }
        adapter.renewItems(sliderItemList)
    }

    fun removeLastItem(view: View) {
        if (adapter.count - 1 >= 0)
            adapter.deleteItem(adapter.count - 1)
    }

    fun addNewItem(view: View) {
        val sliderItem = SliderItem()
        sliderItem.description = "Slider Item Added Manually"
        sliderItem.imageUrl =
            "https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
        adapter.addItem(sliderItem)
    }


}
