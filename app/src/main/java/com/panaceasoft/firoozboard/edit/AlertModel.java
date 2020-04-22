package com.panaceasoft.firoozboard.edit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertModel {

    @SerializedName("show")
    @Expose
    private Boolean show;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}