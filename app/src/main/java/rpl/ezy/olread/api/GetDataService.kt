package rpl.ezy.olread.api

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import rpl.ezy.olread.response.ResponseLogin
import rpl.ezy.olread.response.ResponseSignup
import rpl.ezy.olread.response.ResponseUsers


interface GetDataService {

    @GET("/users")
    fun getAllUsers(): Call<ResponseUsers>

    @FormUrlEncoded
    @POST("/users/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("/users/signup")
    fun userSignup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("status") status: Int
    ): Call<ResponseSignup>

}