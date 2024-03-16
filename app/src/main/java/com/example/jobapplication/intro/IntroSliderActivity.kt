package com.example.jobapplication.intro



import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.jobapplication.R
import com.example.jobapplication.activities.SelectActivity
import com.example.jobapplication.adapters.ViewPagerAdapter
import com.example.jobapplication.auth.LoginActivity


class IntroSliderActivity : AppCompatActivity() {

    private lateinit var mSLideViewPager: ViewPager
    private lateinit var mDotLayout: LinearLayout
    private lateinit var backbtn: Button
    private lateinit var nextbtn: Button
    private lateinit var skipbtn: Button
    private lateinit var dots: Array<TextView>
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var img2 : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_slider)


        backbtn = findViewById(R.id.backBtn)
        nextbtn = findViewById(R.id.nextBtn)
        skipbtn = findViewById(R.id.skipBtn)
        img2 = findViewById(R.id.imageView1)

        backbtn.setOnClickListener {
            if (getitem(0) > 0) {
                mSLideViewPager.setCurrentItem(getitem(-1), true)
            }
        }

        nextbtn.setOnClickListener {
            if (getitem(0) < 2) {
                mSLideViewPager.setCurrentItem(getitem(1), true)
            } else {
                val i = Intent(this@IntroSliderActivity, SelectActivity::class.java)
                startActivity(i)
                finish()
            }
        }

        skipbtn.setOnClickListener {
            val i = Intent(this@IntroSliderActivity, SelectActivity::class.java)
            startActivity(i)
            finish()
        }


        mSLideViewPager = findViewById(R.id.slideViewPager)
        mDotLayout = findViewById(R.id.indicator_layout)

        viewPagerAdapter = ViewPagerAdapter(this)

        mSLideViewPager.adapter = viewPagerAdapter

        setUpindicator(0)
        mSLideViewPager.addOnPageChangeListener(viewListener)

    }


    private fun setUpindicator(position: Int) {
        dots = Array(3) { TextView(this) }
        mDotLayout.removeAllViews()

        for (i in 0 until dots.size) {
            dots[i] = TextView(this)
            dots[i].text = Html.fromHtml("&#8226")
            dots[i].textSize = 35.0F
            dots[i].setTextColor(resources.getColor(R.color.inactive, applicationContext.theme))
            mDotLayout.addView(dots[i])
        }

        dots[position].setTextColor(resources.getColor(R.color.active, applicationContext.theme))
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            setUpindicator(position)
            if (position > 0) {
                backbtn.visibility = View.VISIBLE
                img2.visibility = View.VISIBLE
            } else {
                backbtn.visibility = View.INVISIBLE
                img2.visibility = View.INVISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getitem(i: Int): Int {
        return mSLideViewPager.currentItem + i
    }
}