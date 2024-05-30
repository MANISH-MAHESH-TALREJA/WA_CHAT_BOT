package net.manish.wabot.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.activity.ReplyMessageListActivity;
import net.manish.wabot.databinding.FragmentStatisticsBinding;
import net.manish.wabot.model.StatisticsReplyMsgListModel;
import net.manish.wabot.utilities.Const;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StatisticsFragment extends Fragment implements View.OnClickListener
{
    int count = 0;

    private SharedPreference preference;
    private List<StatisticsReplyMsgListModel> staticList;
    FragmentStatisticsBinding myThis;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle)
    {
        FragmentStatisticsBinding inflate = FragmentStatisticsBinding.inflate(layoutInflater, viewGroup, false);
        myThis = inflate;
        return inflate.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle bundle)
    {
        super.onViewCreated(view, bundle);
        preference = new SharedPreference(requireActivity());
        Const.staticsReplyList = new ArrayList<>();
        staticList = new ArrayList<>();
        List<StatisticsReplyMsgListModel> staticReplyList;
        List<String> messageList = new ArrayList<>();
        List<String> personList = new ArrayList<>();
        long fromPref_Long = preference.getFromPref_Long("Counter");
        Log.e("Counter Value", fromPref_Long + ":::");
        myThis.txtCounter.setText(String.valueOf(fromPref_Long));
        myThis.imgReset.setOnClickListener(this);
        staticReplyList = preference.getReplyList("StaticsReplyList");
        int i = 0;
        if (staticReplyList == null)
        {
            staticReplyList = new ArrayList<>();
        }
        while (i < staticReplyList.size())
        {
            try
            {
                if (!messageList.contains(staticReplyList.get(i).getReplyMsg()))
                {
                    messageList.add(staticReplyList.get(i).getReplyMsg());
                }
                i++;
            } catch (Exception e)
            {
                Log.e("Statics MESSAGE LIST", Objects.requireNonNull(e.getMessage()));
                return;
            }
        }
        for (int i2 = 0; i2 < messageList.size(); i2++)
        {
            for (int i3 = 0; i3 < staticReplyList.size(); i3++)
            {
                if (messageList.get(i2).equals(staticReplyList.get(i3).getReplyMsg()))
                {
                    count++;
                    if (!personList.contains(staticReplyList.get(i3).getPersonName()))
                    {
                        personList.add(staticReplyList.get(i3).getPersonName());
                    }
                }
            }
            staticList.add(new StatisticsReplyMsgListModel(count, messageList.get(i2), String.valueOf(personList.size()), 0));
            count = 0;
            personList.clear();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onClick(View view)
    {
        if (view.getId() == R.id.imgReset)
        {
            myThis.txtCounter.setText("0");
            preference.addToPref_Long("Counter", 0);
        }
    }


    @Override
    public void onResume()
    {
        super.onResume();
        try
        {

            if (staticList != null)
            {
                if (!staticList.isEmpty())
                {
                    myThis.txtMessgesEmpty.setVisibility(View.GONE);
                    myThis.staticRecycleview.setVisibility(View.VISIBLE);
                    myThis.staticRecycleview.setAdapter(new StatisticsReplyMsgListAdapter(getActivity(), staticList));
                    return;
                }
            }
            staticList = new ArrayList<>();
            myThis.txtMessgesEmpty.setVisibility(View.VISIBLE);
            myThis.staticRecycleview.setVisibility(View.GONE);
        } catch (Exception e)
        {
            Log.e("STATICS LIST", Objects.requireNonNull(e.getMessage()));
            staticList = new ArrayList<>();
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    public static class StatisticsReplyMsgListAdapter extends RecyclerView.Adapter<StatisticsReplyMsgListAdapter.ViewHolder>
    {
        private final Context context;
        private final List<StatisticsReplyMsgListModel> listItem;

        public StatisticsReplyMsgListAdapter(Context context2, List<StatisticsReplyMsgListModel> list)
        {
            context = context2;
            listItem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
        {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistics_reply_messages_list_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i)
        {
            viewHolder.txtMessage.setText(listItem.get(i).getReplyMsg());
            viewHolder.txtMsgCount.setText(String.format("%02d", listItem.get(i).getI()));
            Log.e("message", listItem.get(i).getPersonName());
            viewHolder.txtPersonCount.setText(String.format("%02d", Integer.parseInt(listItem.get(i).getPersonName())));
            viewHolder.linearDetails.setOnClickListener(view ->
            {
                Intent intent = new Intent(context, ReplyMessageListActivity.class);
                intent.putExtra("Message", listItem.get(i).getReplyMsg());
                context.startActivity(intent);
            });
        }


        @Override
        public int getItemCount()
        {
            return listItem.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder
        {

            public LinearLayout linearDetails;

            public TextView txtMessage;

            public TextView txtMsgCount;

            public TextView txtPersonCount;

            public ViewHolder(View view)
            {
                super(view);
                txtMessage = view.findViewById(R.id.txtSendMsg);
                txtMsgCount = view.findViewById(R.id.txtmsgCounter);
                txtPersonCount = view.findViewById(R.id.txtMsgperson);
                linearDetails = view.findViewById(R.id.linearReplyDetail);
            }
        }
    }
}
