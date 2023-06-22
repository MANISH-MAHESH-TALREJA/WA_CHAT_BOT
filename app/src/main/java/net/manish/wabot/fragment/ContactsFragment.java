package net.manish.wabot.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.activity.GetContactActivity;
import net.manish.wabot.databinding.FragmentContactsBinding;
import net.manish.wabot.utilities.Const;
/*import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends Fragment implements View.OnClickListener {
    //private static final int GETCONTACT = 100;
    private boolean fullScreen = false;
    
    public SharedPreference preference;
    FragmentContactsBinding myThis;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        myThis = FragmentContactsBinding.inflate(layoutInflater, viewGroup, false);
        return myThis.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        preference = new SharedPreference(requireActivity());
        myThis.imgChooseCont.setOnClickListener(this);
        myThis.imgFullScreen.setOnClickListener(this);
        myThis.contactRdtGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbtnContactList:
                        ContactList();
                        preference.addToPref_String("ContactType", "ContactList");
                        myThis.imgChooseCont.setVisibility(View.VISIBLE);
                        myThis.imgFullScreen.setVisibility(View.VISIBLE);
                        return;
                    case R.id.rbtnEverone:
                        preference.addToPref_String("ContactType", "Everyone");
                        myThis.imgChooseCont.setVisibility(View.GONE);
                        myThis.imgFullScreen.setVisibility(View.GONE);
                        myThis.linearEmpty.setVisibility(View.VISIBLE);
                        myThis.contactRecycleView.setVisibility(View.GONE);
                        myThis.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_group));
                        myThis.txtEmpty.setText("Auto reply to everyone.");
                        return;
                    case R.id.rbtnExceptContList:
                        preference.addToPref_String("ContactType", "ExceptContList");
                        myThis.imgChooseCont.setVisibility(View.VISIBLE);
                        myThis.imgFullScreen.setVisibility(View.VISIBLE);
                        ContactList();
                        return;
                    case R.id.rbtnExceptPhoneCont:
                        if (getActivity().checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED) {
                            preference.addToPref_String("ContactType", "ExceptPhoneList");
                            myThis.imgChooseCont.setVisibility(View.GONE);
                            myThis.imgFullScreen.setVisibility(View.GONE);
                            myThis.linearEmpty.setVisibility(View.VISIBLE);
                            myThis.contactRecycleView.setVisibility(View.GONE);
                            myThis.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact_page));
                            myThis.txtEmpty.setText("Auto reply to everyone except your phone contacts.");
                            return;
                        }
                        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_CONTACTS"}, 100);
                        return;
                    default:
                        return;
                }
            }
        });
        //bannerAd();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id != R.id.imgChooseCont) {
            if (id == R.id.imgFullScreen) {
                if (!fullScreen) {
                    myThis.linearRadioBtn.setVisibility(View.GONE);
                    fullScreen = true;
                    myThis.imgFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    return;
                }
                myThis.linearRadioBtn.setVisibility(View.VISIBLE);
                fullScreen = false;
                myThis.imgFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
            }
        } else if (Build.VERSION.SDK_INT < 23) {
        } else {
            if (getActivity().checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getActivity(), GetContactActivity.class));
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.READ_CONTACTS"}, 100);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Const.contactList = preference.getContactList("ContactList");
        if (preference.getFromPref_String("ContactType").equals("ContactList") || preference.getFromPref_String("ContactType").equals("ExceptContList")) {
            ContactList();
        }
        String fromPref_String = preference.getFromPref_String("ContactType");
        if (fromPref_String.equals("EveryOne")) {
            myThis.imgChooseCont.setVisibility(View.GONE);
            myThis.rbtnEverone.setChecked(true);
        } else if (fromPref_String.equals("ContactList")) {
            myThis.imgChooseCont.setVisibility(View.VISIBLE);
            myThis.rbtnContactList.setChecked(true);
        } else if (fromPref_String.equals("ExceptContList")) {
            myThis.imgChooseCont.setVisibility(View.VISIBLE);
            myThis.rbtnExceptContList.setChecked(true);
        } else if (fromPref_String.equals("ExceptPhoneList")) {
            myThis.imgChooseCont.setVisibility(View.GONE);
            myThis.rbtnExceptPhoneCont.setChecked(true);
        } else {
            myThis.imgChooseCont.setVisibility(View.GONE);
            myThis.rbtnEverone.setChecked(true);
        }
    }

    
    public void ContactList() {
        try {
            if (Const.contactList.isEmpty()) {
                Const.contactList = new ArrayList();
                myThis.linearEmpty.setVisibility(View.VISIBLE);
                myThis.contactRecycleView.setVisibility(View.GONE);
                myThis.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_contacts));
                myThis.txtEmpty.setText("Add one or more contact to the list.");
                return;
            }
            myThis.linearEmpty.setVisibility(View.GONE);
            myThis.contactRecycleView.setVisibility(View.VISIBLE);
            myThis.contactRecycleView.setAdapter(new ContactListAdapter(getActivity(), Const.contactList));
        } catch (Exception e) {
            Const.contactList = new ArrayList();
            Log.e("Contact List", e.getMessage());
        }
    }

    /*private void bannerAd() {
        MobileAds.initialize((Context) getActivity(), (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        myThis.bannerAd.loadAd(new AdRequest.Builder().build());
    }*/

    class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
        private final List<String> listItem;

        public ContactListAdapter(Context context2, List<String> list) {
            listItem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contactlist_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
            viewHolder.txtFirstLater.setText(String.valueOf(listItem.get(i).charAt(0)));
            viewHolder.txtConctName.setText(listItem.get(i));
            viewHolder.imgClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listItem.remove(i);
                    preference.setContactList("ContactList", listItem);
                    notifyDataSetChanged();
                }
            });
        }


        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            
            public TextView imgClose;
            
            public TextView txtConctName;
            
            public TextView txtFirstLater;

            public ViewHolder(View view) {
                super(view);
                txtFirstLater = (TextView) view.findViewById(R.id.txtFirstLater);
                txtConctName = (TextView) view.findViewById(R.id.txtConctName);
                imgClose = (TextView) view.findViewById(R.id.imgConctClose);
            }
        }
    }
}
