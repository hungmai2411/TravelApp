package com.travelappproject;

import com.travelappproject.model.hotel.Message;
import com.travelappproject.model.hotel.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface SendMessageApi {
    @Headers({
            "Authorization: Bearer AAAAMJFIOp4:APA91bFWLvEb7yla2D-7MEkAuDwzHw1xJ0NYDKnQ7N5jbKM5J5W0_wslXEAh_PX8ljG5aZzHYhQ7EGkIhkXeQSvhpJ2eomxVCtJThjLMKFgofwBjblXz6kNJOVOC-iGE3R_Aef8NkPlT",
            "Content-Type: application/json"
    })
    @POST("fcm/send")
    Call<Message> sendMessage(@Body Message message);
}
