package com.example.jobapplication.models

data class Job(
    var postion : String = "",
    var description : String = "",
    var skills : String = "",
    var workExperience : String = "",
    var location :String = "",
    var companyName : String = "",
    var jobId : String = "",
    var applicants : MutableList<String> = mutableListOf(),
    var salary : String = "",
    var duration : String = "",
    var date: String = "",
    var lastDate: String = "",
    var mode : String = "",
)
