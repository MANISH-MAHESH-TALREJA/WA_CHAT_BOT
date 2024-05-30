package net.manish.wabot.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivitySendDirectMessageBinding;
import net.manish.wabot.model.PhoneNumberModel;
import net.manish.wabot.utilities.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SendDirectMessageActivity extends AppCompatActivity implements View.OnClickListener
{
    public int chatType = 1;
    ActivitySendDirectMessageBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_send_direct_message);
        SharedPreference preference = new SharedPreference(this);
        List<PhoneNumberModel> numberList;
        myThis.imgDirectBack.setOnClickListener(this);
        myThis.imgChoose.setOnClickListener(this);
        myThis.btnSendMsg.setOnClickListener(this);
        numberList = preference.getNumberList("NumberList");
        try
        {
            if (numberList.isEmpty())
            {
                numberList = new ArrayList<>();
            }
        }
        catch (Exception e)
        {
            Log.e("Number List", Objects.requireNonNull(e.getMessage()));
            numberList = new ArrayList<>();
        }
        myThis.phoneNumberRecycleview.setAdapter(new PhoneNumberAdapter(numberList));
        myThis.directChatRadioGroup.check(R.id.rb_direct_chat_whatsapp);
        myThis.directChatRadioGroup.setOnCheckedChangeListener((group, checkedId) -> chatType = myThis.directChatRadioGroup.findViewById(myThis.directChatRadioGroup.getCheckedRadioButtonId()).getId() == R.id.rb_direct_chat_whatsapp ? 1 : 2);
    }


    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.imgDirectBack)
        {
            finish();
        }
        if (id == R.id.btnSendMsg)
        {
            String obj = Objects.requireNonNull(myThis.edMessage.getText()).toString();
            String obj2 = Objects.requireNonNull(myThis.edPhoneNumber.getText()).toString();
            String str = myThis.countryCode.getSelectedCountryCode() + obj2;
            if (obj.isEmpty())
            {
                Toast.makeText(SendDirectMessageActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
            }
            else if (obj2.isEmpty())
            {
                myThis.edPhoneNumber.setError("Please enter phone number");
                myThis.edPhoneNumber.requestFocus();
            }
            else if (obj2.length() < 7)
            {
                myThis.edPhoneNumber.setError("Please write correct phone number");
                myThis.edPhoneNumber.requestFocus();
            }
            else
            {
                try
                {
                    PackageManager packageManager = SendDirectMessageActivity.this.getPackageManager();
                    Intent intent = new Intent("android.intent.action.VIEW");

                    try
                    {
                        intent.setPackage(chatType == 1 ? "com.whatsapp" : "com.whatsapp.w4b");
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + str + "&text=" + obj));
                        if (intent.resolveActivity(packageManager) != null)
                        {
                            SendDirectMessageActivity.this.startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(this, chatType == 1 ? "Whatsapp" : "Whatsapp Business" + " Not install", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println(e.getLocalizedMessage());
                    }
                }
                catch (Exception e2)
                {
                    Toast.makeText(SendDirectMessageActivity.this, "Error/n" + e2, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder>
    {
        private final List<PhoneNumberModel> listItem;

        public PhoneNumberAdapter(List<PhoneNumberModel> list)
        {
            listItem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phone_number_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i)
        {
            viewHolder.txtNumber.setText(listItem.get(i).getNumber());
            viewHolder.txtTime.setText(Const.getTimeAgo(listItem.get(i).getTime()));
            viewHolder.linearPhone.setOnClickListener(view -> myThis.edPhoneNumber.setText(listItem.get(i).getNumber()));
        }


        @Override
        public int getItemCount()
        {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {

            public LinearLayout linearPhone;

            public TextView txtNumber;

            public TextView txtTime;

            public ViewHolder(View view)
            {
                super(view);
                txtNumber = view.findViewById(R.id.txtPhoneNumber);
                txtTime = view.findViewById(R.id.txtPhoneTime);
                linearPhone = view.findViewById(R.id.linearPhoneNumber);
            }
        }
    }
}
