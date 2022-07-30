package net.manish.wabot.model;

public class AutoReplyTextModel {
    private String text;

    public AutoReplyTextModel(String str) {
        this.text = str;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String str) {
        this.text = str;
    }
}
