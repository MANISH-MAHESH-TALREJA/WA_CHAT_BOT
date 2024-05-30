package net.manish.wabot.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityReplyRulesBinding;

public class ReplyRulesActivity extends AppCompatActivity implements View.OnClickListener
{
    public SharedPreference preference;
    ActivityReplyRulesBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_reply_rules);
        preference = new SharedPreference(this);
        myThis.chkContain.setOnClickListener(this);
        myThis.chkExact.setOnClickListener(this);
        myThis.imgTimeback.setOnClickListener(this);

    }

    private void checkBoxText()
    {
        myThis.chkContain.setText(Html.fromHtml("<font color='black'>Message that contains</font><br><font color='#808080'> Auto reply while get message containing specific words </font>"));
        myThis.chkExact.setText(Html.fromHtml("<font color='black'>Message with exact match</font><br><font color='#808080'> Auto reply while get message with exact matching text </font>"));
    }

    @Override
    public void onResume()
    {
        super.onResume();
        checkBoxText();
        if (preference.getFromPref_Boolean("exact"))
        {
            myThis.chkExact.setChecked(true);
            myThis.chkContain.setChecked(false);
        }
        else if (preference.getFromPref_Boolean("contain"))
        {
            myThis.chkExact.setChecked(false);
            myThis.chkContain.setChecked(true);
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.chkContain)
        {
            myThis.chkContain.setChecked(true);
            myThis.chkExact.setChecked(false);
            preference.addToPref_Boolean("contain", true);
            preference.addToPref_Boolean("exact", false);
        }
        else if (id == R.id.chkExact)
        {
            myThis.chkExact.setChecked(true);
            myThis.chkContain.setChecked(false);
            preference.addToPref_Boolean("exact", true);
            preference.addToPref_Boolean("contain", false);
        }
        else if (id == R.id.imgTimeback)
        {
            finish();
        }
    }

}
