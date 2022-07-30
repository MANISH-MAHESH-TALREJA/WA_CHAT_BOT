package net.manish.wabot.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityReplyGroupBinding;

public class ReplyGroupActivity extends AppCompatActivity {
    private SharedPreference preference;
    ActivityReplyGroupBinding thisb;

    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        thisb = (ActivityReplyGroupBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_group);
        preference = new SharedPreference(this);
        thisb.switchgroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preference.addToPref_Boolean("GroupEnable", b);
            }
        });
        thisb.imgGroupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        thisb.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }
}
