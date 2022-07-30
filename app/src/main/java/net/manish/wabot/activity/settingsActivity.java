package net.manish.wabot.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivitySettingsBinding;

public class settingsActivity extends AppCompatActivity implements View.OnClickListener {
    ActivitySettingsBinding thisb;
    private SharedPreference preference;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivitySettingsBinding activitySettingsBinding = (ActivitySettingsBinding) DataBindingUtil.setContentView(this, R.layout.activity_settings);
        thisb = activitySettingsBinding;
        activitySettingsBinding.linearReplyTime.setOnClickListener(this);
        preference = new SharedPreference(this);
        thisb.linearReplyHeader.setOnClickListener(this);
        thisb.linearReplyRules.setOnClickListener(this);
        thisb.linearGroupMsg.setOnClickListener(this);
        thisb.imgSettigsBack.setOnClickListener(this);
        thisb.linearRateApp.setOnClickListener(this);
        thisb.linearShareApp.setOnClickListener(this);

        thisb.linearTermAndCondition.setOnClickListener(this);
        thisb.switchgroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preference.addToPref_Boolean("GroupEnable", b);
            }
        });
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            Log.e("VERSION", packageInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        thisb.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgSettigsBack :
                finish();
                return;
                case R.id.linearRateApp :
                rateApp();
                return;
            case R.id.linearReplyHeader :
                startActivity(new Intent(this, ReplyHeaderActivity.class));
                return;
            case R.id.linearReplyTime :
                startActivity(new Intent(this, ReplyTimeActivity.class));
                return;
            case R.id.linearReplyRules :
                startActivity(new Intent(this, ReplyRulesActivity.class));
                return;
            case R.id.linearShareApp :
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", "App:https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "Share Via"));
                return;
            case R.id.linearTermAndCondition :
                Intent intent2 = new Intent(this, PrivacyPolicyActivity.class);
                intent2.putExtra("Policy", "Settings");
                startActivity(intent2);
                return;
            default:
                return;
        }
    }

    public void rateApp() {
        try {
            startActivity(rateFromUrl("market://details"));
        } catch (Exception unused) {
            startActivity(rateFromUrl("https://play.google.com/store/apps/details"));
        }
    }

    private Intent rateFromUrl(String str) {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("%s?id=%s", new Object[]{str, getPackageName()})));

        intent.addFlags(1208483840);
        return intent;
    }
}
