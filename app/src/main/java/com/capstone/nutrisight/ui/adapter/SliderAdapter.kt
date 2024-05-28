package com.capstone.nutrisight.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.capstone.nutrisight.R

class SliderAdapter(private val context: Context): PagerAdapter() {

    private val images = intArrayOf(
        R.drawable.nutrilogo2,
        R.drawable.analysis_illustration,
        R.drawable.grade_illustration,
        R.drawable.ui_illustration
    )

    private val desc = intArrayOf(
        R.string.first_desc,
        R.string.second_desc,
        R.string.third_desc,
        R.string.fourth_desc
    )

    private val titles = intArrayOf(
        R.string.first_title,
        R.string.second_title,
        R.string.third_title,
        R.string.fourth_title
    )

    override fun getCount(): Int {
        return titles.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as android.view.LayoutInflater
        val view = inflater.inflate(R.layout.slides_layout, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageSlider)
        val descView = view.findViewById<TextView>(R.id.textSlider)
        val titleView = view.findViewById<TextView>(R.id.titleSlider)

        imageView.setImageResource(images[position])
        descView.setText(desc[position])
        titleView.setText(titles[position])

        container.addView(view)
        return view
    }

}