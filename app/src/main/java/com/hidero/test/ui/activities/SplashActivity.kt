package com.hidero.test.ui.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.hidero.test.R
import com.hidero.test.util.SPLASH_TIME_OUT


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        YoYo.with(Techniques.Bounce)
            .duration(7000)
            .playOn(findViewById(R.id.logo))
        YoYo.with(Techniques.FadeInUp)
            .duration(5000)
            .playOn(findViewById(R.id.appname))

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_fade_out)
        }, SPLASH_TIME_OUT)
    }

}
