package rpl.ezy.olread.adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.api.GetDataService
import rpl.ezy.olread.api.RetrofitClientInstance
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseRecipes
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.SharedPreferenceUtils
import rpl.ezy.olread.view.RecipeDetailActivity
import java.util.*
import kotlin.collections.ArrayList

class HistoryAdapter(var mContext: Context, var data: ArrayList<MRecipe>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    var interfaces: indeleteOne? = null
    private var sharedPreferences: SharedPreferenceUtils? = null
    var loading: ProgressDialog? = null

    fun interfaceClick(interfaces: indeleteOne) {
        this.interfaces = interfaces
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_vertical, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        sharedPreferences = SharedPreferenceUtils(mContext)
        loading = ProgressDialog(mContext)
        loading!!.setCancelable(false)

        GlideApp.with(mContext)
            .load(data[position].img_url)
            .transform(RoundedCorners(8))
            .into(holder.img_item)
        holder.tv_title.text = data[position].title
        holder.tv_kategori.text = data[position].kategori
//        if(data[position].like != 0){
        holder.tv_like.text = "${data[position].like} likes"
//        }

        Log.d("TES_RECIPES", "${data[position].recipe}")

        holder.itemView.setOnClickListener {
            (mContext as Activity).startActivity(
                Intent(mContext, RecipeDetailActivity::class.java)
                    .putExtra(ConstantUtils.RECIPE_ID, data[position].recipe_id)
            )
        }
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(mContext)
            builder.setMessage("Anda yakin ingin delete history?")

            builder.setPositiveButton(android.R.string.yes) { _, _ ->
                deleteOne(position)
                interfaces!!.delete(true)
//                Toast.makeText(mContext, "YES", Toast.LENGTH_SHORT).show()
            }

            builder.setNegativeButton(android.R.string.no) { dialog, _ ->
                Toast.makeText(mContext, "NO", Toast.LENGTH_SHORT).show()
            }
            builder.show()
            return@setOnLongClickListener true
        }
    }

    private fun deleteOne(position: Int) {
        loading!!.setMessage("Loading . . .")
        loading!!.show()
        val service = RetrofitClientInstance().getRetrofitInstance().create(GetDataService::class.java)
        val call = service.delOneHistory(sharedPreferences!!.getIntSharedPreferences(ConstantUtils.USER_ID), data[position].recipe_id)
        call.enqueue(object: Callback<ResponseRecipes>{
            override fun onFailure(call: Call<ResponseRecipes>, t: Throwable) {
                loading!!.cancel()
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
                if(response.isSuccessful) {
                    loading!!.cancel()
                    Toast.makeText(mContext, "Berhasil hapus!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var img_item = v.findViewById(R.id.img_item) as ImageView
        var tv_title = v.findViewById(R.id.tv_title) as TextView
        var tv_kategori = v.findViewById(R.id.tv_kategori) as TextView
        var tv_like = v.findViewById(R.id.tv_like) as TextView
    }

    interface indeleteOne {
        fun delete(isSuccess: Boolean)
    }

}