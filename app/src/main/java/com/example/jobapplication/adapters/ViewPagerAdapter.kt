package com.example.jobapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.jobapplication.R

class ViewPagerAdapter(private val context: Context) : PagerAdapter() {

    private val images = arrayOf(
        R.drawable.slider_new_1,
        R.drawable.slider_new_2,
        R.drawable.slider_new_3,

    )

    private val headings = arrayOf(
        R.string.heading_one,
        R.string.heading_two,
        R.string.heading_three,

    )

    private val description = arrayOf(
        R.string.desc_one,
        R.string.desc_two,
        R.string.desc_three,

    )

    override fun getCount(): Int {
        return headings.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.slider_layout, container, false)

        val slideTitleImage: ImageView = view.findViewById(R.id.slider_image1)
        val slideHeading: TextView = view.findViewById(R.id.tv1)
        val slideDescription: TextView = view.findViewById(R.id.tv2)

        slideTitleImage.setImageResource(images[position])
        slideHeading.setText(headings[position])
        slideDescription.setText(description[position])

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}
