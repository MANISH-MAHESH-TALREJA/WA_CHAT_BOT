package net.manish.wabot.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import net.manish.wabot.databinding.ActivityPermissionCheckBinding;

public class PermissionCheckActivity extends AppCompatActivity
{
    public static final String storageManualAlertMsg = "We need your permissions to access the camera and storage. Please permit the permission through\nSettings screen.\n\nSelect Permissions -> Enable permission";
    private final int MULTIPLE_PERMISSIONS = 100;
    String[] permissions_old = {"android.permission.READ_CONTACTS"};
    String[] permissions_new = {"android.permission.POST_NOTIFICATIONS", "android.permission.READ_CONTACTS"};
    ActivityPermissionCheckBinding myThis;


    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        myThis = DataBindingUtil.setContentView(this, R.layout.activity_permission_check);
        Context context = this;
        if (Build.VERSION.SDK_INT >= 33)
        {
            hasPermissions(context, permissions_new);
        }
        else
        {
            hasPermissions(context, permissions_old);
        }
    }

    public void isPermissionGranted(boolean z)
    {
        if (!z)
        {
            startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.fromParts("package", getPackageName(), null)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr)
    {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == MULTIPLE_PERMISSIONS)
        {
            if (iArr.length == 0 || iArr[0] != 0)
            {
                StringBuilder str = new StringBuilder();
                for (String str2 : strArr)
                {
                    str.append("\n").append(str2);
                }
                Log.e("Permission", str + "::");
                return;
            }
            getActivity();
            return;
        }
        Log.e("No", "Permission");
    }

    public void hasPermissions(Context context2, String... strArr)
    {
        if (!(context2 == null || strArr == null))
        {
            for (String checkSelfPermission : strArr)
            {
                if (ActivityCompat.checkSelfPermission(context2, checkSelfPermission) != 0)
                {
                    ActivityCompat.requestPermissions(this, strArr, MULTIPLE_PERMISSIONS);
                }
                else
                {
                    isPermissionGranted(true);
                }
            }
        }
    }


    public boolean isStoragePermissionGranted(Activity activity, int i)
    {
        if (Build.VERSION.SDK_INT > 23 && Build.VERSION.SDK_INT < 33)
        {
            if (activity.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED)
            {
                Log.v("meaning", "Permission is granted");
                return true;
            }
            else if (neverAskAgainSelected(activity, "android.permission.READ_CONTACTS"))
            {
                displayNeverAskAgainDialog(activity);
                return false;
            }
            else
            {
                ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_CONTACTS"}, i);
                return false;
            }
        }
        else if (Build.VERSION.SDK_INT >= 33)
        {
            if (activity.checkSelfPermission("android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED && activity.checkSelfPermission("android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED)
            {
                Log.v("meaning", "Permission is granted");
                return true;
            }
            else if (neverAskAgainSelected(activity, "android.permission.POST_NOTIFICATIONS"))
            {
                displayNeverAskAgainDialog(activity);
                return false;
            }

            else if (neverAskAgainSelected(activity, "android.permission.READ_CONTACTS"))
            {
                displayNeverAskAgainDialog(activity);
                return false;
            }
            else
            {
                ActivityCompat.requestPermissions(activity, new String[]{"android.permission.POST_NOTIFICATIONS", "android.permission.READ_CONTACTS"}, i);
                return false;
            }
        }
        else
        {
            return true;
        }

    }

    public boolean neverAskAgainSelected(Activity activity, String str)
    {
        return getRatinaleDisplayStatus(activity, str) != activity.shouldShowRequestPermissionRationale(str);
    }

    public boolean getRatinaleDisplayStatus(Context context2, String str)
    {
        return context2.getSharedPreferences("GENERIC_PREFERENCES", 0).getBoolean(str, false);
    }

    private void displayNeverAskAgainDialog(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(PermissionCheckActivity.storageManualAlertMsg);
        builder.setCancelable(false);
        builder.setPositiveButton("Permit Manually", (dialogInterface, i) ->
        {
            dialogInterface.dismiss();
            Intent intent = new Intent();
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
            activity.startActivity(intent);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) ->
        {
            moveTaskToBack(true);
            Process.killProcess(Process.myPid());
            System.exit(0);
        });
        builder.show();
    }


    private void getActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
    }


    public void onResume()
    {
        super.onResume();
        if (isStoragePermissionGranted(this, MULTIPLE_PERMISSIONS))
        {
            getActivity();
        }
    }
}
