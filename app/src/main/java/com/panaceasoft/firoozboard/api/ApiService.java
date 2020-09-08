package com.panaceasoft.firoozboard.api;

import com.panaceasoft.firoozboard.edit.model.AlertModel;
import com.panaceasoft.firoozboard.edit.model.InviteModel;
import com.panaceasoft.firoozboard.edit.model.QueryModel;
import com.panaceasoft.firoozboard.ui.user.sms.KavehNegar;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @POST("ApiService//edit/update_password.php")
    Call<QueryModel> updatePassword(@Query("token") String api_key,
                                    @Query("phone") String phone,
                                    @Query("password") String password
    );


    @POST("ApiService//edit/pay_detail.php")
    Call<QueryModel> sendPayDetail(@Query("token") String api_key,
                                   @Query("userID") String userID,
                                   @Query("refId") String refId,
                                   @Query("catId") String catId,
                                   @Query("price") String price

    );


    @POST("ApiService/edit/invite.php")
    Call<ResponseBody> sendInviteCode(@Query("invite_code") String inviteCode,
                                      @Query("invited_user") String invitedUser

    );

    @GET("ApiService/edit/invitersCount.php")
    Call<List<InviteModel>> getAllInviter();

}
