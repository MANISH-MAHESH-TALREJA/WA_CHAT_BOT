package net.manish.wabot.activity;

import android.os.Bundle;
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
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_reply_group);
        preference = new SharedPreference(this);
        myThis.switchgroups.setOnCheckedChangeListener((compoundButton, b) -> preference.addToPref_Boolean("GroupEnable", b));
        myThis.imgGroupBack.setOnClickListener(view -> finish());
    }
    @Override
    public void onResume() {
        super.onResume();
        myThis.switchgroups.setChecked(preference.getFromPref_Boolean("GroupEnable"));
    }
}
