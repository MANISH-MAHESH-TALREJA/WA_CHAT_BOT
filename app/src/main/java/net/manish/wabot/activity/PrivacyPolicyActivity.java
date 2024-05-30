package net.manish.wabot.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity
{
    private SharedPreference preference;
    ActivityPrivacyPolicyBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        preference = new SharedPreference(this);
        myThis.policyWebView.loadUrl("https://sites.google.com/view/wa-chat-bot");
        String stringExtra = getIntent().getStringExtra("Policy");
        assert stringExtra != null;
        if (stringExtra.equals("Settings"))
        {
            myThis.btnAccept.setOnClickListener(view ->
            {
                preference.addToPref_Boolean("PrivacyPolicy", true);
                finish();
            });
        }
        else if (!stringExtra.equals("Splash"))
        {
            System.out.println("SPLASH");
        }
        else
        {
            if (preference.getFromPref_Boolean("PrivacyPolicy"))
            {
                startActivity(new Intent(this, PermissionCheckActivity.class));
            }
            else
            {
                myThis.btnAccept.setOnClickListener(view ->
                {
                    startActivity(new Intent(PrivacyPolicyActivity.this, PermissionCheckActivity.class));
                    preference.addToPref_Boolean("PrivacyPolicy", true);
                });
            }
        }
    }
}
