package net.manish.wabot.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityGetContactBinding;
import net.manish.wabot.utilities.Const;

import java.util.ArrayList;
import java.util.List;

public class GetContactActivity extends AppCompatActivity implements View.OnClickListener
{

    public List<String> cntList;
    private Cursor cursor;

    public SharedPreference preference;

    public List<String> searchList;

    public int textLength = 0;
    ActivityGetContactBinding myThis;


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = (ActivityGetContactBinding) DataBindingUtil.setContentView(this, R.layout.activity_get_contact);
        preference = new SharedPreference(this);
        cntList = new ArrayList();
        searchList = new ArrayList();
        getContactList();
        myThis.imgCntBack.setOnClickListener(this);
        myThis.imgCntSave.setOnClickListener(this);
        myThis.imgCntSearch.setOnClickListener(this);
        myThis.imgCntClose.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgCntBack:
            case R.id.imgCntSave:
                finish();
                return;
            case R.id.imgCntClose:
                myThis.edCntSearch.setText("");
                myThis.imgCntClose.setVisibility(View.GONE);
                getContactList();
                return;
            case R.id.imgCntSearch:
                myThis.txtCntSearch.setVisibility(View.GONE);
                myThis.imgCntSearch.setVisibility(View.GONE);
                myThis.edCntSearch.setVisibility(View.VISIBLE);
                myThis.edCntSearch.addTextChangedListener(new TextWatcher()
                {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
                    {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
                    {
                        textLength = myThis.edCntSearch.getText().toString().length();
                        cntList.clear();
                        for (int i4 = 0; i4 < searchList.size(); i4++)
                        {
                            if (textLength <= ((String) searchList.get(i4)).length() && ((String) searchList.get(i4)).toLowerCase().contains(myThis.edCntSearch.getText().toString().toLowerCase().trim()))
                            {
                                cntList.add((String) searchList.get(i4));
                            }
                        }

                        AppendList(cntList);
                    }

                    @Override
                    public void afterTextChanged(Editable editable)
                    {
                        if (myThis.edCntSearch.getText().toString().length() > 0)
                        {
                            myThis.imgCntClose.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return;
            default:
                return;
        }
    }

    @SuppressLint("Range")
    private void getContactList()
    {


       /*  cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "display_name ASC");
         cntList=new ArrayList<>();
        while (cursor.moveToNext()) {

        }
        cursor.close();*/

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "display_name ASC");
        String temp_name = "";
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(temp_name))
                continue;
            temp_name = name;
            cntList.add(phones.getString(phones.getColumnIndex("display_name")));
            searchList.add(phones.getString(phones.getColumnIndex("display_name")));

            //add name to your list or adapter here`enter code here`
        }
        phones.close();
        myThis.getContactRecycleView.setAdapter(new GetContactAdapter(this, cntList));
    }

    public void AppendList(List<String> list)
    {
        myThis.getContactRecycleView.setAdapter(new GetContactAdapter(this, list));
    }

    class GetContactAdapter extends RecyclerView.Adapter<GetContactAdapter.ViewHolder>
    {

        public List<String> listitem;

        public GetContactAdapter(Context context2, List<String> list)
        {
            listitem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_contact_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i)
        {
            viewHolder.txtContact.setText(listitem.get(i));
            viewHolder.txtCntFName.setText(String.valueOf(listitem.get(i).charAt(0)));
            if (Const.contactList.contains(listitem.get(i)))
            {
                viewHolder.chkContact.setChecked(true);
            }
            else
            {
                viewHolder.chkContact.setChecked(false);
            }
            viewHolder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b)
                {
                    if (b)
                    {
                        Const.contactList.add((String) listitem.get(i));
                        preference.setContactList("ContactList", Const.contactList);
                        return;
                    }
                    else
                    {
                        Const.contactList.remove((String) listitem.get(i));
                        preference.setContactList("ContactList", Const.contactList);
                    }
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listitem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {

            public CheckBox chkContact;

            public TextView txtCntFName;

            public TextView txtContact;

            public ViewHolder(View view)
            {
                super(view);
                chkContact = view.findViewById(R.id.chkContact);

                txtContact = view.findViewById(R.id.txtContact);
                txtCntFName = view.findViewById(R.id.txtCntFname);
                LinearLayout linearContacts = (LinearLayout) view.findViewById(R.id.linearContacts);
             /*   linearContacts.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!chkContact.isChecked()) {
                            chkContact.setChecked(true);
                            Const.contactList.add((String) listitem.get(getAdapterPosition()));
                            preference.setContactList("ContactList", Const.contactList);
                            return;
                        }
                        chkContact.setChecked(false);
                        Const.contactList.remove((String) listitem.get(getAdapterPosition()));
                        preference.setContactList("ContactList", Const.contactList);
                    }
                });*/
            }
        }
    }
}
