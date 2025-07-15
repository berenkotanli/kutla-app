package com.example.kutlaapp.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.kutlaapp.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val tvAppName: TextView = findViewById(R.id.tvAppName)
        val confettiAnimation: LottieAnimationView = findViewById(R.id.confettiAnimation)

        val scaleAnim = ScaleAnimation(
            0.5f, 1.2f,
            0.5f, 1.2f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnim.duration = 1200
        scaleAnim.fillAfter = true

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000

        tvAppName.startAnimation(fadeIn)
        tvAppName.startAnimation(scaleAnim)

        scaleAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}

            override fun onAnimationEnd(animation: Animation?) {
                confettiAnimation.visibility = android.view.View.VISIBLE
                confettiAnimation.playAnimation()
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })

        tvAppName.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 3000)
    }
}
