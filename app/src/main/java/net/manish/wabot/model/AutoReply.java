package net.manish.wabot.model;

public class AutoReply
{
    String ReceiveMsg;
    String SendMsg;

    public AutoReply(String str, String str2)
    {
        this.ReceiveMsg = str;
        this.SendMsg = str2;
    }

    public String getReceiveMsg()
    {
        return this.ReceiveMsg;
    }

    public String getSendMsg()
    {
        return this.SendMsg;
    }

}
