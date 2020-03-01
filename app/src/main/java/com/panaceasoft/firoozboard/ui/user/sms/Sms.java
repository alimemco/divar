package com.panaceasoft.firoozboard.ui.user.sms;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sms {

    @SerializedName("return")
    @Expose
    private Return _return;
    @SerializedName("entries")
    @Expose
    private Object entries;

    public Return getReturn() {
        return _return;
    }

    public void setReturn(Return _return) {
        this._return = _return;
    }

    public Object getEntries() {
        return entries;
    }

    public void setEntries(Object entries) {
        this.entries = entries;
    }

}