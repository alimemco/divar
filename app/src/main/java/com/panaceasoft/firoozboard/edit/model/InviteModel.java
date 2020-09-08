package com.panaceasoft.firoozboard.edit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InviteModel {

   @SerializedName("caller_username")
   @Expose
   private String callerUsername;
   @SerializedName("caller_phone")
   @Expose
   private String callerPhone;
   @SerializedName("inviteCount")
   @Expose
   private String inviteCount;

   public String getCallerUsername() {
      return callerUsername;
   }

   public void setCallerUsername(String callerUsername) {
      this.callerUsername = callerUsername;
   }

   public String getCallerPhone() {
      return callerPhone;
   }

   public void setCallerPhone(String callerPhone) {
      this.callerPhone = callerPhone;
   }

   public String getInviteCount() {
      return inviteCount;
   }

   public void setInviteCount(String inviteCount) {
      this.inviteCount = inviteCount;
   }

}