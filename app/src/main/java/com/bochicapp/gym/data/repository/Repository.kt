package com.bochicapp.gym.data.repository

import android.content.Context
import com.bochicapp.gym.data.local.DataBase

object Repository {

    lateinit var database: DataBase

    fun init( context: Context ){
        database = DataBase(
            context = context
        )
    }

}