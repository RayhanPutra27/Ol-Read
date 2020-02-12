package rpl.ezy.olread.view.user

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_select_kategori.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.KategoriAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseCategory
import rpl.ezy.olread.utils.SharedPreferenceUtils

class SelectKategori (var mContext: Context) : Dialog(mContext) {

    private var sharedPreference: SharedPreferenceUtils? = null
    var mAdapter: KategoriAdapter? = null
    var interfaceKategori: InterfaceKategori? = null

    fun interfaceKategori(interfaceKategori: InterfaceKategori){
        this.interfaceKategori = interfaceKategori
    }

    init {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_select_kategori)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        val window = window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp

        sharedPreference = SharedPreferenceUtils(mContext)

//        bt_close_select_kategori.setOnClickListener {
//            dismiss()
//        }

        getKategori(mContext)
    }

    private fun getKategori(mContext: Context){
        val service = RetrofitClientInstance()
            .getRetrofitInstance()
            .create(GetDataService::class.java)

        service.getCategory().enqueue(object: Callback<ResponseCategory> {
            override fun onFailure(call: Call<ResponseCategory>, t: Throwable) {
                Toast.makeText(
                    mContext,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(
                call: Call<ResponseCategory>,
                response: Response<ResponseCategory>
            ) {
                var data = response.body()!!.data

                mAdapter = KategoriAdapter(mContext, data)
                mAdapter!!.interfaceClick(object: KategoriAdapter.OnClickItem{
                    override fun void(kategori: String) {
                        interfaceKategori!!.void(kategori)
                        dismiss()
                    }
                })
                rv_kategori.apply{
                    layoutManager = LinearLayoutManager(mContext)
                    adapter = mAdapter
                }
            }

        })
    }

        interface InterfaceKategori{
            fun void(kategori: String)
        }

    override fun onBackPressed() {
        super.onBackPressed()
        dismiss()
    }

}

