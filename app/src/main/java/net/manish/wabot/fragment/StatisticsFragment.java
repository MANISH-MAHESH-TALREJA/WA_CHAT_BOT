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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.activity.ReplyMessageListActivity;
import net.manish.wabot.databinding.FragmentStatisticsBinding;
import net.manish.wabot.model.StatisticsReplyMsgListModel;
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

public class StatisticsFragment extends Fragment implements View.OnClickListener {
    int count = 0;
    private List<String> messageList;
    
    //public UnifiedNativeAd nativeAd;
    private List<String> personList;
    private SharedPreference preference;
    private List<StatisticsReplyMsgListModel> staticList;
    private List<StatisticsReplyMsgListModel> staticReplylList;
    FragmentStatisticsBinding thisb;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FragmentStatisticsBinding inflate = FragmentStatisticsBinding.inflate(layoutInflater, viewGroup, false);
        thisb = inflate;
        return inflate.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        preference = new SharedPreference(getActivity());
        Const.staticsReplyList = new ArrayList();
        staticList = new ArrayList();
        staticReplylList = new ArrayList();
        messageList = new ArrayList();
        personList = new ArrayList();
        long fromPref_Long = preference.getFromPref_Long("Counter");
        Log.e("Counter Value", fromPref_Long + ":::");
        thisb.txtCounter.setText(String.valueOf(fromPref_Long));
        thisb.imgReset.setOnClickListener(this);
        //TMAdsUtils.initAd(getContext());
        //TMAdsUtils.loadNativeAd(getContext(), thisb.myTemplate);
        staticReplylList = preference.getReplyList("StaticsReplyList");
        /*if (isAdded()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    nativeAds();
                }
            }, 1000);
        }*/
        int i = 0;
        if (staticReplylList==null){
            staticReplylList=new ArrayList<>();
        }
        while (i < staticReplylList.size()) {
            try {
                if (!messageList.contains(staticReplylList.get(i).getReplyMsg())) {
                    messageList.add(staticReplylList.get(i).getReplyMsg());
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Statics MESSAGE LIST", e.getMessage());
                staticReplylList = new ArrayList();
                return;
            }
        }
        for (int i2 = 0; i2 < messageList.size(); i2++) {
            for (int i3 = 0; i3 < staticReplylList.size(); i3++) {
                if (messageList.get(i2).equals(staticReplylList.get(i3).getReplyMsg())) {
                    count++;
                    if (!personList.contains(staticReplylList.get(i3).getPersonName())) {
                        personList.add(staticReplylList.get(i3).getPersonName());
                    }
                }
            }
            staticList.add(new StatisticsReplyMsgListModel(count, messageList.get(i2), String.valueOf(personList.size()), 0));
            count = 0;
            personList.clear();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgReset) {
            thisb.txtCounter.setText("0");
            preference.addToPref_Long("Counter", 0);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        try {

            if (staticList != null) {
                if (!staticList.isEmpty()) {
                    thisb.txtMessgesEmpty.setVisibility(View.GONE);
                    thisb.staticRecycleview.setVisibility(View.VISIBLE);
                    thisb.staticRecycleview.setAdapter(new StatisticsReplyMsgListAdapter(getActivity(), staticList));
                    return;
                }
            }
            staticList = new ArrayList();
            thisb.txtMessgesEmpty.setVisibility(View.VISIBLE);
            thisb.staticRecycleview.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("STATICS LIST", e.getMessage());
            staticList = new ArrayList();
        }
    }

    
    /*public void nativeAds() {
        MobileAds.initialize((Context) getActivity(), (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        AdLoader.Builder builder = new AdLoader.Builder((Context) getActivity(), getResources().getString(R.string.admob_native_ad));
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                if (nativeAd != null) {
                    nativeAd.destroy();
                }

            }
        });
        builder.withAdListener(new AdListener() {
          @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);

                Log.e("Native Ad Failed", loadAdError + ":::");
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }*/

    

@Override
    public void onDestroyView() {
        /*UnifiedNativeAd unifiedNativeAd = nativeAd;
        if (unifiedNativeAd != null) {
            unifiedNativeAd.destroy();
            Log.e("Native ad", "Destroy in statistic");
        }*/
        super.onDestroyView();
    }

    public class StatisticsReplyMsgListAdapter extends RecyclerView.Adapter<StatisticsReplyMsgListAdapter.ViewHolder> {
        private Context context;
        private List<StatisticsReplyMsgListModel> listItem;

        public StatisticsReplyMsgListAdapter(Context context2, List<StatisticsReplyMsgListModel> list) {
            context = context2;
            listItem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistics_reply_messages_list_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
            viewHolder.txtMessage.setText(listItem.get(i).getReplyMsg());
            viewHolder.txtMsgCount.setText(String.format("%02d", listItem.get(i).getI()));
            Log.e("message",listItem.get(i).getPersonName());
            viewHolder.txtPersonCount.setText(String.format("%02d", Integer.parseInt(listItem.get(i).getPersonName())));
            /*viewHolder.txtPersonCount.setText(listItem.get(i).getPersonName());*/
            viewHolder.linearDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ReplyMessageListActivity.class);
                    intent.putExtra("Message", listItem.get(i).getReplyMsg());
                    context.startActivity(intent);
                }
            });
        }


        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            
            public LinearLayout linearDetails;
            
            public TextView txtMessage;
            
            public TextView txtMsgCount;
            
            public TextView txtPersonCount;

            public ViewHolder(View view) {
                super(view);
                txtMessage = (TextView) view.findViewById(R.id.txtSendMsg);
                txtMsgCount = (TextView) view.findViewById(R.id.txtmsgCounter);
                txtPersonCount = (TextView) view.findViewById(R.id.txtMsgperson);
                linearDetails = (LinearLayout) view.findViewById(R.id.linearReplyDetail);
            }
        }
    }
}
