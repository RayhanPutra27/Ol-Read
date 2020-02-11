package rpl.ezy.olread.view.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_manage_user.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.R
import rpl.ezy.olread.adapter.UserAdapter
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.response.ResponseUsers

class ManageUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_user)

        bt_back.setOnClickListener {
            finish()
        }

        setUser()
    }

    private fun setUser(){
        val service =
            RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.getAllUsers()
        call.enqueue(object : Callback<ResponseUsers> {
            override fun onFailure(call: Call<ResponseUsers>, t: Throwable) {
                Toast.makeText(
                    this@ManageUserActivity,
                    "Something went wrong...Please try later!",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d("LOGLOGAN", "${t.message}")
            }

            override fun onResponse(call: Call<ResponseUsers>, response: Response<ResponseUsers>) {
                if (response.body()!!.status == 200){
                    val data = response.body()!!.data

                    val mAdapter = UserAdapter(this@ManageUserActivity, data)

                    mAdapter.interfaceRefresh(object : UserAdapter.InterfaceRefresh{
                        override fun void(status: Boolean) {
                            if(status){
                                setUser()
                            }
                        }

                    })

                    rv_manage.apply {
                        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                        adapter = mAdapter
                    }

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setUser()
    }
}
