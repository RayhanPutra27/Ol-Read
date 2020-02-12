package rpl.ezy.olread.view.user

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import kotlinx.android.synthetic.main.dialog_baned.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.RecipeDetailActivity
import java.io.Serializable

class ManageRecipeDialog(var mContext: Context, var data: MRecipe): Dialog(mContext) {

    private var sharedPreferences: SharedPreferenceUtils? = null
    var interfaceRefresh: InterfaceRefresh? = null

    fun interfaceRefresh(data: InterfaceRefresh) {
        this.interfaceRefresh = data
    }

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_baned)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp

        sharedPreferences = SharedPreferenceUtils(context)

        message.setText("${data.title}")

        bt_no.setText("Delete")
        bt_no.setOnClickListener {
            deleteRecipe(data.recipe_id)
        }

        bt_yes.setText("Edit")
        bt_yes.setOnClickListener {
            (mContext as Activity).startActivity(Intent(mContext, AddRecipesActivity::class.java)
                .putExtra("type", "edit")
                .putExtra(ConstantUtils.RECIPE_ID, data.recipe_id))
            dismiss()
        }
    }

    private fun deleteRecipe(recipeId: Int) {
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.delRecipe(recipeId)
        call.enqueue(object : Callback<ResponseRecipes> {
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseRecipes>,
                response: Response<ResponseRecipes>
            ) {
                if (response.isSuccessful) {
                    val responses = response.body()!!
                    if (responses.status == 200) {
                        Toast.makeText(
                            mContext,
                            responses.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        interfaceRefresh!!.void(true)
                        dismiss()
                    } else {
                        Toast.makeText(
                            mContext,
                            responses.message,
                            Toast.LENGTH_SHORT
                        ).show()
                        interfaceRefresh!!.void(false)
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        "Ada kesalahan server",
                        Toast.LENGTH_SHORT
                    ).show()
                    interfaceRefresh!!.void(false)
                }
            }
        })
    }
//
//    private fun unBanedUser() {
//        val service =
//            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
//        val call = service.unBanUser(user_id)
//        call.enqueue(object : Callback<ResponseUsers> {
//            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
//                Toast.makeText(
//                    mContext,
//                    "Something went wrong...Please try later!",
//                    Toast.LENGTH_SHORT
//                ).show()
//                Log.d("LOGLOGAN", "${t.message}")
//            }
//
//            override fun onResponse(
//                call: Call<ResponseUsers>,
//                response: Response<ResponseUsers>
//            ) {
//                if (response.isSuccessful) {
//                    val responses = response.body()!!
//                    if (responses.status == 200) {
//                        Toast.makeText(
//                            mContext,
//                            responses.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        interfaceRefresh!!.void(true)
//                        dismiss()
//                    } else {
//                        Toast.makeText(
//                            mContext,
//                            responses.message,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        interfaceRefresh!!.void(false)
//                    }
//                } else {
//                    Toast.makeText(
//                        mContext,
//                        "Ada kesalahan server",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    interfaceRefresh!!.void(false)
//                }
//            }
//        })
//    }

    interface InterfaceRefresh {
        fun void(status: Boolean)
    }
}