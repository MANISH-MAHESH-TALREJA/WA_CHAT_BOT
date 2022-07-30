package net.manish.wabot.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivitySendDirectMessageBinding;
import net.manish.wabot.model.PhoneNumberModel;
import net.manish.wabot.utilities.Const;
/*import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;*/
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendDirectMessageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1000;
    private static int DELIMAGE = 100;
    private static final int REQUEST_ID_PERMISSIONS = 100;
    private static final int SELECT_PICTURE = 1;
    private static String currentPhotoPath;
    private File file;
    //public UnifiedNativeAd nativeAd;
    private List<PhoneNumberModel> numberList;
    private String phonenumber;
    public Uri photoURI;
    private SharedPreference preference;
    public Uri shareUri = Uri.EMPTY;
    public int chatType = 1;
    ActivitySendDirectMessageBinding thisb;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        thisb = (ActivitySendDirectMessageBinding) DataBindingUtil.setContentView(this, R.layout.activity_send_direct_message);
        preference = new SharedPreference(this);
        numberList = new ArrayList();
        //nativeAds();
        thisb.imgDirectBack.setOnClickListener(this);
        thisb.imgChoose.setOnClickListener(this);
        thisb.btnSendMsg.setOnClickListener(this);
        numberList= preference.getNumberList("NumberList");
        try {
            if (numberList.isEmpty()) {
                numberList = new ArrayList();
            }
        } catch (Exception e) {
            Log.e("Number List", e.getMessage());
            numberList = new ArrayList();
        }
        thisb.phoneNumberRecycleview.setAdapter(new PhoneNumberAdapter(this, numberList));
        thisb.directChatRadioGroup.check(R.id.rb_direct_chat_whatsapp);
        thisb.directChatRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                chatType = ((RadioButton) thisb.directChatRadioGroup.findViewById(thisb.directChatRadioGroup.getCheckedRadioButtonId())).getId() == R.id.rb_direct_chat_whatsapp ? 1 : 2;
            }
        });
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imgDirectBack) {
                finish();
        }
        if (id== R.id.btnSendMsg){
            String obj =  thisb.edMessage.getText().toString().toString();
            String obj2 = thisb.edPhoneNumber.getText().toString();
            String str =thisb.countryCode.getSelectedCountryCode() + obj2;
            if (obj.length() == 0) {
                Toast.makeText(SendDirectMessageActivity.this, "Please enter message", Toast.LENGTH_SHORT).show();
            } else if (obj2.length() == 0) {
                thisb.edPhoneNumber.setError("Please enter phone number");
                thisb.edPhoneNumber.requestFocus();
            } else if (obj2.length() < 7 || obj.length() <= 0) {
                thisb.edPhoneNumber.setError("Please write correct phone number");
                thisb.edPhoneNumber.requestFocus();
            } else {
                try {
                    PackageManager packageManager = SendDirectMessageActivity.this.getPackageManager();
                    Intent intent = new Intent("android.intent.action.VIEW");

                    try {
                        intent.setPackage(chatType == 1 ? "com.whatsapp" : "com.whatsapp.w4b");
                        intent.setData(Uri.parse("https://api.whatsapp.com/send?phone=" + str + "&text=" + obj));
                        if (intent.resolveActivity(packageManager) != null) {
                            SendDirectMessageActivity.this.startActivity(intent);
                        }
                        else{
                            Toast.makeText(this, chatType == 1 ? "Whatsapp" : "Whatsapp Business"+" Not install", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e2) {
                    Toast.makeText(SendDirectMessageActivity.this, "Error/n" + e2.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        }

        @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == 1 && intent != null) {
            Uri data = intent.getData();
            thisb.txtNoImage.setVisibility(View.GONE);
            thisb.imgSetImage.setVisibility(View.VISIBLE);
            thisb.imgSetImage.setImageURI(data);
            shareUri = data;
        } else if (i == 1000) {
            thisb.txtNoImage.setVisibility(View.GONE);
            thisb.imgSetImage.setVisibility(View.VISIBLE);
            thisb.imgSetImage.setImageURI(photoURI);
            shareUri = photoURI;
        }
    }

    private void chooseImage(Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.choose_image_layout);
            ((LinearLayout) dialog.findViewById(R.id.layourGallery)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    dialog.dismiss();
                }
            });
            ((LinearLayout) dialog.findViewById(R.id.layoutCamera)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        File file2 = null;
                        try {
                            file2 = createImageFile();
                        } catch (IOException e) {
                            e.getMessage();
                            Log.e("Dir Error", e.getMessage());
                        }
                        if (file2 != null) {
                            Uri uriForFile = FileProvider.getUriForFile(context, "com.akweb.whatsautoreplybuzz.provider", file2);
                            photoURI = uriForFile;
                            intent.putExtra("output", uriForFile);
                            startActivityForResult(intent, 1000);
                            dialog.dismiss();
                        }
                    }
                }
            });
            dialog.show();
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"}, 100);
    }



    private File createImageFile() throws IOException {
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File createTempFile = File.createTempFile(getResources().getString(R.string.app_name) + format + "_", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        currentPhotoPath = createTempFile.getAbsolutePath();
        return createTempFile;
    }

    /*private void nativeAds() {
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
          @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.e("Adssss", initializationStatus.toString());
            }
        });
        AdLoader.Builder builder = new AdLoader.Builder((Context) this, getResources().getString(R.string.admob_native_ad));
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
    public void onDestroy() {
        super.onDestroy();
    }

    public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.ViewHolder> {
        private Context context;
        private List<PhoneNumberModel> listItem;

        public PhoneNumberAdapter(Context context2, List<PhoneNumberModel> list) {
            context = context2;
            listItem = list;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.phone_number_design_layout, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
            viewHolder.txtNumber.setText(listItem.get(i).getNumber());
            viewHolder.txtTime.setText(Const.getTimeAgo(listItem.get(i).getTime()));
            viewHolder.linearPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    thisb.edPhoneNumber.setText(listItem.get(i).getNumber());
                }
            });
        }




        @Override
        public int getItemCount() {
            return listItem.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            
            public LinearLayout linearPhone;
            
            public TextView txtNumber;
            
            public TextView txtTime;

            public ViewHolder(View view) {
                super(view);
                txtNumber = (TextView) view.findViewById(R.id.txtPhoneNumber);
                txtTime = (TextView) view.findViewById(R.id.txtPhoneTime);
                linearPhone = (LinearLayout) view.findViewById(R.id.linearPhoneNumber);
            }
        }
    }
}
