package net.manish.wabot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivitySplashscreenBinding;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;*/

public class SplashscreenActivity extends AppCompatActivity {
    //public static InterstitialAd mInterstitialAd;
    private Activity activity;
    public Context context;
    public SharedPreference preference;
    ActivitySplashscreenBinding thisb;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        thisb = (ActivitySplashscreenBinding) DataBindingUtil.setContentView(this, R.layout.activity_splashscreen);
        context = this;
        activity = this;
        preference = new SharedPreference(this);
        getActivity();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void getActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //InterstitialAd(SplashscreenActivity.this);
                if (!preference.getFromPref_Boolean("PrivacyPolicy")) {
                    Intent intent = new Intent(SplashscreenActivity.this, PrivacyPolicyActivity.class);
                    intent.putExtra("Policy", "Splash");
                    startActivity(intent);
                    finish();
                } else if (Build.VERSION.SDK_INT < 23) {
                } else {
                    if (context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(SplashscreenActivity.this, MainActivity.class));
                        finish();
                        return;
                    }
                    startActivity(new Intent(SplashscreenActivity.this, PermissionCheckActivity.class));
                }
            }
        }, 2500);
    }


    /*public void InterstitialAd(final Context context2) {
        InterstitialAd.load(context2, context2.getString(R.string.admob_interstitial_id), new AdRequest.Builder().build(), new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(InterstitialAd interstitialAd) {

                Log.e("START", "ADS");
                if (mInterstitialAd != null) {
                    mInterstitialAd.show((Activity) context2);
                } else {
                    Log.e("TAG", "The interstitial ad wasn't ready yet.");
                }
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                Log.e("ADS FAILED", loadAdError.toString() + "::::");
                mInterstitialAd = null;
            }
        });
    }*/
}
