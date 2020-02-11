package rpl.ezy.olread.view.admin

import android.app.Dialog
import android.content.Context
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
import rpl.ezy.olread.response.ResponseUsers
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils

class BanedDialog(var mContext: Context, var user_id: Int, var action: Int): Dialog(mContext) {

    private var sharedPreferences: SharedPreferenceUtils? = null
    var interfaceRefresh: InterfaceRefresh? = null

    fun interfaceRefresh(data: InterfaceRefresh){
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

        bt_no.setOnClickListener {
            dismiss()
        }
        bt_yes.setOnClickListener {
            if(action == ConstantUtils.ACTIONBAN) {
                banedUser()
            } else {
                unBanedUser()
            }
        }
    }

    private fun banedUser(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.banUser(user_id)
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseUsers>,
                response: Response<ResponseUsers>
            ) {
                if(response.isSuccessful){
                    val responses = response.body()!!
                    if( responses.status == 200){
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

    private fun unBanedUser(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.unBanUser(user_id)
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseUsers>,
                response: Response<ResponseUsers>
            ) {
                if(response.isSuccessful){
                    val responses = response.body()!!
                    if( responses.status == 200){
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

    interface InterfaceRefresh {
        fun void(status: Boolean)
    }

}