package com.example.jobapplication.intro



import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
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
                title = "First Slide",
                description = "I know I am hero",
                imageDrawable = R.drawable.page1,
                backgroundDrawable = R.color.background,
                titleColorRes = R.color.purple,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.red,
            ))
        addSlide(
            AppIntroFragment.createInstance(
                title = "Second  Slide",
                description = "I know I am hero",
                imageDrawable = R.drawable.page2,
                backgroundDrawable = R.color.background,
                titleColorRes = R.color.purple,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.red,
            ))
        addSlide(
            AppIntroFragment.createInstance(
                title = "Third Slide",
                description = "I know I am hero",
                imageDrawable = R.drawable.page3,
                backgroundDrawable = R.color.background,
                titleColorRes = R.color.purple,
                descriptionColorRes = R.color.black,
                backgroundColorRes = R.color.red,
            ))

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