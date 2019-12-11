package rpl.ezy.olread.api

import retrofit2.Call
import retrofit2.http.*
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.*


interface GetDataService {

    // User

    @GET("users")
    fun getAllUsers(): Call<ResponseUsers>

    @FormUrlEncoded
    @POST("users/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("users/signup")
    fun userSignup(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("status") status: Int
    ): Call<ResponseSignup>

    @GET("users/{user_id}")
    fun getUser(@Path("user_id") user_id: Int): Call<ResponseUsers>

    // Recipes

    @GET("recipes/accepted")
    fun getAcceptedRecipe(): Call<ResponseRecipes>

    @GET("recipes/kategori/{kategori}")
    fun getSelectCategory(@Path("kategori") kategori: String): Call<ResponseRecipes>

    @GET("recipes/unaccepted")
    fun getUnAcceptedRecipe(): Call<ResponseRecipes>

    @GET("recipes/{recipe_id}")
    fun getRecipeById(@Path("recipe_id") recipe_id: Int): Call<ResponseRecipeById>

    @PUT("recipes/confirm/{recipes_id}")
    fun confirmRecipes(@Path("recipes_id") recipe_id: Int): Call<ResponseRecipes>

    @GET("recipes/kategori")
    fun getCategory(): Call<ResponseRecipes>

    @GET("recipes/search/{title}")
    fun getSearch(@Path("title") title: String): Call<ResponseRecipes>

    @GET("recipes/users/{user_id}")
    fun getRecipeByUser(@Path("user_id") user_id: Int): Call<ResponseRecipes>

    // Archive
    @GET("recipes/archive/{user_id}")
    fun getArchivebyId(@Path("user_id") user_id: Int): Call<ResponseRecipes>

    @FormUrlEncoded
    @POST("recipes/archive")
    fun archivingRecipe(
        @Field("user_id") user_id: Int,
        @Field("recipe_id") recipe_id: Int
    ): Call<ResponseRecipes>

    @FormUrlEncoded
    @POST("recipes/archive/delete")
    fun delArchive(
        @Field("user_id") user_id: Int,
        @Field("recipe_id") recipe_id: Int
    ): Call<ResponseRecipes>
}