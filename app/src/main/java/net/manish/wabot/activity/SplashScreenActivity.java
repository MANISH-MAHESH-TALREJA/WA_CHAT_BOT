package net.manish.wabot.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
//import net.manish.wabot.databinding.ActivitySplashscreenBinding;

import yanzhikai.textpath.SyncTextPathView;


public class SplashScreenActivity extends AppCompatActivity
{
    public Context context;
    public SharedPreference preference;
    //ActivitySplashscreenBinding myThis;


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_splash_screen);

        //myThis = (ActivitySplashscreenBinding) DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        context = this;
        preference = new SharedPreference(this);
        SyncTextPathView syncTextPathView1 = findViewById(R.id.mainText);
        syncTextPathView1.startAnimation(0,1);
        SyncTextPathView syncTextPathView2 = findViewById(R.id.fancy_text3);
        syncTextPathView2.startAnimation(0,1);
        SyncTextPathView syncTextPathView3 = findViewById(R.id.fancy_text4);
        syncTextPathView3.startAnimation(0,1);
        getActivity();
    }


    @Override
    public void onResume()
    {
        super.onResume();
    }

    public void getActivity()
    {
        new Handler().postDelayed(() ->
        {
            if (!preference.getFromPref_Boolean("PrivacyPolicy"))
            {
                Intent intent = new Intent(SplashScreenActivity.this, PrivacyPolicyActivity.class);
                intent.putExtra("Policy", "Splash");
                startActivity(intent);
                finish();
            }
            else
            {
                if (context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED)
                {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                    return;
                }
                startActivity(new Intent(SplashScreenActivity.this, PermissionCheckActivity.class));
            }
        }, 5000);
    }

}
