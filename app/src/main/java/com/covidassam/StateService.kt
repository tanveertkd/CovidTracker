package com.covidassam

//import android.telecom.Call
//import retrofit2.Call
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.GET
//
//const val BASE_URL = "https://api.covid19india.org/"
//
//interface StateInterface{
//
//    @GET("v4/min/data.min.json")
//    fun getCurrentCount() : Call<Assam>
//}
//
//object StateService{
//    val stateInstance: StateInterface
//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        stateInstance = retrofit.create(StateInterface::class.java)
//    }
//}