package rpl.ezy.olread.adapter

import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import rpl.ezy.olread.R
import rpl.ezy.olread.model.MCategory
import rpl.ezy.olread.utils.SharedPreferenceUtils

class KategoriAdapter(var mContext: Context, var data: ArrayList<MCategory>) :
    RecyclerView.Adapter<KategoriAdapter.ViewHolder>() {

    var interfaces: OnClickItem? = null
    var sharedPreferences: SharedPreferenceUtils? = null
    var loading: ProgressDialog? = null

    fun interfaceClick(interfaces: OnClickItem) {
        this.interfaces = interfaces
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_kategori, parent, false)
        sharedPreferences = SharedPreferenceUtils(mContext)
        loading = ProgressDialog(mContext)
        loading!!.setCancelable(false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = data[position]
        holder.kategori.text = data.kategori

        if (position == this.data.size - 1) {
            holder.view.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            interfaces!!.void(data.kategori)
        }
    }

    interface OnClickItem {
        fun void(kategori: String)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var view = v.findViewById(R.id.view) as View
        var kategori = v.findViewById(R.id.tv_kategori) as TextView
        var edit = v.findViewById(R.id.edit_kategori) as ImageView
    }
}