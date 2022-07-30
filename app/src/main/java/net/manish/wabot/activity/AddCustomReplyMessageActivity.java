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
import net.manish.wabot.adapter.AutoReplyAdapter;
import net.manish.wabot.databinding.ActivityAddCustomReplyMessageBinding;
import net.manish.wabot.model.AutoReply;
import net.manish.wabot.utilities.Const;

import java.util.ArrayList;

public class AddCustomReplyMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoReplyAdapter autoReplyAdapter;
    private String edIncomingMsg = "";
    private String edReplyMsg = "";
    private String edValue = "";
    private SharedPreference preference;
    ActivityAddCustomReplyMessageBinding thisb;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        thisb = (ActivityAddCustomReplyMessageBinding) DataBindingUtil.setContentView(this, R.layout.activity_add_custom_reply_message);
        Const.replyList = new ArrayList();
        preference = new SharedPreference(this);
        Const.replyList = preference.getList("MessageList");
        edValue = getIntent().getStringExtra("ActivityValue");
        if (edValue == null || !edValue.equals("Update")) {
            if (edValue != null && edValue.equals("Add")) {
                edValue = "";
                thisb.btnAddMessage.setText("Add Message");
            }
        } else {
            edIncomingMsg = getIntent().getStringExtra("ReceiveMessage");
            edReplyMsg = getIntent().getStringExtra("SendMessage");
            thisb.edIncomingMsg.setText(edIncomingMsg);
            thisb.edReplyMsg.setText(edReplyMsg);
            thisb.txtReply.setText(edReplyMsg);
            thisb.txtIncoming.setText(edIncomingMsg);
            thisb.btnAddMessage.setText("Update Message");
        }
        thisb.btnAddMessage.setOnClickListener(this);
        //TMAdsUtils.loadBigBannerAds(AddCustomReplyMessageActivity.this,thisb.adsview);
        thisb.imgBack.setOnClickListener(this);
        thisb.edIncomingMsg.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                thisb.txtIncoming.setText(thisb.edIncomingMsg.getText().toString().toString().trim());
            }


            @Override
            public void afterTextChanged(Editable editable) {
                if (thisb.edIncomingMsg.getText().toString().toString().trim().isEmpty()) {
                    thisb.txtIncoming.setText("Type incoming message below");
                } else {
                    thisb.txtIncoming.setText(thisb.edIncomingMsg.getText().toString().toString().trim());
                }
            }
        });
        thisb.edReplyMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                thisb.txtReply.setText(thisb.edReplyMsg.getText().toString().toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (thisb.edReplyMsg.getText().toString().toString().trim().isEmpty()) {
                    thisb.txtReply.setText("Type your reply message");
                } else {
                    thisb.txtReply.setText(thisb.edReplyMsg.getText().toString().toString().trim());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnAddMessage) {
            try {
                if (Const.replyList.isEmpty()) {
                    Const.replyList = new ArrayList();
                }
                if (thisb.edIncomingMsg.getText().toString().toString().trim().isEmpty()) {
                    thisb.edIncomingMsg.requestFocus();
                    thisb.edIncomingMsg.setError("Please fill Incoming message");
                } else if (thisb.edReplyMsg.getText().toString().toString().trim().isEmpty()) {
                    thisb.edReplyMsg.requestFocus();
                    thisb.edReplyMsg.setError("Please fill Reply message");
                } else {

                    if (edValue != null) {
                        if (edValue.equals("Update")) {
                            for (int i = 0; i < Const.replyList.size(); i++) {
                                if (edIncomingMsg.equalsIgnoreCase(Const.replyList.get(i).getReceiveMsg()) && edReplyMsg.equalsIgnoreCase(Const.replyList.get(i).getSendMsg())) {
                                    Const.replyList.set(i, new AutoReply(thisb.edIncomingMsg.getText().toString().trim(), thisb.edReplyMsg.getText().toString().trim()));
                                    Log.e("I Value", i + ":::");
                                    preference.setList("MessageList", Const.replyList);
                                    finish();
                                }
                                Log.e("List Size", Const.replyList.size() + "::::");
                            }
                            return;
                        }
                    }
                    if (Const.replyList == null || Const.replyList.isEmpty()) {
                        Const.replyList = new ArrayList();
                    }
                    Const.replyList.add(new AutoReply(thisb.edIncomingMsg.getText().toString().toString().trim(), thisb.edReplyMsg.getText().toString().toString().trim()));
                    autoReplyAdapter = new AutoReplyAdapter(this, Const.replyList);

                    Toast.makeText(this, "Message added sucessfully", Toast.LENGTH_SHORT).show();
                    preference.setList("MessageList", Const.replyList);
                    finish();
                }
            } catch (Exception e) {
                Log.e("ADD MSG", e.getMessage());
                Const.replyList = new ArrayList();
            }
        } else if (id == R.id.imgBack) {
            finish();
        }
    }
}
