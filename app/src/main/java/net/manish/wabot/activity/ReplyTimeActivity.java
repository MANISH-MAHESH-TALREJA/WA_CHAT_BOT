package net.manish.wabot.activity;

import android.app.TimePickerDialog;
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
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/
import net.manish.wabot.databinding.ActivityReplyTimeBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReplyTimeActivity extends AppCompatActivity implements View.OnClickListener {
    private int compareHour;
    private int compareMinute;
    private String eTime = "";
    public String endTime = "";
    public String formate;
    public SharedPreference preference;
    private String sTime = "";
    public String spinTime = "";
    public String startTime = "";
    ActivityReplyTimeBinding myThis;
    public String time = "";


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        myThis = (ActivityReplyTimeBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_time);
       preference = new SharedPreference(this);
       time = preference.getFromPref_String("TimeofMsg");
        if (time.equals("")) {
            time = String.valueOf(5);
        }
        spinTime = preference.getFromPref_String("SpinTime");
        if (spinTime.isEmpty()) {
            spinTime = "Seconds";
        }
        sTime = preference.getFromPref_String("StartTime");
        eTime = preference.getFromPref_String("EndTime");
        if (sTime.equals("")) {
            myThis.txtStartTime.setText("No start time selected");
        } else {
            myThis.txtStartTime.setText(sTime);
        }
        if (eTime.equals("")) {
            myThis.txtEndTime.setText("No start time selected");
        } else {
            myThis.txtEndTime.setText(eTime);
        }
        myThis.chkScheduleTime.setChecked(preference.getFromPref_Boolean("ScheduleTime"));
        myThis.chkImmediately.setOnClickListener(this);
        myThis.chkTime.setOnClickListener(this);
        myThis.chkOnce.setOnClickListener(this);
        myThis.imgTimeback.setOnClickListener(this);
        myThis.linearStartTime.setOnClickListener(this);
        myThis.linearEndTime.setOnClickListener(this);
        myThis.chkScheduleTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    preference.addToPref_Boolean("ScheduleTime", true);
                } else {
                    preference.addToPref_Boolean("ScheduleTime", false);
                }
            }
        });
        //bannerAd();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (myThis.chkScheduleTime.isChecked() && sTime.equals("") && eTime.equals("")) {
            Toast.makeText(this, "Please Select the time", Toast.LENGTH_SHORT).show();
        }
    }
    private void checkBoxText() {
        myThis.chkImmediately.setText(Html.fromHtml("<font color='black'>Reply Immediately</font><br><font color='#808080'>Don't delay in sending auto-reply </font>"));
        myThis.chkTime.setText(Html.fromHtml("<font color='black'>Wait Time</font><br><font color='#808080'>Don't send a reply immediately to the same person,wait for " + time + " " + spinTime + " for the next auto-reply </font>"));
        myThis.chkOnce.setText(Html.fromHtml("<font color='black'>Reply Once</font><br><font color='#808080'>Reply only once to the same person/group until you ON auto-reply next time </font>"));
        myThis.chkScheduleTime.setText(Html.fromHtml("<font color='black'>Schedule Reply Time</font><br><font color='#808080'>Reply only scheduled time that you set for auto reply messages</font>"));
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
        if (preference.getFromPref_Boolean("Once")) {
            myThis.chkOnce.setChecked(true);
            myThis.chkImmediately.setChecked(false);
            myThis.chkTime.setChecked(false);
        } else if (preference.getFromPref_Boolean("Time")) {
            myThis.chkOnce.setChecked(false);
            myThis.chkImmediately.setChecked(false);
            myThis.chkTime.setChecked(true);
        } else if (preference.getFromPref_Boolean("Immediately")) {
            myThis.chkOnce.setChecked(false);
            myThis.chkImmediately.setChecked(true);
            myThis.chkTime.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chkContain:
                myThis.chkImmediately.setChecked(true);
                myThis.chkTime.setChecked(false);
                myThis.chkOnce.setChecked(false);
                preference.addToPref_Boolean("Immediately", true);
                preference.addToPref_Boolean("Once", false);
                preference.addToPref_Boolean("Time", false);
                return;
            case R.id.chkOnce :
                myThis.chkOnce.setChecked(true);
                myThis.chkTime.setChecked(false);
                myThis.chkImmediately.setChecked(false);
                preference.addToPref_Boolean("Once", true);
                preference.addToPref_Boolean("Time", false);
                preference.addToPref_Boolean("Immediately", false);
                return;
            case R.id.chkTime :
                myThis.chkTime.setChecked(true);
                myThis.chkImmediately.setChecked(false);
                myThis.chkOnce.setChecked(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle((CharSequence) "Wait Time");
                View inflate = getLayoutInflater().inflate(R.layout.reply_time_design_layout, (ViewGroup) null);
                builder.setView(inflate);
                final EditText editText = (EditText) inflate.findViewById(R.id.edTime);
                final Spinner spinner = (Spinner) inflate.findViewById(R.id.spinTime);
                if (preference.getFromPref_String("SpinTime").equals("Minutes")) {
                    spinner.setSelection(1);
                } else if (preference.getFromPref_String("SpinTime").equals("Seconds")) {
                    spinner.setSelection(0);
                } else {
                    spinner.setSelection(0);
                }
                editText.setText(time);
                editText.setSelection(editText.getText().length());
                builder.setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                  @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton((CharSequence) "Ok", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        preference.addToPref_String("TimeofMsg", editText.getText().toString().trim());
                        preference.addToPref_String("SpinTime", spinner.getSelectedItem().toString());
                        Log.e("Spin Value", spinner.getSelectedItem().toString() + "::::");
                        time = editText.getText().toString().trim();
                        spinTime = spinner.getSelectedItem().toString();
                        myThis.chkTime.setText(Html.fromHtml("<font color='black'>Wait Time</f><br><font color='#808080'>Don't send a reply immediately to the same person,wait for " + time + " " + spinTime + " for the next auto-reply </font>"));
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
                preference.addToPref_Boolean("Time", true);
                preference.addToPref_Boolean("Once", false);
                preference.addToPref_Boolean("Immediately", false);
                return;
            case R.id.imgTimeback :
                finish();
                if (myThis.chkScheduleTime.isChecked() && sTime.equals("") && eTime.equals("")) {
                    Toast.makeText(this, "Please Select the time", Toast.LENGTH_SHORT).show();
                    return;
                }
                return;
            case R.id.linearEndTime :
                Calendar instance = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener()
                {
                    public void onTimeSet(TimePicker timePicker, int i, int i2)
                    {
                        if (i == 0)
                        {
                            i += 12;
                            formate = "AM";
                        }
                        else if (i == 12)
                        {
                            formate = "PM";
                        }
                        else if (i > 12)
                        {
                            i -= 12;
                            formate = "PM";
                        }
                        else
                        {
                            formate = "AM";
                        }
                        endTime = String.format("%02d", new Object[]{Integer.valueOf(i)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(i2)}) + " " + formate;
                        StringBuilder sb = new StringBuilder();
                        sb.append(endTime);
                        sb.append(":::");
                        Log.e("End Time", sb.toString());
                        try
                        {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                            if (startTime.isEmpty() && startTime.equals(""))
                            {
                                ReplyTimeActivity replyTimeActivity = ReplyTimeActivity.this;
                                String unused6 = replyTimeActivity.startTime = replyTimeActivity.preference.getFromPref_String("StartTime");
                            }
                            Date parse = simpleDateFormat.parse(startTime);
                            Date parse2 = simpleDateFormat.parse(endTime);
                            if (isAfterTime(parse, parse2))
                            {
                                Toast.makeText(ReplyTimeActivity.this, "Please select proper Time", Toast.LENGTH_SHORT).show();
                                Log.e("After Time", isAfterTime(parse, parse2) + "::::");
                                return;
                            }
                            myThis.txtEndTime.setText(endTime);
                            preference.addToPref_String("EndTime", endTime);
                        }
                        catch (ParseException e)
                        {
                            e.printStackTrace();
                            Log.e("EndTime", e.getMessage());
                        }
                    }
                }, instance.get(11), instance.get(12), false);
                timePickerDialog.show();
                return;
            case R.id.linearStartTime :
                Calendar instance2 = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int i, int i2) {
                        if (i == 0) {
                            i += 12;
                            formate = "AM";
                        } else if (i == 12) {
                           formate = "PM";
                        } else if (i > 12) {
                            i -= 12;
                             formate = "PM";
                        } else {
                             formate = "AM";
                        }
                        startTime = String.format("%02d", new Object[]{Integer.valueOf(i)}) + ":" + String.format("%02d", new Object[]{Integer.valueOf(i2)}) + " " + formate;
                        myThis.txtStartTime.setText(startTime);
                        preference.addToPref_String("StartTime", startTime);
                    }
                }, instance2.get(11), instance2.get(12), false);
                timePickerDialog.show();
                return;
            default:
                return;
        }
    }

    
    public boolean isAfterTime(Date date, Date date2) {
        return !date2.after(date);
    }
}
