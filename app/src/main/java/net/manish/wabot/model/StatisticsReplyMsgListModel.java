package net.manish.wabot.model;

public class StatisticsReplyMsgListModel
{
    private final String PersonName;
    private int i;
    private final String replyMsg;
    private final long time;

    public StatisticsReplyMsgListModel(int i2, String str, String str2, long j)
    {
        this.i = i2;
        this.replyMsg = str;
        this.PersonName = str2;
        this.time = j;
    }

    public int getI()
    {
        return this.i;
    }

    public void setI(int i2)
    {
        this.i = i2;
    }

    public String getReplyMsg()
    {
        return this.replyMsg;
    }

    public String getPersonName()
    {
        return this.PersonName;
    }


    public long getTime()
    {
        return this.time;
    }

}
