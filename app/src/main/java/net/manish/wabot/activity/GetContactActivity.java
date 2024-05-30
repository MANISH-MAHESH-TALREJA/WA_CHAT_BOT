package net.manish.wabot.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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

    public SharedPreference preference;

    public List<String> searchList;

    public int textLength = 0;
    ActivityGetContactBinding myThis;


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_get_contact);
        preference = new SharedPreference(this);
        cntList = new ArrayList<>();
        searchList = new ArrayList<>();
        getContactList();
        myThis.imgCntBack.setOnClickListener(this);
        myThis.imgCntSave.setOnClickListener(this);
        myThis.imgCntSearch.setOnClickListener(this);
        myThis.imgCntClose.setOnClickListener(this);
    }

    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.imgCntBack || id == R.id.imgCntSave)
        {
            finish();
        }
        else if (id == R.id.imgCntClose)
        {
            myThis.edCntSearch.setText("");
            myThis.imgCntClose.setVisibility(View.GONE);
            getContactList();
        }
        else if (id == R.id.imgCntSearch)
        {
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
                        if (textLength <= searchList.get(i4).length() && searchList.get(i4).toLowerCase().contains(myThis.edCntSearch.getText().toString().toLowerCase().trim()))
                        {
                            cntList.add(searchList.get(i4));
                        }
                    }

                    AppendList(cntList);
                }

                @Override
                public void afterTextChanged(Editable editable)
                {
                    if (!myThis.edCntSearch.getText().toString().isEmpty())
                    {
                        myThis.imgCntClose.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    @SuppressLint("Range")
    private void getContactList()
    {

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, "display_name ASC");
        String temp_name = "";
        while (true)
        {
            assert phones != null;
            if (!phones.moveToNext()) break;
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(temp_name))
                continue;
            temp_name = name;
            cntList.add(phones.getString(phones.getColumnIndex("display_name")));
            searchList.add(phones.getString(phones.getColumnIndex("display_name")));

        }
        phones.close();
        myThis.getContactRecycleView.setAdapter(new GetContactAdapter(cntList));
    }

    public void AppendList(List<String> list)
    {
        myThis.getContactRecycleView.setAdapter(new GetContactAdapter(list));
    }

    class GetContactAdapter extends RecyclerView.Adapter<GetContactAdapter.ViewHolder>
    {

        public List<String> listItem;

        public GetContactAdapter(List<String> list)
        {
            listItem = list;
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
            viewHolder.txtContact.setText(listItem.get(i));
            viewHolder.txtCntFName.setText(String.valueOf(listItem.get(i).charAt(0)));
            viewHolder.chkContact.setChecked(Const.contactList.contains(listItem.get(i)));
            viewHolder.chkContact.setOnCheckedChangeListener((compoundButton, b) ->
            {
                if (b)
                {
                    Const.contactList.add(listItem.get(i));
                    preference.setContactList("ContactList", Const.contactList);
                }
                else
                {
                    Const.contactList.remove(listItem.get(i));
                    preference.setContactList("ContactList", Const.contactList);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return listItem.size();
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

            }
        }
    }
}
