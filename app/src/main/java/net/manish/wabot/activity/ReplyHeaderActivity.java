package net.manish.wabot.activity;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.manish.wabot.NotificationService;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityReplyHeaderBinding;

public class ReplyHeaderActivity extends AppCompatActivity implements View.OnClickListener
{

    public String headerMsg;

    public SharedPreference preference;

    public String replyMsg;
    ActivityReplyHeaderBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_reply_header);
        preference = new SharedPreference(this);
        if (preference.getFromPref_String("ReplyHeader").isEmpty())
        {
            myThis.switchReplyHeader.setChecked(true);
        }
        replyMsg = preference.getFromPref_String("autoReplyText");
        headerMsg = preference.getFromPref_String("HeaderText");

        if (headerMsg.isEmpty())
        {
            headerMsg = NotificationService.TAG;
            myThis.txtHeaderText.setText(NotificationService.TAG);
            myThis.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
        }
        else
        {
            myThis.txtHeaderText.setText(preference.getFromPref_String("HeaderText"));
            myThis.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
        }
        headerSwitch();
        Log.e("Header Switch", preference.getFromPref_String("ReplyHeader") + ":::");
        myThis.linearHeaderMessage.setOnClickListener(this);
        myThis.editHeader.setOnClickListener(this);
        myThis.imgHeaderBack.setOnClickListener(this);
    }

    private void headerSwitch()
    {
        myThis.switchReplyHeader.setText(Html.fromHtml("<font color='black'>Enable</font><br><font color='#808080'>send the reply header along with the reply message</font>"));
        myThis.switchReplyHeader.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (b)
            {
                preference.addToPref_String("ReplyHeader", "Checked");
                myThis.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
                myThis.txtBoldText.setText(headerMsg);
                myThis.txtBoldText.setTypeface(null, Typeface.BOLD);
                preference.addToPref_String("BoldHeaderText", myThis.txtBoldText.getText().toString());
                return;
            }
            preference.addToPref_String("ReplyHeader", "Unchecked");
            myThis.txtReplyMessage.setText(replyMsg);
            preference.addToPref_String("BoldHeaderText", " ");
        });
    }


    @Override
    public void onResume()
    {
        super.onResume();
        if (preference.getFromPref_String("ReplyHeader").equals("Checked"))
        {
            myThis.switchReplyHeader.setChecked(true);
            myThis.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
            myThis.txtBoldText.setText(headerMsg);
            myThis.txtBoldText.setTypeface(null, Typeface.BOLD);
            preference.addToPref_String("BoldHeaderText", myThis.txtBoldText.getText().toString());
        }
        else if (preference.getFromPref_String("ReplyHeader").equals("Unchecked"))
        {
            myThis.switchReplyHeader.setChecked(false);
            myThis.txtReplyMessage.setText(replyMsg);
            preference.addToPref_String("BoldHeaderText", " ");
        }
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.imgHeaderBack)
        {
            finish();
        }
        else if (id == R.id.linearHeaderMessage)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Header Text");
            View inflate = getLayoutInflater().inflate(R.layout.reply_header_dialog_layout, null);
            builder.setView(inflate);
            final EditText editText = inflate.findViewById(R.id.edReplyHeaderMsg);
            editText.setText(myThis.txtHeaderText.getText().toString());
            editText.setSelection(editText.getText().length());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton("Ok", (dialogInterface, i) ->
            {
                myThis.txtHeaderText.setText(editText.getText().toString().trim());
                headerMsg = editText.getText().toString().trim();
                myThis.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
                preference.addToPref_String("HeaderText", editText.getText().toString().trim());
                if (preference.getFromPref_String("ReplyHeader").equals("Checked"))
                {
                    myThis.txtBoldText.setText(headerMsg);
                    myThis.txtBoldText.setTypeface(null, Typeface.BOLD);
                    preference.addToPref_String("BoldHeaderText", myThis.txtBoldText.getText().toString());
                }
                else
                {
                    preference.addToPref_String("BoldHeaderText", " ");
                }
                dialogInterface.dismiss();
            });
            builder.show();
        }
        else if (id == R.id.editHeader)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Header Text");
            View inflate = getLayoutInflater().inflate(R.layout.reply_header_dialog_layout, null);
            builder.setView(inflate);
            final EditText editText = inflate.findViewById(R.id.edReplyHeaderMsg);
            editText.setText(myThis.txtHeaderText.getText().toString());
            editText.setSelection(editText.getText().length());
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());
            builder.setPositiveButton("Ok", (dialogInterface, i) ->
            {
                myThis.txtHeaderText.setText(editText.getText().toString().trim());
                headerMsg = editText.getText().toString().trim();
                myThis.txtReplyMessage.setText(Html.fromHtml("<b>" + headerMsg + "</b><br>" + replyMsg));
                preference.addToPref_String("HeaderText", editText.getText().toString().trim());
                if (preference.getFromPref_String("ReplyHeader").equals("Checked"))
                {
                    myThis.txtBoldText.setText(headerMsg);
                    myThis.txtBoldText.setTypeface(null, Typeface.BOLD);
                    preference.addToPref_String("BoldHeaderText", myThis.txtBoldText.getText().toString());
                }
                else
                {
                    preference.addToPref_String("BoldHeaderText", " ");
                }
                dialogInterface.dismiss();
            });
            builder.show();
        }
    }
}
