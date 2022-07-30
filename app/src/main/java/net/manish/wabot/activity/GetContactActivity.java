package net.manish.wabot.activity;

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
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityGetContactBinding;
import net.manish.wabot.utilities.Const;
import java.util.ArrayList;
import java.util.List;

public class GetContactActivity extends AppCompatActivity implements View.OnClickListener {
    
    public List<String> cntList;
    private Cursor cursor;
    
    public SharedPreference preference;
    
    public List<String> searchList;
    
    public int textLength = 0;
    ActivityGetContactBinding thisb;

    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        thisb = (ActivityGetContactBinding) DataBindingUtil.setContentView(this, R.layout.activity_get_contact);
        preference = new SharedPreference(this);
        cntList = new ArrayList();
        searchList = new ArrayList();
        getContactList();
        thisb.imgCntBack.setOnClickListener(this);
        thisb.imgCntSave.setOnClickListener(this);
        thisb.imgCntSearch.setOnClickListener(this);
        thisb.imgCntClose.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgCntBack:
            case R.id.imgCntSave:
                finish();
                return;
            case R.id.imgCntClose:
                thisb.edCntSearch.setText("");
                thisb.imgCntClose.setVisibility(View.GONE);
                getContactList();
                return;
            case R.id.imgCntSearch:
                thisb.txtCntSearch.setVisibility(View.GONE);
                thisb.imgCntSearch.setVisibility(View.GONE);
                thisb.edCntSearch.setVisibility(View.VISIBLE);
                thisb.edCntSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                        textLength = thisb.edCntSearch.getText().toString().length();
                        cntList.clear();
                        for (int i4 = 0; i4 < searchList.size(); i4++) {
                            if (textLength <= ((String) searchList.get(i4)).length() && ((String) searchList.get(i4)).toLowerCase().contains(thisb.edCntSearch.getText().toString().toLowerCase().trim())) {
                                cntList.add((String) searchList.get(i4));
                            }
                        }

                        AppendList(cntList);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (thisb.edCntSearch.getText().toString().length() > 0) {
                            thisb.imgCntClose.setVisibility(View.VISIBLE);
                        }
                    }
                });
                return;
            default:
                return;
        }
    }

    private void getContactList() {


       /*  cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, (String[]) null, (String) null, (String[]) null, "display_name ASC");
         cntList=new ArrayList<>();
        while (cursor.moveToNext()) {

        }
        cursor.close();*/

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, "display_name ASC");
        String temp_name="";
        while (phones.moveToNext())
        {
            String name=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            if (name.equals(temp_name))
                continue;
            temp_name=name;
            cntList.add(phones.getString(phones.getColumnIndex("display_name")));
            searchList.add(phones.getString(phones.getColumnIndex("display_name")));

            //add name to your list or adapter here`enter code here`
        }
        phones.close();
        thisb.getContactRecycleView.setAdapter(new GetContactAdapter(this, cntList));
    }

    public void AppendList(List<String> list) {
        thisb.getContactRecycleView.setAdapter(new GetContactAdapter(this, list));
    }

    class GetContactAdapter extends RecyclerView.Adapter<GetContactAdapter.ViewHolder> {
        private Context context;
        
        public List<String> listitem;

        public GetContactAdapter(Context context2, List<String> list) {
            context = context2;
            listitem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.get_contact_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtContact.setText(listitem.get(i));
            viewHolder.txtCntFName.setText(String.valueOf(listitem.get(i).charAt(0)));
            if (Const.contactList.contains(listitem.get(i))) {
                viewHolder.chkContact.setChecked(true);
            } else {
                viewHolder.chkContact.setChecked(false);
            }
            viewHolder.chkContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
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
        public int getItemCount() {
            return listitem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            
            public CheckBox chkContact;
            private LinearLayout linearContacts;
            
            public TextView txtCntFName;
            
            public TextView txtContact;

            public ViewHolder(View view) {
                super(view);
                chkContact = (CheckBox) view.findViewById(R.id.chkContact);

                txtContact = (TextView) view.findViewById(R.id.txtContact);
                txtCntFName = (TextView) view.findViewById(R.id.txtCntFname);
                linearContacts = (LinearLayout) view.findViewById(R.id.linearContacts);
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
