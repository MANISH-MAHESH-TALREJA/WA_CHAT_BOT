package net.manish.wabot.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import net.manish.wabot.NotificationService;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.FragmentHomeBinding;
import net.manish.wabot.utilities.Const;
/*import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private String AutoSwitch;
    //private AdLoader adLoader;
    private AutoReplyTextAdapter adapter;
    private List<String> autoPrefList;
    private List<String> autoText;
    private Context context;
    
    //public UnifiedNativeAd nativeAd;
    private List<String> prefList;
    
    public SharedPreference preference;
    private String selectAutoText = null;
    FragmentHomeBinding thisb;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FragmentHomeBinding inflate = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false);
        thisb = inflate;

        return inflate.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        context = getActivity();
        preference = new SharedPreference(getActivity());
        Const.replyList = new ArrayList();
        Const.autoText = new ArrayList();
        autoText = new ArrayList();
        autoPrefList = new ArrayList();
        prefList = new ArrayList();
        autoText = preference.getAutoTextList("AutoText");
        preference.addToPref_Boolean("autoReplyTextSwitch", true);
        try {
            if (autoText.isEmpty()) {
                autoText = new ArrayList();
            }
        } catch (Exception e) {
            autoText = new ArrayList();
            Log.e("Auto Text ", e.getMessage());
        }
        thisb.msgSwitch.setChecked(preference.getFromPref_Boolean("CheckedState"));
        thisb.WASwitch.setChecked(preference.getFromPref_Boolean("WAState"));
        thisb.WBSwitch.setChecked(preference.getFromPref_Boolean("WBState"));
        thisb.msgSwitch.setChecked(preference.getFromPref_Boolean("CheckedState"));
        Log.e("msgSwitch Value", preference.getFromPref_Boolean("CheckedState") + ":::");
        thisb.msgSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    preference.addToPref_Boolean("CheckedState", false);
                    AutoSwitch = "unchecked";
                    getActivity().stopService(new Intent(getActivity(), NotificationService.class));
                } else if (isNotificationServiceEnabled(getActivity())) {
                    preference.addToPref_Boolean("CheckedState", true);
                    AutoSwitch = "checked";
                    if (preference.getFromPref_Boolean("Once")) {
                        Const.userList.clear();
                        preference.setUserList("UserList", Const.userList);
                        Const.userList = new ArrayList();
                    }
                    if (preference.getFromPref_String("ReplyHeader").equals("")) {
                        preference.addToPref_String("ReplyHeader", "Checked");
                    }
                    getActivity().startService(new Intent(getActivity(), NotificationService.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Permission Required");
                    builder.setMessage("This app requires Notification Access permissions to work.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            launchNotificationAccessSettings();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            thisb.msgSwitch.setChecked(false);
                            preference.addToPref_Boolean("CheckedState", false);
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            }
        });

        boolean whatsappFound = isAppInstalled(context, "com.whatsapp");
        thisb.whatsClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!whatsappFound){
                    Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(!whatsappFound){
            thisb.WASwitch.setClickable(false);
        }
        else{
            thisb.WASwitch.setClickable(true);
        }
        thisb.WASwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    preference.addToPref_Boolean("WAState", false);
                } else {
                    preference.addToPref_Boolean("WAState", true);
                }
            }
        });

        boolean whatsappFound1 = isAppInstalled(context, "com.whatsapp.w4b");


        thisb.busiClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!whatsappFound1){
                    Toast.makeText(context, "WhatsApp Business not Installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(!whatsappFound1){
            thisb.WBSwitch.setClickable(false);
        }
        else{
            thisb.WBSwitch.setClickable(true);
        }
        thisb.WBSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    preference.addToPref_Boolean("WBState", false);
                } else {
                    preference.addToPref_Boolean("WBState", true);
                }
            }
        });
        if (!preference.getFromPref_String("autoReplyText").equals("")) {
            thisb.txtAutoReply.setText(preference.getFromPref_String("autoReplyText"));
        } else {
            thisb.txtAutoReply.setText("Can't talk now.");
            preference.addToPref_String("autoReplyText", "Can't talk now.");
        }
        thisb.autoReplyTextSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (!b) {
                    preference.addToPref_Boolean("autoReplyTextSwitch", false);
                } else if (!preference.getFromPref_Boolean("CheckedState")) {
                    Toast.makeText(getActivity(), "Please On Auto Reply", Toast.LENGTH_SHORT).show();
                    Log.e("Auto REply", "Text");
                    thisb.autoReplyTextSwitch.setChecked(false);
                } else {

                }
            }
        });
        //TMAdsUtils.initAd(getContext());
        //TMAdsUtils.loadNativeAd(getContext(), thisb.myTemplate);
        thisb.imgEdit.setOnClickListener(this);
        thisb.imgMsgClear.setOnClickListener(this);
        AutoReplyText();
        autoPrefList.addAll(autoText);
        /*if (isAdded()) {
            new Handler().postDelayed(new Runnable() {
              @Override
                public void run() {
                    nativeAds();
                }
            }, 1000);
        }*/
    }


    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    private void AutoReplyText() {
        Const.autoText.add("In a meeting,text you later.");
        Const.autoText.add("At work, text you later.");
        Const.autoText.add("At the movie, text you later.");
        Const.autoText.add("I am busy, talk to you later.");
        Const.autoText.add("I am driving, text you later.");
        Const.autoText.add("I am sleeping,text you later.");
        Const.autoText.add("Can't talk now.");
        autoPrefList.addAll(Const.autoText);
    }

    
    /*public void nativeAds() {
        MobileAds.initialize((Context) getActivity(), (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        AdLoader.Builder builder = new AdLoader.Builder((Context) getActivity(), getResources().getString(R.string.admob_native_ad));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }

            }
        });
        builder.withAdListener(new AdListener() {
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                Log.e("Native Ad Failed", loadAdError + ":::");
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }*/

    


    public void onResume() {
        super.onResume();
        if (preference.getFromPref_Boolean("autoReplyTextSwitch")) {
            thisb.autoReplyTextSwitch.setChecked(true);
            SharedPreference sharedPreference = preference;
            sharedPreference.addToPref_String("autoReplyText", sharedPreference.getFromPref_String("autoReplyText"));
        } else {
            thisb.autoReplyTextSwitch.setChecked(false);
        }
        Log.e("autoReplyTextSwitch", preference.getFromPref_Boolean("autoReplyTextSwitch") + ":::");
        adapter = new AutoReplyTextAdapter(getActivity(), autoPrefList);
        thisb.autoRecycleview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        thisb.autoRecycleview.setLayoutManager(linearLayoutManager);
    }

    public void onStart() {
        super.onStart();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgEdit) {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.add_auto_reply_text_layout);
            dialog.getWindow().setLayout(-1, -2);
            EditText editText = (EditText) dialog.findViewById(R.id.edAutoReplyText);
            editText.setText(thisb.txtAutoReply.getText().toString().toString().trim());
            editText.setSelection(editText.getText().length());
            ((ImageView) dialog.findViewById(R.id.imgClose)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!editText.getText().toString().toString().trim().isEmpty()) {
                        editText.getText().clear();
                    }
                }
            });
            ((TextView) dialog.findViewById(R.id.btnSetMsg)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (editText.getText().toString().toString().trim().isEmpty()) {
                        editText.requestFocus();
                        editText.setError("Please Write a text");
                        return;
                    }
                    thisb.txtAutoReply.setText(editText.getText().toString().toString().trim());
                    preference.addToPref_String("autoReplyText", editText.getText().toString());
                    autoText.add(editText.getText().toString());
                    preference.setAutoTextList("AutoText", autoText);
                    autoPrefList.add(editText.getText().toString());
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (id == R.id.imgMsgClear) {
            autoText.clear();
            preference.setReplyList("AutoText", autoText);
            autoPrefList.clear();
            autoPrefList.addAll(Const.autoText);
            adapter.notifyDataSetChanged();
        }
    }


    public boolean isListenerEnabled(Context context2, Class cls) {
        ComponentName componentName = new ComponentName(context, cls);
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return string != null && string.contains(componentName.flattenToString());
    }

    public void launchNotificationAccessSettings() {
        enableService(true);
        int i = Build.VERSION.SDK_INT;
        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 100);
    }

    private void enableService(boolean z) {
        getActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(getActivity(), NotificationService.class), z ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    public static boolean isNotificationServiceEnabled(Context context2) {
        String packageName = context2.getPackageName();
        String string = Settings.Secure.getString(context2.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(string)) {
            String[] split = string.split(":");
            for (String unflattenFromString : split) {
                ComponentName unflattenFromString2 = ComponentName.unflattenFromString(unflattenFromString);
                if (unflattenFromString2 != null && TextUtils.equals(packageName, unflattenFromString2.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 100) {
            return;
        }
        if (isListenerEnabled(getActivity(), NotificationService.class)) {
            Toast.makeText(getActivity(), "permission Granted", Toast.LENGTH_SHORT).show();
            preference.addToPref_Boolean("CheckedState", true);
            return;
        }
        Toast.makeText(getActivity(), "permission Denied", Toast.LENGTH_SHORT).show();
        preference.addToPref_Boolean("CheckedState", false);
    }

    class AutoReplyTextAdapter extends RecyclerView.Adapter<AutoReplyTextAdapter.ViewHolder> {
        private Context context;
        private List<String> listItem;

        public AutoReplyTextAdapter(Context context2, List<String> list) {
            context = context2;
            listItem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.auto_reply_text_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
            viewHolder.txtAutoMsg.setText(listItem.get(i));
            viewHolder.linearAutoText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    thisb.txtAutoReply.setText(listItem.get(i));
                    preference.addToPref_String("autoReplyText", listItem.get(i));
                }
            });
        }



        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linearAutoText;
            TextView txtAutoMsg;

            public ViewHolder(View view) {
                super(view);
                txtAutoMsg = (TextView) view.findViewById(R.id.txtAutoText);
                linearAutoText = (LinearLayout) view.findViewById(R.id.linearAutoText);
            }
        }
    }


    @Override
    public void onDestroyView() {
        /*UnifiedNativeAd unifiedNativeAd = nativeAd;
        if (unifiedNativeAd != null) {
            unifiedNativeAd.destroy();
        }*/
        Log.e("Native ad", "Destroy in Home");
        super.onDestroyView();
    }
}
