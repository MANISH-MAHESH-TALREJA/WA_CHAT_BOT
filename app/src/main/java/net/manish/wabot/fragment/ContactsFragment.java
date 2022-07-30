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
    private static int GETCONTACT = 100;
    private boolean fullScreen = false;
    
    public SharedPreference preference;
    FragmentContactsBinding thisb;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        thisb = FragmentContactsBinding.inflate(layoutInflater, viewGroup, false);
        return thisb.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        preference = new SharedPreference(getActivity());
        thisb.imgChooseCont.setOnClickListener(this);
        thisb.imgFullScreen.setOnClickListener(this);
        thisb.contactRdtGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rbtnContactList:
                        ContactList();
                        preference.addToPref_String("ContactType", "ContactList");
                        thisb.imgChooseCont.setVisibility(View.VISIBLE);
                        thisb.imgFullScreen.setVisibility(View.VISIBLE);
                        return;
                    case R.id.rbtnEverone:
                        preference.addToPref_String("ContactType", "Everyone");
                        thisb.imgChooseCont.setVisibility(View.GONE);
                        thisb.imgFullScreen.setVisibility(View.GONE);
                        thisb.linearEmpty.setVisibility(View.VISIBLE);
                        thisb.contactRecycleView.setVisibility(View.GONE);
                        thisb.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_group));
                        thisb.txtEmpty.setText("Auto reply to everyone.");
                        return;
                    case R.id.rbtnExceptContList:
                        preference.addToPref_String("ContactType", "ExceptContList");
                        thisb.imgChooseCont.setVisibility(View.VISIBLE);
                        thisb.imgFullScreen.setVisibility(View.VISIBLE);
                        ContactList();
                        return;
                    case R.id.rbtnExceptPhoneCont:
                        if (Build.VERSION.SDK_INT < 23) {
                            return;
                        }
                        if (getActivity().checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED) {
                            preference.addToPref_String("ContactType", "ExceptPhoneList");
                            thisb.imgChooseCont.setVisibility(View.GONE);
                            thisb.imgFullScreen.setVisibility(View.GONE);
                            thisb.linearEmpty.setVisibility(View.VISIBLE);
                            thisb.contactRecycleView.setVisibility(View.GONE);
                            thisb.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_contact_page));
                            thisb.txtEmpty.setText("Auto reply to everyone except your phone contacts.");
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
                    thisb.linearRadioBtn.setVisibility(View.GONE);
                    fullScreen = true;
                    thisb.imgFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen_exit));
                    return;
                }
                thisb.linearRadioBtn.setVisibility(View.VISIBLE);
                fullScreen = false;
                thisb.imgFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
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
            thisb.imgChooseCont.setVisibility(View.GONE);
            thisb.rbtnEverone.setChecked(true);
        } else if (fromPref_String.equals("ContactList")) {
            thisb.imgChooseCont.setVisibility(View.VISIBLE);
            thisb.rbtnContactList.setChecked(true);
        } else if (fromPref_String.equals("ExceptContList")) {
            thisb.imgChooseCont.setVisibility(View.VISIBLE);
            thisb.rbtnExceptContList.setChecked(true);
        } else if (fromPref_String.equals("ExceptPhoneList")) {
            thisb.imgChooseCont.setVisibility(View.GONE);
            thisb.rbtnExceptPhoneCont.setChecked(true);
        } else {
            thisb.imgChooseCont.setVisibility(View.GONE);
            thisb.rbtnEverone.setChecked(true);
        }
    }

    
    public void ContactList() {
        try {
            if (Const.contactList.isEmpty()) {
                Const.contactList = new ArrayList();
                thisb.linearEmpty.setVisibility(View.VISIBLE);
                thisb.contactRecycleView.setVisibility(View.GONE);
                thisb.imgEmpty.setImageDrawable(getResources().getDrawable(R.drawable.ic_contacts));
                thisb.txtEmpty.setText("Add one or more contact to the list.");
                return;
            }
            thisb.linearEmpty.setVisibility(View.GONE);
            thisb.contactRecycleView.setVisibility(View.VISIBLE);
            thisb.contactRecycleView.setAdapter(new ContactListAdapter(getActivity(), Const.contactList));
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
        thisb.bannerAd.loadAd(new AdRequest.Builder().build());
    }*/

    class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
        private Context context;
        private List<String> listItem;

        public ContactListAdapter(Context context2, List<String> list) {
            context = context2;
            listItem = list;
        }

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
