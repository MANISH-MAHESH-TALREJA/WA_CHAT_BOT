package net.manish.wabot.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener
{
    ActivitySettingsBinding myThis;
    private SharedPreference preference;


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        ActivitySettingsBinding activitySettingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        myThis = activitySettingsBinding;
        activitySettingsBinding.linearReplyTime.setOnClickListener(this);
        preference = new SharedPreference(this);
        myThis.linearReplyHeader.setOnClickListener(this);
        myThis.linearReplyRules.setOnClickListener(this);
        myThis.linearGroupMsg.setOnClickListener(this);
        myThis.imgSettigsBack.setOnClickListener(this);
        myThis.linearRateApp.setOnClickListener(this);
        myThis.linearShareApp.setOnClickListener(this);

        myThis.linearTermAndCondition.setOnClickListener(this);
        myThis.switchgroups.setOnCheckedChangeListener((compoundButton, b) -> preference.addToPref_Boolean("GroupEnable", b));
        try
        {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);

            Log.e("VERSION", packageInfo.versionName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        myThis.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgSettigsBack:
                finish();
                return;
            case R.id.linearRateApp:
                rateApp();
                return;
            case R.id.linearReplyHeader:
                startActivity(new Intent(this, ReplyHeaderActivity.class));
                return;
            case R.id.linearReplyTime:
                startActivity(new Intent(this, ReplyTimeActivity.class));
                return;
            case R.id.linearReplyRules:
                startActivity(new Intent(this, ReplyRulesActivity.class));
                return;
            case R.id.linearShareApp:
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.TEXT", "DOWNLOAD WA BOT -> A WHATSAPP AUTO REPLIER BOT FROM GOOGLE PLAY STORE : https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(intent, "SHARE APP VIA"));
                return;
            case R.id.linearTermAndCondition:
                Intent intent2 = new Intent(this, PrivacyPolicyActivity.class);
                intent2.putExtra("Policy", "Settings");
                startActivity(intent2);
                return;
            default:
                return;
        }
    }

    public void rateApp()
    {
        try
        {
            startActivity(rateFromUrl("market://details"));
        }
        catch (Exception unused)
        {
            startActivity(rateFromUrl("https://play.google.com/store/apps/details?id=" + getPackageName()));
        }
    }

    @SuppressLint("WrongConstant")
    private Intent rateFromUrl(String str)
    {
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(String.format("%s?id=%s", new Object[]{str, getPackageName()})));

        intent.addFlags(1208483840);
        return intent;
    }
}
