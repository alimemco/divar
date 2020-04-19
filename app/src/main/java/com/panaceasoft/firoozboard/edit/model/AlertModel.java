package com.panaceasoft.firoozboard.edit.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlertModel {

@SerializedName("show")
@Expose
private Boolean show;
@SerializedName("message")
@Expose
private String message;
@SerializedName("categories")
@Expose
private List<Category> categories = null;

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

public List<Category> getCategories() {
return categories;
}

public void setCategories(List<Category> categories) {
this.categories = categories;
}

}