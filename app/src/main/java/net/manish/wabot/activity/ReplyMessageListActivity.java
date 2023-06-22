package net.manish.wabot.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityReplyMessageListBinding;
import net.manish.wabot.model.ReplyMessageListModel;
import net.manish.wabot.model.StatisticsReplyMsgListModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReplyMessageListActivity extends AppCompatActivity {
    ActivityReplyMessageListBinding myThis;

    
    @Override
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        myThis = (ActivityReplyMessageListBinding) DataBindingUtil.setContentView(this, R.layout.activity_reply_message_list);
        SharedPreference preference = new SharedPreference(this);
        List<ReplyMessageListModel> replyMsgList = new ArrayList();
        List<StatisticsReplyMsgListModel> staticsList = new ArrayList();
        List<String> timeList = new ArrayList();
        String message = getIntent().getStringExtra("Message");
        myThis.txtReplyMsg.setText(message);
        myThis.imgListBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        try {
            staticsList = preference.getReplyList("StaticsReplyList");
            Log.e("StaticsList Size", staticsList.size() + ":::");
            for (int size = staticsList.size() + -1; size >= 0; size--) {
                if (message.equals(staticsList.get(size).getReplyMsg())) {
                    Date date = new Date(staticsList.get(size).getTime());
                    String format = new SimpleDateFormat("EEE, MMM d, yyyy").format(date);
                    String format2 = new SimpleDateFormat("h:mm a").format(date);
                    if (!timeList.contains(format)) {
                        timeList.add(format);
                        str = "False";
                    } else {
                        str = "True";
                    }
                    replyMsgList.add(new ReplyMessageListModel(staticsList.get(size).getPersonName(), format, format2, str));
                }
            }
            myThis.msgListRecyclerView.setAdapter(new ReplyMessageListAdapter(this, replyMsgList));
        } catch (Exception e) {
            Log.e("Reply List", e.getMessage());
        }
    }


    public class ReplyMessageListAdapter extends RecyclerView.Adapter<ReplyMessageListAdapter.ViewHolder> {
        private final List<ReplyMessageListModel> listItem;

        public ReplyMessageListAdapter(Context context2, List<ReplyMessageListModel> list) {
            listItem = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reply_messsage_list_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.txtFiLetter.setText(String.valueOf(listItem.get(i).getName().charAt(0)));
            viewHolder.txtName.setText(listItem.get(i).getName());
            viewHolder.txtTime.setText(listItem.get(i).getTime());
            if (listItem.get(i).getSameDate().equals("True")) {
                viewHolder.linearDay.setVisibility(View.GONE);
                return;
            }
            viewHolder.linearDay.setVisibility(View.VISIBLE);
            if (listItem.get(i).getDate().equals(new SimpleDateFormat("EEE, MMM d, yyyy").format(new Date()))) {
                viewHolder.txtMsgDay.setText("Today");
            } else {
                viewHolder.txtMsgDay.setText(listItem.get(i).getDate());
            }
        }


        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout linearDay;
            public TextView txtFiLetter;
            public TextView txtMsgDay;
            public TextView txtName;
            public TextView txtTime;
            public ViewHolder(View view) {
                super(view);
                txtName = (TextView) view.findViewById(R.id.txtUserName);
                txtTime = (TextView) view.findViewById(R.id.txtTime);
                txtFiLetter = (TextView) view.findViewById(R.id.txtUserfLetter);
                txtMsgDay = (TextView) view.findViewById(R.id.txtMsgDay);
                linearDay = (LinearLayout) view.findViewById(R.id.linearDay);
            }
        }
    }
}
