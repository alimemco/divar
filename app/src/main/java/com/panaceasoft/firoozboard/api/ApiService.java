package com.panaceasoft.firoozboard.api;

import com.panaceasoft.firoozboard.edit.model.AlertModel;
import com.panaceasoft.firoozboard.ui.user.sms.KavehNegar;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @FormUrlEncoded
    @POST("https://api.kavenegar.com/v1/{API-KEY}/verify/lookup.json")
    Call<KavehNegar> sendSms(
            @Field("receptor") String phone,
            @Field("token") String code,
            @Field("template") String template,
            @Path("API-KEY") String api_key);


    @FormUrlEncoded
    @POST("https://api.kavenegar.com/v1/{API-KEY}/verify/lookup.json")
    Call<KavehNegar> sendForgetPassword(
            @Field("receptor") String phone,
            @Field("token") String code,
            @Field("template") String template,
            @Path("API-KEY") String api_key);


    @GET("ApiService/json/alert.json")
    Call<AlertModel> getAlert();
}
