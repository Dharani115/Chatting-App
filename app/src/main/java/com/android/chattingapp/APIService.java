package com.android.chattingapp;

import com.android.chattingapp.Notification.MyResponse;
import com.android.chattingapp.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA0I7cqfA:APA91bGWx9TyXP3nLa-EsZEcVRjEXQlRzQbONpHSeafNEnlvmW915AEXb3XUAJ-MZvt9sYM8Dzi8C_pgMYpvbXdt58T0aKPPotmFaQqXJNo0GtXK18PKQ-PvbPlMZ2xDCua-r2MHqC_1"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}