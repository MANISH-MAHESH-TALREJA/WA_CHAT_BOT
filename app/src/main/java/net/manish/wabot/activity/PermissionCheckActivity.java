package net.manish.wabot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import net.manish.wabot.R;
import net.manish.wabot.SharedPreference;
import net.manish.wabot.databinding.ActivityPermissionCheckBinding;

public class PermissionCheckActivity extends AppCompatActivity {
    public static final String storageManualAlertMsg = "We need your permissions to access the camera and storage. Please permit the permission through\nSettings screen.\n\nSelect Permissions -> Enable permission";
    private final int MULTIPLE_PERMISSIONS = 100;
    String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.READ_CONTACTS", "android.permission.CAMERA"};
    ActivityPermissionCheckBinding myThis;

    
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        myThis = (ActivityPermissionCheckBinding) DataBindingUtil.setContentView(this, R.layout.activity_permission_check);
        Context context = this;
        SharedPreference preference = new SharedPreference(this);
        hasPermissions(context, permissions);
    }

    public void isPermissionGranted(boolean z) {
        if (!z) {
            startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", getPackageName(), (String) null)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == MULTIPLE_PERMISSIONS) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                String str = "";
                for (String str2 : strArr) {
                    str = str + "\n" + str2;
                }
                Log.e("Permission", str + "::");
                return;
            }
            getActivity();
            return;
        }
        Log.e("No", "Permission");
    }

    public boolean hasPermissions(Context context2, String... strArr) {
        if (!(context2 == null || strArr == null)) {
            for (String checkSelfPermission : strArr) {
                if (ActivityCompat.checkSelfPermission(context2, checkSelfPermission) != 0) {
                    ActivityCompat.requestPermissions(this, strArr, MULTIPLE_PERMISSIONS);
                } else {
                    isPermissionGranted(true);
                }
            }
        }
        return true;
    }


    public boolean isStoragePermissionGranted(Activity activity, int i) {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        if (activity.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission("android.permission.CAMERA") == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            Log.v("meaning", "Permission is granted");
            return true;
        } else if (neverAskAgainSelected(activity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            displayNeverAskAgainDialog(activity, storageManualAlertMsg);
            return false;
        } else if (neverAskAgainSelected(activity, "android.permission.READ_EXTERNAL_STORAGE")) {
            displayNeverAskAgainDialog(activity, storageManualAlertMsg);
            return false;
        } else if (neverAskAgainSelected(activity, "android.permission.CAMERA")) {
            displayNeverAskAgainDialog(activity, storageManualAlertMsg);
            return false;
        } else if (neverAskAgainSelected(activity, "android.permission.READ_CONTACTS")) {
            displayNeverAskAgainDialog(activity, storageManualAlertMsg);
            return false;
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA", "android.permission.READ_CONTACTS"}, i);
            return false;
        }
    }

    public boolean neverAskAgainSelected(Activity activity, String str) {
        return getRatinaleDisplayStatus(activity, str) != activity.shouldShowRequestPermissionRationale(str);
    }

    public boolean getRatinaleDisplayStatus(Context context2, String str) {
        return context2.getSharedPreferences("GENERIC_PREFERENCES", 0).getBoolean(str, false);
    }

    private void displayNeverAskAgainDialog(Activity activity, String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(str);
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Intent intent = new Intent();
                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                intent.setData(Uri.fromParts("package", activity.getPackageName(), (String) null));
                activity.startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                Process.killProcess(Process.myPid());
                System.exit(0);
            }
        });
        builder.show();
    }



    private void getActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    
    public void onResume() {
        super.onResume();
        if (isStoragePermissionGranted(this, MULTIPLE_PERMISSIONS)) {
            getActivity();
        }
    }
}
