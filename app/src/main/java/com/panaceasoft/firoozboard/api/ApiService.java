package com.panaceasoft.firoozboard.api;


import com.panaceasoft.firoozboard.ui.user.smsModel.SmsModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    /*
        @POST("sms/send.php")
        Call<ResponseBody> sendSms(
                @Query("phone") String phone,
                @Query("code") String code
        );*/
    @FormUrlEncoded
    @POST("https://api.kavenegar.com/v1/{API-KEY}/verify/lookup.json")
    Call<SmsModel> sendSms(
            @Field("receptor") String phone,
            @Field("token") String code,
            @Field("template") String template,
            @Path("API-KEY") String api_key);
}
