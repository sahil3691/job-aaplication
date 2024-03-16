package com.example.jobapplication.intro




import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.jobapplication.R


class StartingActivity : AppCompatActivity() {

    private lateinit var animatedLayout: FrameLayout
    private lateinit var startBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = Color.TRANSPARENT


        animatedLayout = findViewById(R.id.animatedLayout)
        startBtn =  findViewById(R.id.StartBtn)

        val myTextView = findViewById<TextView>(R.id.text_view)
        val tv1 = findViewById<TextView>(R.id.tv1)
        val tv2 = findViewById<TextView>(R.id.tv2)
        val img1 = findViewById<ImageView>(R.id.imageView)
        val logoImg = findViewById<ImageView>(R.id.logoIv)

        val animationView2 = findViewById<LottieAnimationView>(R.id.startAnimation)
        animationView2.setAnimation((R.raw.intro))
        animationView2.repeatCount  = 1
        animationView2.playAnimation()

        Handler(mainLooper).postDelayed({
            animateLayout()


        }, 11000)


        Handler(mainLooper).postDelayed({
            myTextView.visibility = View.VISIBLE
            val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            myTextView.startAnimation(fadeInAnimation)
            tv1.visibility = View.VISIBLE
            tv2.visibility = View.VISIBLE
            tv1.startAnimation(fadeInAnimation)
            tv2.startAnimation(fadeInAnimation)
            img1.visibility = View.VISIBLE
            startBtn.visibility = View.VISIBLE
            img1.startAnimation(fadeInAnimation)
            startBtn.startAnimation(fadeInAnimation)
            logoImg.visibility = View.VISIBLE
            logoImg.startAnimation(fadeInAnimation)


        }, 11000)


        startBtn.setOnClickListener {
            val intent  = Intent(this@StartingActivity, IntroSliderActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun animateLayout() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_down)
        animatedLayout.visibility = FrameLayout.VISIBLE
        animatedLayout.startAnimation(animation)


    }
}