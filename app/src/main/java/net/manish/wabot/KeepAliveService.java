package net.manish.wabot;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class KeepAliveService extends Service
{
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        startNotificationService();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2)
    {
        startNotificationService();
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent intent)
    {
        super.onTaskRemoved(intent);
        tryReconnectService();
    }


    public void tryReconnectService()
    {
        Intent intent = new Intent(getApplicationContext(), getClass());
        intent.setPackage(getPackageName());
        startService(intent);
    }

    private void startNotificationService()
    {
        if (!isMyServiceRunning())
        {
            Log.d("DEBUG", "KeepAliveService startNotificationService");
            startService(new Intent(this, NotificationService.class));
        }
    }

    private boolean isMyServiceRunning()
    {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE))
        {
            if (NotificationService.class.equals(runningServiceInfo.service.getClassName()))
            {
                Log.e("isMyServiceRunning?", "true");
                return true;
            }
        }
        Log.e("isMyServiceRunning?", "false");
        return false;
    }
}
