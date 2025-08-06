package com.holidayplanner.model

import java.util.Date

data class Message(
   val message:String = "",
   val userCase: UseCase,
   val dateTime: Date
)
