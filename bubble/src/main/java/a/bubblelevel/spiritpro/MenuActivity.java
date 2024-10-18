package a.bubblelevel.spiritpro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.walhalla.common.KonfettiPresenter;
import com.walhalla.ui.plugins.Launcher;
import com.walhalla.ui.plugins.Module_U;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import a.bubblelevel.spiritpro.util.Const;


import a.bubblelevel.spiritpro.databinding.ActivityMenuBinding;



public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding binding;
    private Thread waitThread;
    private final boolean pulsarEnabled = true;
    private KonfettiPresenter k;



//    @Override
//    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
////        menu.add("crash").setOnMenuItemClickListener(v -> {
////            throw new RuntimeException("Test Crash"); // Force a crash
////        });
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//
//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        int itemId = item.getItemId();
//        if (itemId == R.id.action_about) {
//            Module_U.aboutDialog(this);
//            return true;
//        } else if (itemId == R.id.action_privacy_policy) {
//            Launcher.openBrowser(this, getString(R.string.url_privacy_policy));
//            return true;
//        } else if (itemId == R.id.action_rate_app) {
//            Launcher.rateUs(this);
//            return true;
//
////            case R.id.action_more_app_01:
////                Module_U.openMarket(this, "com.walhalla.ttloader");
////                return true;
//
////            case R.id.action_more_app_02:
////                Module_U.moreApp(this, "com.walhalla.vibro");
////                return true;
//        } else if (itemId == R.id.action_share_app) {
//            Module_U.shareThisApp(this);
//            return true;
//        } else if (itemId == R.id.action_discover_more_app) {
//            Module_U.moreApp(this);
//            return true;
//
////            case R.id.action_exit:
////                this.finish();
////                return true;
//        } else if (itemId == R.id.action_feedback) {
//            Module_U.feedback(this);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setSupportActionBar(binding.toolbar);
//        setTitle(null);
        k = new KonfettiPresenter(binding.konfettiView);
        AdAdmob adAdmob = new AdAdmob(this);
        adAdmob.BannerAd(binding.banner, this);
        adAdmob.FullscreenAd_Counter(this);

        binding.startButton.setOnClickListener(view ->
                startActivity(new Intent(MenuActivity.this, MainActivity.class)));

        binding.shareButton.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_SEND);
//            intent.putExtra(Intent.EXTRA_TEXT, Const.APP_URL);
//            intent.setType("text/plain");
//            startActivity(Intent.createChooser(intent, getString(R.string.abc_share)));
            k.rain();
            Module_U.shareThisApp(this);
        });
        binding.rateApp.setOnClickListener(view -> {
            k.explode();
            Launcher.rateUs(this);
            //throw new RuntimeException("0000");
        });
        if (pulsarEnabled) {
            this.binding.pulsator.setCount(1);
            this.binding.pulsator.setDuration(2_200);//single pulse duration
            this.binding.pulsator.start();
        }
    }

    @Override
    protected void onPause() {
        if (waitThread != null) {
            waitThread.interrupt();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {

        binding.startButton.setEnabled(true);
        //@@@     binding.shareButton.setEnabled(true);
        toggleStartButton(true);
        super.onResume();
        if (binding.konfettiView.isActive()) {
            binding.konfettiView.reset();
        }

    }

    public void toggleStartButton(boolean isEnabled) {
        if (isEnabled) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.startButton.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.startButton.setVisibility(View.INVISIBLE);
        }
    }

}
