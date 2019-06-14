package com.yumingcui.android.unsplashphotos.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.yumingcui.android.unsplashphotos.R
import com.yumingcui.android.unsplashphotos.home.HomeActivity

import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startAnimations()
    }

    private fun startAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val fade = Fade()
            fade.duration = R.integer.splash_stay_time_in_milli_second.toLong()
            window.exitTransition = fade
        }

        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.splash_fade_out)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in)

        backgroundImageView.clearAnimation()
        backgroundImageView.startAnimation(fadeOut)

        blurredBackgroundImageView.clearAnimation()
        blurredBackgroundImageView.startAnimation(fadeIn)

        fadeIn.duration = 2000
        fadeOut.duration = 2000

        val animTranslate = AnimationUtils.loadAnimation(this, R.anim.splash_translate)
        logoImageView.clearAnimation()
        logoImageView.startAnimation(animTranslate)

        fadeIn.setAnimationListener(
            object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}

                override fun onAnimationEnd(animation: Animation) {
                    goToNextScreen()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
    }

    private fun goToNextScreen() {
        this.finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}
