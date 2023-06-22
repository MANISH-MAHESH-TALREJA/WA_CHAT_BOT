package net.manish.wabot.activity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityReplyRulesBinding;

public class ReplyRulesActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog.Builder builder;
    private int compareHour;
    private int compareMinute;
    private final String eTime = "";
    public String endTime = "";
    public String formate;
    public SharedPreference preference;
    private final String sTime = "";
    public String spinTime = "";
    public String startTime = "";
    ActivityReplyRulesBinding myThis;

    private TimePickerDialog timePickerDialog;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        myThis = (ActivityReplyRulesBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_rules);
        preference = new SharedPreference(this);

        myThis.chkContain.setOnClickListener(this);
        myThis.chkExact.setOnClickListener(this);
        myThis.imgTimeback.setOnClickListener(this);


        //bannerAd();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void checkBoxText() {
        myThis.chkContain.setText(Html.fromHtml("<font color='black'>Message that contains</font><br><font color='#808080'> Auto reply while get message containing specific words </font>"));
        myThis.chkExact.setText(Html.fromHtml("<font color='black'>Message with exact match</font><br><font color='#808080'> Auto reply while get message with exact matching text </font>"));
    }

    /*private void bannerAd() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        myThis.bannerAd.loadAd(new AdRequest.Builder().build());
    }*/

    @Override
    public void onResume() {
        super.onResume();
        checkBoxText();
        if (preference.getFromPref_Boolean("exact")) {
            myThis.chkExact.setChecked(true);
            myThis.chkContain.setChecked(false);
        } else if (preference.getFromPref_Boolean("contain")) {
            myThis.chkExact.setChecked(false);
            myThis.chkContain.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkContain:
                myThis.chkContain.setChecked(true);
                myThis.chkExact.setChecked(false);
                preference.addToPref_Boolean("contain", true);
                preference.addToPref_Boolean("exact", false);
                return;
            case R.id.chkExact:
                myThis.chkExact.setChecked(true);
                myThis.chkContain.setChecked(false);
                preference.addToPref_Boolean("exact", true);
                preference.addToPref_Boolean("contain", false);
                return;
            case R.id.imgTimeback:
                  finish();
                return;

                default:
                return;
        }
    }



}
