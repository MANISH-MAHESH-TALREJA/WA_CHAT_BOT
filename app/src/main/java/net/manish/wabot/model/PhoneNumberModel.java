package net.manish.wabot.model;

public class PhoneNumberModel
{
    private final String Number;
    private final long time;

    public PhoneNumberModel(String str, Long l)
    {
        this.Number = str;
        this.time = l;
    }

    public String getNumber()
    {
        return this.Number;
    }

    public long getTime()
    {
        return this.time;
    }

}
