package net.manish.wabot.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.InputDeviceCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.SwipeToDeleteCallBack;
import net.manish.wabot.activity.AddCustomReplyMessageActivity;
import net.manish.wabot.adapter.AutoReplyAdapter;
import net.manish.wabot.databinding.FragmentCustomReplyBinding;
import net.manish.wabot.model.AutoReply;
import net.manish.wabot.utilities.Const;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

public class CustomReplyFragment extends Fragment implements View.OnClickListener
{

    public AutoReplyAdapter adapter;
    private SharedPreference preference;
    FragmentCustomReplyBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        FragmentCustomReplyBinding inflate = FragmentCustomReplyBinding.inflate(layoutInflater, viewGroup, false);
        myThis = inflate;
        return inflate.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle)
    {
        super.onViewCreated(view, bundle);
        preference = new SharedPreference(getActivity());
        myThis.btnAdd.setOnClickListener(this);
        myThis.msgCustomSwitch.setChecked(preference.getFromPref_Boolean("customAutoReplySwitch"));
        Log.e("msgCustomSwitch Value", preference.getFromPref_Boolean("customAutoReplySwitch") + ":::");
        myThis.msgCustomSwitch.setOnCheckedChangeListener((compoundButton, b) ->
        {
            if (!b)
            {
                preference.addToPref_Boolean("customAutoReplySwitch", false);
            } else if (!preference.getFromPref_Boolean("CheckedState"))
            {
                Toast.makeText(getActivity(), "Please On Auto Reply", Toast.LENGTH_SHORT).show();
                Log.e("Custom Reply", "Text");
                myThis.msgCustomSwitch.setChecked(false);
            } else
            {
                preference.addToPref_Boolean("customAutoReplySwitch", true);
            }
        });
    }


    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.btnAdd)
        {
            Intent intent = new Intent(getActivity(), AddCustomReplyMessageActivity.class);
            intent.putExtra("ActivityValue", "Add");
            startActivity(intent);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Const.replyList = preference.getList("MessageList");
        try
        {
            if (Const.replyList != null)
            {
                if (!Const.replyList.isEmpty())
                {
                    myThis.txtEmpty.setVisibility(View.GONE);
                    myThis.linearList.setVisibility(View.VISIBLE);

                    adapter = new AutoReplyAdapter(getActivity(), Const.replyList);
                    myThis.customRecycleview.setAdapter(adapter);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    myThis.customRecycleview.setLayoutManager(linearLayoutManager);
                    enableSwipeToDeleteAndUndo();
                }
            } else
            {
                Const.replyList = new ArrayList<>();
                myThis.txtEmpty.setVisibility(View.VISIBLE);
                myThis.linearList.setVisibility(View.GONE);

            }
        } catch (Exception e)
        {
            Log.e("LIST EMPTY", Objects.requireNonNull(e.getMessage()));
            Const.replyList = new ArrayList<>();
        }
        enableSwipeToDeleteAndUndo();
    }


    private void enableSwipeToDeleteAndUndo()
    {
        new ItemTouchHelper(new SwipeToDeleteCallBack(getActivity())
        {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i)
            {
                super.onSwiped(viewHolder, i);
                final int adapterPosition = viewHolder.getAdapterPosition();
                final AutoReply autoReply = adapter.getData().get(adapterPosition);
                adapter.removeItem(adapterPosition);
                adapter.notifyDataSetChanged();
                Snackbar make = Snackbar.make(myThis.getRoot(), "Item was removed from the list.", 0);
                make.setAction("UNDO", view ->
                {
                    adapter.restoreItem(autoReply, adapterPosition);
                    myThis.customRecycleview.scrollToPosition(adapterPosition);
                    adapter.notifyDataSetChanged();
                });
                make.setActionTextColor(InputDeviceCompat.SOURCE_ANY);
                make.show();
            }
        }).attachToRecyclerView(myThis.customRecycleview);
    }
}
