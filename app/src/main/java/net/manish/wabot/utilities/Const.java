package net.manish.wabot.utilities;

import net.manish.wabot.model.AutoReply;
import net.manish.wabot.model.StatisticsReplyMsgListModel;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Const
{
    public static List<String> autoText = new ArrayList();
    public static List<String> contactList = new ArrayList();
    public static List<AutoReply> replyList = new ArrayList();
    public static List<StatisticsReplyMsgListModel> staticsReplyList = new ArrayList();
    public static List<String> userList = new ArrayList();

    public static String getTimeAgo(long j)
    {
        if (j < 1000000000000L)
        {
            j *= 1000;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (j > currentTimeMillis || j <= 0)
        {
            return null;
        }
        long j2 = currentTimeMillis - j;
        if (j2 < 60000)
        {
            return "just now";
        }
        if (j2 < 120000)
        {
            return "a minute ago";
        }
        if (j2 < 3000000)
        {
            return (j2 / 60000) + " minutes ago";
        }
        else if (j2 < 5400000)
        {
            return "an hour ago";
        }
        else
        {
            if (j2 < 86400000)
            {
                return (j2 / 3600000) + " hours ago";
            }
            else if (j2 < 172800000)
            {
                return "yesterday";
            }
            else
            {
                return (j2 / 86400000) + " days ago";
            }
        }
    }
}
