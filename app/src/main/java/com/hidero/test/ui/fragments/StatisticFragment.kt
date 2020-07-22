package com.hidero.test.ui.fragments

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hidero.test.R
import com.hidero.test.databinding.FragmentStatisticBinding
import com.hidero.test.ui.base.BaseFragment
import com.hidero.test.ui.viewmodels.StatisticViewModel

class StatisticFragment : BaseFragment<FragmentStatisticBinding>() {
    private var viewModel: StatisticViewModel? = null
    var tfLight: Typeface? = null
    protected val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    )
    override fun getLayoutId() = R.layout.fragment_statistic

    override fun initViews(view: View) {
        setHasOptionsMenu(true)
        (baseActivity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setTitle("Doanh thu")
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tfLight = Typeface.createFromAsset(requireActivity().getAssets(), "OpenSans-Light.ttf");
        viewModel = ViewModelProvider(this)[StatisticViewModel::class.java]
        viewModel?.statistic?.observe(viewLifecycleOwner, Observer {
            if (it.size == 0){
//                val data = CombinedData()
//                data.setData(viewModel?.generateBarData())
//                data.setData(viewModel?.generateLineData(true))
//                binding.chart.data = data
//                binding.chart.invalidate()
                binding.chart.visibility = View.GONE
                binding.pbLoading.visibility = View.VISIBLE

            }else{
                val data = CombinedData()
                data.setData(viewModel?.generateBarData())
                data.setData(viewModel?.generateLineData(false))
                binding.chart.data = data
                binding.chart.invalidate()
                binding.chart.visibility = View.VISIBLE
                binding.pbLoading.visibility = View.GONE
            }

        })
        viewModel?.getStatistic(2020)
        binding.chart.apply {
            getDescription().setEnabled(false)
            setBackgroundColor(Color.WHITE)
            setDrawGridBackground(false)
            setDrawBarShadow(false)
            setHighlightFullBarEnabled(false)
            setDrawOrder(
                arrayOf(
                    CombinedChart.DrawOrder.BAR,
                    CombinedChart.DrawOrder.LINE
                )
            )
            legend.setWordWrapEnabled(true)
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL)
            legend.setDrawInside(false)

            val rightAxis: YAxis = getAxisRight()
            rightAxis.setDrawGridLines(false)
            rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

            val leftAxis: YAxis = getAxisLeft()
            leftAxis.setDrawGridLines(false)
            leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

            val xAxis: XAxis = getXAxis()
            xAxis.position = XAxis.XAxisPosition.BOTH_SIDED
            xAxis.axisMinimum = 0f
            xAxis.granularity = 1f
            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return months.get(value.toInt() % months.size)
                }
            }

            val data = CombinedData()
            data.setData(viewModel?.generateBarData())
            data.setData(viewModel?.generateLineData(true))
            data.setValueTypeface(tfLight)

//            xAxis.axisMaximum = data.xMax + 0.25f

            setData(data)
            invalidate()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

}