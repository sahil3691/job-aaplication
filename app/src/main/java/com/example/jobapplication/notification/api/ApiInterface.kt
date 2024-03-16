package com.example.buzzhub.notification.api

import com.example.buzzhub.notification.PushNotification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {
    @Headers("Content-Type:application/json", "Authorization:key=AAAAafQGBcA:APA91bFu3FOLsw2KT9OqH95fos2gPNvl2elf1BB2XTNcm73WPWMjhIRh9X8EkQ3NopC6h3pUhWW678HPbdSk5Q5KNpxXQXat8AB7GnJwii2ZaCDbTklaEjegFefAEn8AH_vr8kIT1M_O")
    @POST("fcm/send")
    fun sendNotification(@Body notification : PushNotification)
    :Call<PushNotification>
}