package com.capacitorjs.plugins.actionsheet;

public class ActionSheetOption {

    private String title;
    private String style;

    public ActionSheetOption(String title, String style) {
        this.title = title;
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public String getStyle() {
        return style;
    }
}
