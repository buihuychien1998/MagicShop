package com.hidero.test.ui.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.hidero.test.data.api.APIUtil
import com.hidero.test.data.repository.remote.CoroutineRepository
import com.hidero.test.data.valueobject.FormState
import com.hidero.test.data.valueobject.Statistic
import com.hidero.test.ui.base.BaseViewModel
import com.hidero.test.util.baseUrl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import kotlin.coroutines.CoroutineContext


class StatisticViewModel : BaseViewModel() {
    private val apiService by lazy {
        APIUtil.getCoroutineData(baseUrl)
    }
    private val parentJob = Job()
    val entries1: ArrayList<BarEntry> = ArrayList()
    val entries2: ArrayList<BarEntry> = ArrayList()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.IO

    private val scope = CoroutineScope(coroutineContext)
    private val repository =
        CoroutineRepository(apiService)
    private val _statistic = MutableLiveData<MutableList<Statistic>>()
    val statistic: LiveData<MutableList<Statistic>>
        get() = _statistic

    val formstate = MutableLiveData<FormState>()

    fun getStatistic(year: Int?) {
        formstate.value = FormState(true)
        scope.launch {
            try {
                _statistic.postValue(repository.getStatistic(year))
                formstate.postValue(FormState(false))

            } catch (ex: Exception) {
                Timber.e(ex)
            }
        }

    }

    fun generateLineData(status: Boolean): LineData? {
        val data = LineData()
        val entries: ArrayList<Entry> = ArrayList()
        if (status)
        for (index in 0 until 12) entries.add(
            Entry(
                index + 0.5f,
                getRandom(15F, 5F)
            )
        )
        else for (index in 0 until 12) entries.add(
            Entry(
                index + 0.5f,
                entries1[index].y -entries2[index].y
            )
        )
        val set = LineDataSet(entries, "Lợi nhuận")
        set.color = Color.rgb(240, 238, 70)
        set.lineWidth = 2.5f
        set.setCircleColor(Color.rgb(240, 238, 70))
        set.circleRadius = 5f
        set.fillColor = Color.rgb(240, 238, 70)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(240, 238, 70)
        set.axisDependency = YAxis.AxisDependency.LEFT
        data.addDataSet(set)
        return data
    }

    fun generateBarData(): BarData? {
        entries1.clear()
        entries2.clear()
//        for (index in 0 until 12) {
////            entries1.add(BarEntry(0F, getRandom(25F, 1F)))
////            entries2.add(BarEntry(0F, floatArrayOf(getRandom(13F, 12F), getRandom(13F, 12F))))
////            if (index < 7)
////                entries2.add(BarEntry(0F, getRandom(25F, 0F)))
////            else
////                entries2.add(BarEntry(0F, 0F))
//        }
        var j = 0
        statistic.value?.size?.let {
            for (i in 0 until 12) {
                if (j < it) {
                    if (statistic.value?.get(j)?.month == i) {
                        entries1.add(
                            BarEntry(
                                (i + 1).toFloat(),
                                statistic.value?.get(j)!!.money / 1000000
                            )
                        )
                        entries2.add(BarEntry((i + 1).toFloat(), getRandom(statistic.value?.get(j)!!.money / 1000000, 0F)))
                        j++
                    } else {
                        entries1.add(BarEntry(0f, 0f))
                        entries2.add(BarEntry(0f, 0f))
                    }
                } else {
                    entries1.add(BarEntry(0f, 0f))
                    entries2.add(BarEntry(0f, 0f))
                }

            }
        }

        val set1 = BarDataSet(entries1, "Doanh thu")
        set1.setColor(Color.rgb(104, 241, 175))
        set1.valueTextColor = Color.rgb(60, 220, 78)
        set1.valueTextSize = 10f
        set1.axisDependency = YAxis.AxisDependency.LEFT
        val set2 = BarDataSet(entries2, "Chi phí")
//        set2.stackLabels = arrayOf("Stack 1", "Stack 2")
        set2.setColor(Color.rgb(164, 228, 251))
//        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255))
        set2.valueTextColor = Color.rgb(61, 165, 255)
        set2.valueTextSize = 10f
        set2.axisDependency = YAxis.AxisDependency.LEFT
        val groupSpace = 0.06f
        val barSpace = 0.02f // x2 dataset
        val barWidth = 0.45f // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        val data = BarData(set1, set2)
        data.barWidth = barWidth
        data.setValueFormatter(LargeValueFormatter())
        // make this BarData object grouped
        data.groupBars(0f, groupSpace, barSpace) // start at x = 0
        return data
    }

    private fun getRandom(range: Float, start: Float): Float {
        return (Math.random() * range).toFloat() + start
    }

    fun roundTwoDecimals(d: Float): Float {
        val twoDForm = DecimalFormat("#.##")
        return twoDForm.format(d).toFloat()
    }
}