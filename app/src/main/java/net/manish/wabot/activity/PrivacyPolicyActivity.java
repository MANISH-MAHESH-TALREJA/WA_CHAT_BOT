package net.manish.wabot.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicyActivity extends AppCompatActivity {
    private String Policy = "";
    private SharedPreference preference;
    ActivityPrivacyPolicyBinding thisb;

    
    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        thisb = (ActivityPrivacyPolicyBinding) DataBindingUtil.setContentView(this, R.layout.activity_privacy_policy);
        preference = new SharedPreference(this);
        thisb.policyWebView.loadUrl("https://templatemela.com/privacy");
        String stringExtra = getIntent().getStringExtra("Policy");
        Policy = stringExtra;
        if (stringExtra.equals("Settings")) {
            thisb.btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preference.addToPref_Boolean("PrivacyPolicy", true);
                    finish();
                }
            });
        } else if (!Policy.equals("Splash")) {
        } else {
            if (preference.getFromPref_Boolean("PrivacyPolicy")) {
                startActivity(new Intent(this, PermissionCheckActivity.class));
            } else {
                thisb.btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(PrivacyPolicyActivity.this, PermissionCheckActivity.class));
                        preference.addToPref_Boolean("PrivacyPolicy", true);
                    }
                });
            }
        }
    }
}
