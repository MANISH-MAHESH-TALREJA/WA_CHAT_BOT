package net.manish.wabot.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.activity.AddCustomReplyMessageActivity;
import net.manish.wabot.model.AutoReply;

import java.util.List;

public class AutoReplyAdapter extends RecyclerView.Adapter<AutoReplyAdapter.ViewHolder>
{
    public Context context;
    public List<AutoReply> listItem;
    private SharedPreference preference;


    public AutoReplyAdapter(Context context2, List<AutoReply> list)
    {
        context = context2;
        listItem = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.add_message_design_layout, viewGroup, false);
        preference = new SharedPreference(context);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewholder, @SuppressLint("RecyclerView") int i)
    {
        viewholder.ReceiveMsg.setText(listItem.get(i).getReceiveMsg());
        viewholder.SendMsg.setText(listItem.get(i).getSendMsg());
        viewholder.messageCardView.setOnClickListener(view ->
        {
            Intent intent = new Intent(context, AddCustomReplyMessageActivity.class);
            intent.putExtra("ReceiveMessage", listItem.get(i).getReceiveMsg());
            intent.putExtra("SendMessage", listItem.get(i).getSendMsg());
            intent.putExtra("ActivityValue", "Update");
            context.startActivity(intent);
        });

        viewholder.imgEdit.setOnClickListener(view ->
        {
            Intent intent = new Intent(context, AddCustomReplyMessageActivity.class);
            intent.putExtra("ReceiveMessage", listItem.get(i).getReceiveMsg());
            intent.putExtra("SendMessage", listItem.get(i).getSendMsg());
            intent.putExtra("ActivityValue", "Update");
            context.startActivity(intent);
        });

        viewholder.imgDelete.setOnClickListener(view -> removeItem(i));
    }


    @Override
    public int getItemCount()
    {
        return listItem.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView ReceiveMsg;
        public TextView SendMsg;
        public LinearLayout messageCardView;
        public ImageView imgEdit, imgDelete;

        public ViewHolder(View view)
        {
            super(view);
            ReceiveMsg = view.findViewById(R.id.txtIncomingMessage);
            SendMsg = view.findViewById(R.id.txtReplyMessage);
            imgEdit = view.findViewById(R.id.imgEdit);
            imgDelete = view.findViewById(R.id.imgDelete);
            messageCardView = view.findViewById(R.id.linearUpdate);
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void removeItem(int i)
    {
        listItem.remove(i);
        preference.setList("MessageList", listItem);
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void restoreItem(AutoReply autoReply, int i)
    {
        listItem.add(i, autoReply);
        preference.setList("MessageList", listItem);
        notifyDataSetChanged();
    }

    public List<AutoReply> getData()
    {
        return listItem;
    }
}
