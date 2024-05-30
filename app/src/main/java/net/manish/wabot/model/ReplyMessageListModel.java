package net.manish.wabot.model;

public class ReplyMessageListModel
{
    private final String date;
    private String name;
    private final String sameDate;
    private final String time;

    public ReplyMessageListModel(String str, String str2, String str3, String str4)
    {
        this.name = str;
        this.date = str2;
        this.time = str3;
        this.sameDate = str4;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String str)
    {
        this.name = str;
    }

    public String getTime()
    {
        return this.time;
    }

    public String getDate()
    {
        return this.date;
    }

    public String getSameDate()
    {
        return this.sameDate;
    }

}
