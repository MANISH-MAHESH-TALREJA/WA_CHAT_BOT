package net.manish.wabot.activity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReplyRulesActivity extends AppCompatActivity implements View.OnClickListener {
    private AlertDialog.Builder builder;
    private int compareHour;
    private int compareMinute;
    private String eTime = "";
    public String endTime = "";
    public String formate;
    public SharedPreference preference;
    private String sTime = "";
    public String spinTime = "";
    public String startTime = "";
    ActivityReplyRulesBinding thisb;

    private TimePickerDialog timePickerDialog;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        thisb = (ActivityReplyRulesBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_rules);
        preference = new SharedPreference(this);

        thisb.chkContain.setOnClickListener(this);
        thisb.chkExact.setOnClickListener(this);
        thisb.imgTimeback.setOnClickListener(this);


        //bannerAd();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void checkBoxText() {
        thisb.chkContain.setText(Html.fromHtml("<font color='black'>Message that contains</font><br><font color='#808080'> Auto replay while get message containing specific words </font>"));
        thisb.chkExact.setText(Html.fromHtml("<font color='black'>Message with exact match</font><br><font color='#808080'> Auto reply while get message with exact matching text </font>"));
    }

    /*private void bannerAd() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        thisb.bannerAd.loadAd(new AdRequest.Builder().build());
    }*/

    @Override
    public void onResume() {
        super.onResume();
        checkBoxText();
        if (preference.getFromPref_Boolean("exact")) {
            thisb.chkExact.setChecked(true);
            thisb.chkContain.setChecked(false);
        } else if (preference.getFromPref_Boolean("contain")) {
            thisb.chkExact.setChecked(false);
            thisb.chkContain.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkContain:
                thisb.chkContain.setChecked(true);
                thisb.chkExact.setChecked(false);
                preference.addToPref_Boolean("contain", true);
                preference.addToPref_Boolean("exact", false);
                return;
            case R.id.chkExact:
                thisb.chkExact.setChecked(true);
                thisb.chkContain.setChecked(false);
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
