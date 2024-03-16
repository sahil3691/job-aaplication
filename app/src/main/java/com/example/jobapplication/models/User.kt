package com.example.jobapplication.models

data class User(
    var userId : String = "",
    var username : String = "",
    var image : String = "",
    var resume : String = "",
    var education :String = "",
    var gender : String = "",
    var dob : String = "",
    var skills : String = "",
    var certificate : String = "",
    var experience : String = "",
    var jobs : MutableList<String> = mutableListOf(),
    var fcmToken : String = "",

)
