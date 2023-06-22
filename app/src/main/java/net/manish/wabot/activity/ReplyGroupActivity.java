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
    ActivityReplyGroupBinding myThis;

    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        myThis = (ActivityReplyGroupBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_group);
        preference = new SharedPreference(this);
        myThis.switchgroups.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preference.addToPref_Boolean("GroupEnable", b);
            }
        });
        myThis.imgGroupBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        myThis.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }
}
