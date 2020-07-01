package rpl.ezy.olread.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RetrofitClientInstance {

    fun getRetrofitInstance(): Retrofit {
//        if (retrofit == null) {

            var retrofit = Retrofit.Builder()
                .baseUrl("https://olrecipe.000webhostapp.com/")
//                .baseUrl("http://192.168.43.193/lumen/olrecipe/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
//        }
        return retrofit
    }

}