package com.panaceasoft.firoozboard.api;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {


    @POST("sms/send.php")
    Call<ResponseBody> sendSms(
            @Query("phone") String phone,
            @Query("code") String code
    );
}
