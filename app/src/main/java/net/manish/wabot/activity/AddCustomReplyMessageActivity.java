package net.manish.wabot.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityAddCustomReplyMessageBinding;
import net.manish.wabot.model.AutoReply;
import net.manish.wabot.utilities.Const;

import java.util.ArrayList;
import java.util.Objects;

public class AddCustomReplyMessageActivity extends AppCompatActivity implements View.OnClickListener
{
    private String edIncomingMsg = "";
    private String edReplyMsg = "";
    private String edValue = "";
    private SharedPreference preference;
    ActivityAddCustomReplyMessageBinding myThis;


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_add_custom_reply_message);
        Const.replyList = new ArrayList<>();
        preference = new SharedPreference(this);
        Const.replyList = preference.getList("MessageList");
        edValue = getIntent().getStringExtra("ActivityValue");
        if (edValue == null || !edValue.equals("Update"))
        {
            if (edValue != null && edValue.equals("Add"))
            {
                edValue = "";
                myThis.btnAddMessage.setText("Add Message");
            }
        }
        else
        {
            edIncomingMsg = getIntent().getStringExtra("ReceiveMessage");
            edReplyMsg = getIntent().getStringExtra("SendMessage");
            myThis.edIncomingMsg.setText(edIncomingMsg);
            myThis.edReplyMsg.setText(edReplyMsg);
            myThis.txtReply.setText(edReplyMsg);
            myThis.txtIncoming.setText(edIncomingMsg);
            myThis.btnAddMessage.setText("Update Message");
        }
        myThis.btnAddMessage.setOnClickListener(this);
        myThis.imgBack.setOnClickListener(this);
        myThis.edIncomingMsg.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
                myThis.txtIncoming.setText(Objects.requireNonNull(myThis.edIncomingMsg.getText()).toString().trim());
            }


            @Override
            public void afterTextChanged(Editable editable)
            {
                if (Objects.requireNonNull(myThis.edIncomingMsg.getText()).toString().trim().isEmpty())
                {
                    myThis.txtIncoming.setText("Type incoming message below");
                }
                else
                {
                    myThis.txtIncoming.setText(myThis.edIncomingMsg.getText().toString().trim());
                }
            }
        });
        myThis.edReplyMsg.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
                myThis.txtReply.setText(Objects.requireNonNull(myThis.edReplyMsg.getText()).toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                if (Objects.requireNonNull(myThis.edReplyMsg.getText()).toString().trim().isEmpty())
                {
                    myThis.txtReply.setText("Type your reply message");
                }
                else
                {
                    myThis.txtReply.setText(myThis.edReplyMsg.getText().toString().trim());
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.btnAddMessage)
        {
            try
            {
                if (Const.replyList.isEmpty())
                {
                    Const.replyList = new ArrayList<>();
                }
                if (Objects.requireNonNull(myThis.edIncomingMsg.getText()).toString().trim().isEmpty())
                {
                    myThis.edIncomingMsg.requestFocus();
                    myThis.edIncomingMsg.setError("Please fill Incoming message");
                }
                else if (Objects.requireNonNull(myThis.edReplyMsg.getText()).toString().trim().isEmpty())
                {
                    myThis.edReplyMsg.requestFocus();
                    myThis.edReplyMsg.setError("Please fill Reply message");
                }
                else
                {

                    if (edValue != null)
                    {
                        if (edValue.equals("Update"))
                        {
                            for (int i = 0; i < Const.replyList.size(); i++)
                            {
                                if (edIncomingMsg.equalsIgnoreCase(Const.replyList.get(i).getReceiveMsg()) && edReplyMsg.equalsIgnoreCase(Const.replyList.get(i).getSendMsg()))
                                {
                                    Const.replyList.set(i, new AutoReply(myThis.edIncomingMsg.getText().toString().trim(), myThis.edReplyMsg.getText().toString().trim()));
                                    Log.e("I Value", i + ":::");
                                    preference.setList("MessageList", Const.replyList);
                                    finish();
                                }
                                Log.e("List Size", Const.replyList.size() + "::::");
                            }
                            return;
                        }
                    }
                    if (Const.replyList == null || Const.replyList.isEmpty())
                    {
                        Const.replyList = new ArrayList<>();
                    }
                    Const.replyList.add(new AutoReply(myThis.edIncomingMsg.getText().toString().trim(), myThis.edReplyMsg.getText().toString().trim()));

                    Toast.makeText(this, "Message added successfully", Toast.LENGTH_SHORT).show();
                    preference.setList("MessageList", Const.replyList);
                    finish();
                }
            }
            catch (Exception e)
            {
                Log.e("ADD MSG", Objects.requireNonNull(e.getMessage()));
                Const.replyList = new ArrayList<>();
            }
        }
        else if (id == R.id.imgBack)
        {
            finish();
        }
    }
}
