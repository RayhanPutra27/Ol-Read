package rpl.ezy.olread.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
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

    @Multipart
    @POST("users/signup")
    fun userSignup(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("status") status: RequestBody,
        @Part profil: MultipartBody.Part
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

    @GET("recipes/confirm/{recipes_id}")
    fun confirmRecipes(@Path("recipes_id") recipe_id: Int): Call<ResponseRecipes>

    @GET("recipes/kategori")
    fun getCategory(): Call<ResponseRecipes>

    @GET("recipes/search/{title}")
    fun getSearch(@Path("title") title: String): Call<ResponseRecipes>

    @GET("recipes/users/{user_id}")
    fun getRecipeByUser(@Path("user_id") user_id: Int): Call<ResponseRecipes>

    @GET("recipes/trends")
    fun getTrendsRecipe(): Call<ResponseRecipes>

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

    // Like
    @GET("recipes/like/{user_id}")
    fun getLikebyId(@Path("user_id") user_id: Int): Call<ResponseRecipes>

    @FormUrlEncoded
    @POST("recipes/like")
    fun likeRecipe(
        @Field("user_id") user_id: Int,
        @Field("recipe_id") recipe_id: Int
    ): Call<ResponseRecipes>

    @FormUrlEncoded
    @POST("recipes/like/delete")
    fun delLike(
        @Field("user_id") user_id: Int,
        @Field("recipe_id") recipe_id: Int
    ): Call<ResponseRecipes>

    @Multipart
    @POST("recipes/request")
    fun reqRecipe(
        @Part("user_id") user_id: RequestBody,
        @Part("title") title: RequestBody,
        @Part("recipe") recipe: RequestBody,
//        @Part("img_url") image: File,
        @Part img_url: MultipartBody.Part,
        @Part("kategori") kategori: RequestBody
    ): Call<ResponseAddRecipe>

    @GET("recipes/rejected")
    fun getRejectedRecipe(): Call<ResponseRecipes>

    @GET("recipes/reject/{recipes_id}")
    fun rejectRecipes(@Path("recipes_id") recipe_id: Int): Call<ResponseRecipes>

    @GET("users/ban/{user_id}")
    fun banUser(@Path("user_id") user_id: Int): Call<ResponseUsers>

    @GET("users/unban/{user_id}")
    fun unBanUser(@Path("user_id") user_id: Int): Call<ResponseUsers>
}