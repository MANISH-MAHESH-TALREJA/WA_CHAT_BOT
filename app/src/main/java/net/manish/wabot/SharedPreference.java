package net.manish.wabot;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import net.manish.wabot.model.AutoReply;
import net.manish.wabot.model.PhoneNumberModel;
import net.manish.wabot.model.StatisticsReplyMsgListModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class SharedPreference
{
    private final Context context;
    SharedPreferences.Editor editor;
    private SharedPreferences preferences;

    public SharedPreference(Context context2)
    {
        context = context2;
        preferences = context2.getSharedPreferences("mypref", 0);
        editor = preferences.edit();
    }

    public void addToPref_Long(String str, long j)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putLong(str, j);
        editor.commit();
    }

    public long getFromPref_Long(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(str, 0);
    }

    public void addToPref_Boolean(String str, boolean z)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        editor = preferences.edit();
        editor.putBoolean(str, z);
        editor.commit();
    }

    public boolean getFromPref_Boolean(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(str, false);
    }

    public void addToPref_String(String str, String str2)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(str, str2);
        editor.commit();
    }

    public String getFromPref_String(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(str, "");
    }

    public <T> void setList(String str, List<T> list)
    {
        set(str, new Gson().toJson(list));
    }

    public <T> void setReplyList(String str, List<T> list)
    {
        set(str, new Gson().toJson(list));
    }

    public <T> void setAutoTextList(String str, List<T> list)
    {
        String json = new Gson().toJson(list);
        Log.e("PRINT JSON", json);
        set(str, json);
    }

    public <T> void setUserList(String str, List<T> list)
    {
        String json = new Gson().toJson(list);
        Log.e("PRINT JSON", json);
        set(str, json);
    }

    /*public <T> void setNumberList(String str, List<T> list)
    {
        String json = new Gson().toJson(list);
        Log.e("PRINT JSON", json);
        set(str, json);
    }*/

    public <T> void setContactList(String str, List<T> list)
    {
        String json = new Gson().toJson(list);
        Log.e("PRINT JSON Contact", json);
        set(str, json);
    }

    public void set(String str, String str2)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putString(str, str2);
        editor.commit();
    }

    public List<AutoReply> getList(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new Gson().fromJson(preferences.getString(str, ""), new TypeToken<List<AutoReply>>()
        {
        }.getType());
    }

    public List<StatisticsReplyMsgListModel> getReplyList(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new Gson().fromJson(preferences.getString(str, ""), new TypeToken<List<StatisticsReplyMsgListModel>>()
        {
        }.getType());
    }

    public List<String> getAutoTextList(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        Log.e("GET JSON", string);
        return gson.fromJson(string, new TypeToken<List<String>>()
        {
        }.getType());
    }

    public List<String> getUserList(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        Log.e("GET JSON", string);
        return gson.fromJson(string, new TypeToken<List<String>>()
        {
        }.getType());
    }

    public List<PhoneNumberModel> getNumberList(String str)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String string = preferences.getString(str, "");
        Log.e("GET JSON", string);
        return gson.fromJson(string, new TypeToken<List<PhoneNumberModel>>()
        {
        }.getType());
    }

    public List<String> getContactList(String str)
    {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this.context);
        Gson gson = new Gson();
        String string = this.preferences.getString(str, "");
        Log.e("GET JSON Contact", string);
        return gson.fromJson(string, new TypeToken<List<String>>()
        {
        }.getType());
    }
}
