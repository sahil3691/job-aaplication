package com.example.jobapplication.intro

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.example.jobapplication.R
import com.example.jobapplication.activities.SelectActivity
import com.example.jobapplication.auth.LoginActivity

import com.github.appintro.AppIntro2
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType

class IntroSliderActivity : AppIntro2() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        addSlide(
            AppIntroFragment.createInstance(
                description = "I know I am hero \n I know I am hero ",
                imageDrawable = R.drawable.slider_new_1,
                backgroundDrawable = R.color.black,
                titleColorRes = R.color.purple,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.red,

            ))

        addSlide(
            AppIntroFragment.createInstance(

                description = "I know I am hero \n I know I am hero ",
                imageDrawable = R.drawable.slider_new_2 ,
                backgroundDrawable = R.color.black,
                titleColorRes = R.color.purple,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.red,
            ))

        addSlide(
            AppIntroFragment.createInstance(

                description = "I know I am hero \n I know I am hero ",
                imageDrawable = R.drawable.slider_new_3,
                backgroundDrawable = R.color.black,
                titleColorRes = R.color.purple,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.red,
            ))

//        // Change Indicator Color
//        setIndicatorColor(
//            selectedIndicatorColor = getColor(R.color.black),
//            unselectedIndicatorColor = getColor(R.color.blue)
//        )
        setProgressIndicator()


        showStatusBar(true);
        setTransformer(AppIntroPageTransformerType.Zoom)
        isWizardMode = true
        isVibrate = true
        vibrateDuration = 50L
        isWizardMode = true
        setImmersiveMode()

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        startActivity(Intent(this@IntroSliderActivity, SelectActivity::class.java))
        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        startActivity(Intent(this@IntroSliderActivity, SelectActivity::class.java))
        finish()
    }
}
