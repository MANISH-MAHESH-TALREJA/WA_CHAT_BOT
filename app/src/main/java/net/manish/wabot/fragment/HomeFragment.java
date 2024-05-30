package net.manish.wabot.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.manish.wabot.NotificationService;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.FragmentHomeBinding;
import net.manish.wabot.utilities.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements View.OnClickListener
{
    private AutoReplyTextAdapter adapter;
    private List<String> autoPrefList;
    private List<String> autoText;
    private Context context;

    public SharedPreference preference;
    FragmentHomeBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        FragmentHomeBinding inflate = FragmentHomeBinding.inflate(layoutInflater, viewGroup, false);
        myThis = inflate;

        return inflate.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle)
    {
        super.onViewCreated(view, bundle);
        context = getActivity();
        preference = new SharedPreference(requireActivity());
        Const.replyList = new ArrayList<>();
        Const.autoText = new ArrayList<>();
        autoText = new ArrayList<>();
        autoPrefList = new ArrayList<>();
        autoText = preference.getAutoTextList("AutoText");
        preference.addToPref_Boolean("autoReplyTextSwitch", true);
        try
        {
            if (autoText.isEmpty())
            {
                autoText = new ArrayList<>();
            }
        } catch (Exception e)
        {
            autoText = new ArrayList<>();
            Log.e("Auto Text ", Objects.requireNonNull(e.getMessage()));
        }
        myThis.msgSwitch.setChecked(preference.getFromPref_Boolean("CheckedState"));
        myThis.WASwitch.setChecked(preference.getFromPref_Boolean("WAState"));
        myThis.WBSwitch.setChecked(preference.getFromPref_Boolean("WBState"));
        myThis.msgSwitch.setChecked(preference.getFromPref_Boolean("CheckedState"));
        Log.e("msgSwitch Value", preference.getFromPref_Boolean("CheckedState") + ":::");
        myThis.msgSwitch.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (!b)
            {
                preference.addToPref_Boolean("CheckedState", false);
                requireActivity().stopService(new Intent(getActivity(), NotificationService.class));
            } else if (isNotificationServiceEnabled(requireActivity()))
            {
                preference.addToPref_Boolean("CheckedState", true);
                if (preference.getFromPref_Boolean("Once"))
                {
                    Const.userList.clear();
                    preference.setUserList("UserList", Const.userList);
                    Const.userList = new ArrayList<>();
                }
                if (preference.getFromPref_String("ReplyHeader").isEmpty())
                {
                    preference.addToPref_String("ReplyHeader", "Checked");
                }
                requireActivity().startService(new Intent(getActivity(), NotificationService.class));
            } else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Permission Required");
                builder.setMessage("This app requires Notification Access permissions to work.");
                builder.setPositiveButton("Ok", (dialogInterface, i) ->
                {
                    launchNotificationAccessSettings();
                    dialogInterface.dismiss();
                });
                builder.setNegativeButton("Cancel", (dialogInterface, i) ->
                {
                    dialogInterface.dismiss();
                    myThis.msgSwitch.setChecked(false);
                    preference.addToPref_Boolean("CheckedState", false);
                });
                builder.setCancelable(false);
                builder.show();
            }
        });

        boolean whatsappFound = isAppInstalled(context, "com.whatsapp");
        myThis.whatsClick.setOnClickListener(v ->
        {
            if (!whatsappFound)
            {
                Toast.makeText(context, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
            }
        });

        if (!whatsappFound)
        {
            myThis.WASwitch.setClickable(false);
        } else
        {
            myThis.WASwitch.setClickable(true);
        }
        myThis.WASwitch.setOnCheckedChangeListener((compoundButton, b) ->
        {

            if (!b)
            {
                preference.addToPref_Boolean("WAState", false);
            } else
            {
                preference.addToPref_Boolean("WAState", true);
            }
        });

        boolean whatsappFound1 = isAppInstalled(context, "com.whatsapp.w4b");


        myThis.busiClick.setOnClickListener(v ->
        {
            if (!whatsappFound1)
            {
                Toast.makeText(context, "WhatsApp Business not Installed", Toast.LENGTH_SHORT).show();
            }
        });

        if (!whatsappFound1)
        {
            myThis.WBSwitch.setClickable(false);
        } else
        {
            myThis.WBSwitch.setClickable(true);
        }
        myThis.WBSwitch.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (!b)
            {
                preference.addToPref_Boolean("WBState", false);
            } else
            {
                preference.addToPref_Boolean("WBState", true);
            }
        });
        if (!preference.getFromPref_String("autoReplyText").isEmpty())
        {
            myThis.txtAutoReply.setText(preference.getFromPref_String("autoReplyText"));
        } else
        {
            myThis.txtAutoReply.setText("Can't talk now.");
            preference.addToPref_String("autoReplyText", "Can't talk now.");
        }
        myThis.autoReplyTextSwitch.setOnCheckedChangeListener((compoundButton, b) ->
        {

            if (!b)
            {
                preference.addToPref_Boolean("autoReplyTextSwitch", false);
            } else if (!preference.getFromPref_Boolean("CheckedState"))
            {
                Toast.makeText(getActivity(), "Please On Auto Reply", Toast.LENGTH_SHORT).show();
                Log.e("Auto REply", "Text");
                myThis.autoReplyTextSwitch.setChecked(false);
            }
            else
            {
                System.out.println("INSIDE ELSE");
            }
        });
        myThis.imgEdit.setOnClickListener(this);
        myThis.imgMsgClear.setOnClickListener(this);
        AutoReplyText();
        autoPrefList.addAll(autoText);
    }


    public static boolean isAppInstalled(Context context, String packageName)
    {
        try
        {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    private void AutoReplyText()
    {
        Const.autoText.add("In a meeting, text you later.");
        Const.autoText.add("At work, text you later.");
        Const.autoText.add("At the movie, text you later.");
        Const.autoText.add("I am busy, talk to you later.");
        Const.autoText.add("I am driving, text you later.");
        Const.autoText.add("I am sleeping, text you later.");
        Const.autoText.add("Can't talk now.");
        autoPrefList.addAll(Const.autoText);
    }


    public void onResume()
    {
        super.onResume();
        if (preference.getFromPref_Boolean("autoReplyTextSwitch"))
        {
            myThis.autoReplyTextSwitch.setChecked(true);
            SharedPreference sharedPreference = preference;
            sharedPreference.addToPref_String("autoReplyText", sharedPreference.getFromPref_String("autoReplyText"));
        } else
        {
            myThis.autoReplyTextSwitch.setChecked(false);
        }
        Log.e("autoReplyTextSwitch", preference.getFromPref_Boolean("autoReplyTextSwitch") + ":::");
        adapter = new AutoReplyTextAdapter(getActivity(), autoPrefList);
        myThis.autoRecycleview.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myThis.autoRecycleview.setLayoutManager(linearLayoutManager);
    }

    public void onStart()
    {
        super.onStart();
    }

    public void onClick(View view)
    {
        int id = view.getId();
        if (id == R.id.imgEdit)
        {
            Dialog dialog = new Dialog(requireActivity());
            dialog.setContentView(R.layout.add_auto_reply_text_layout);
            Objects.requireNonNull(dialog.getWindow()).setLayout(-1, -2);
            EditText editText = dialog.findViewById(R.id.edAutoReplyText);
            editText.setText(myThis.txtAutoReply.getText().toString().trim());
            editText.setSelection(editText.getText().length());
            dialog.findViewById(R.id.imgClose).setOnClickListener(view1 ->
            {
                if (!editText.getText().toString().trim().isEmpty())
                {
                    editText.getText().clear();
                }
            });
            dialog.findViewById(R.id.btnSetMsg).setOnClickListener(view12 ->
            {
                if (editText.getText().toString().trim().isEmpty())
                {
                    editText.requestFocus();
                    editText.setError("Please Write a text");
                    return;
                }
                myThis.txtAutoReply.setText(editText.getText().toString().trim());
                preference.addToPref_String("autoReplyText", editText.getText().toString());
                autoText.add(editText.getText().toString());
                preference.setAutoTextList("AutoText", autoText);
                autoPrefList.add(editText.getText().toString());
                dialog.dismiss();
            });
            dialog.show();
        } else if (id == R.id.imgMsgClear)
        {
            autoText.clear();
            preference.setReplyList("AutoText", autoText);
            autoPrefList.clear();
            autoPrefList.addAll(Const.autoText);
            adapter.notifyDataSetChanged();
        }
    }


    public boolean isListenerEnabled(Context context2, Class cls)
    {
        ComponentName componentName = new ComponentName(context, cls);
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_notification_listeners");
        return string != null && string.contains(componentName.flattenToString());
    }

    public void launchNotificationAccessSettings()
    {
        enableService();
        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 100);
    }

    private void enableService()
    {
        requireActivity().getPackageManager().setComponentEnabledSetting(new ComponentName(requireActivity(), NotificationService.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public static boolean isNotificationServiceEnabled(Context context2)
    {
        String packageName = context2.getPackageName();
        String string = Settings.Secure.getString(context2.getContentResolver(), "enabled_notification_listeners");
        if (!TextUtils.isEmpty(string))
        {
            String[] split = string.split(":");
            for (String unflattenFromString : split)
            {
                ComponentName flattenFromString2 = ComponentName.unflattenFromString(unflattenFromString);
                if (flattenFromString2 != null && TextUtils.equals(packageName, flattenFromString2.getPackageName()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void onActivityResult(int i, int i2, Intent intent)
    {
        super.onActivityResult(i, i2, intent);
        if (i != 100)
        {
            return;
        }
        if (isListenerEnabled(getActivity(), NotificationService.class))
        {
            Toast.makeText(getActivity(), "permission Granted", Toast.LENGTH_SHORT).show();
            preference.addToPref_Boolean("CheckedState", true);
            return;
        }
        Toast.makeText(getActivity(), "permission Denied", Toast.LENGTH_SHORT).show();
        preference.addToPref_Boolean("CheckedState", false);
    }

    class AutoReplyTextAdapter extends RecyclerView.Adapter<AutoReplyTextAdapter.ViewHolder>
    {
        private final List<String> listItem;

        public AutoReplyTextAdapter(Context context, List<String> list)
        {
            listItem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.auto_reply_text_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i)
        {
            viewHolder.txtAutoMsg.setText(listItem.get(i));
            viewHolder.linearAutoText.setOnClickListener(view ->
            {
                myThis.txtAutoReply.setText(listItem.get(i));
                preference.addToPref_String("autoReplyText", listItem.get(i));
            });
        }


        @Override
        public int getItemCount()
        {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder
        {
            LinearLayout linearAutoText;
            TextView txtAutoMsg;

            public ViewHolder(View view)
            {
                super(view);
                txtAutoMsg = view.findViewById(R.id.txtAutoText);
                linearAutoText = view.findViewById(R.id.linearAutoText);
            }
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }
}
