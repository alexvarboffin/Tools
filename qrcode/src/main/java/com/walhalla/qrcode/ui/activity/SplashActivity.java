//package com.walhalla.qrcode.ui.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.databinding.DataBindingUtil;
//
//import com.walhalla.qrcode.R;
//import com.walhalla.qrcode.databinding.ActivitySplashBinding;
//
//
//public class SplashActivity extends AppCompatActivity {
//
//    /**
//     * Constants
//     */
//    private final int SPLASH_DELAY = 800_000;
//    private final int ANIM_DELAY = 800;
//    /**
//     * Fields
//     */
//    private ActivitySplashBinding mBinding;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
//        this.mBinding.iconLogo.setImageResource(R.drawable.ic_cool_logo);
//
//        getWindow().setBackgroundDrawable(null);
//        animateLogo();
//        goToMainPage();
//    }
//
//    /**
//     * This method takes user to the main page
//     */
//    private void goToMainPage() {
//        new Handler().postDelayed(() -> {
//            this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
//            this.finish();
//        }, SPLASH_DELAY);
//    }
//
//    /**
//     * This method animates the logo
//     */
//    private void animateLogo() {
//        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_without_duration);
//        fadeInAnimation.setDuration(ANIM_DELAY);//Потушить иконку в черный фон
//        this.mBinding.iconLogo.startAnimation(fadeInAnimation);
//    }
//
//
//}
