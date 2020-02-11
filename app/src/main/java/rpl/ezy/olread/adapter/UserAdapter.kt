package rpl.ezy.olread.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.dialog_baned.*
import rpl.ezy.olread.R
import rpl.ezy.olread.model.MUser
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.utils.ConstantUtils.USER_ID
import rpl.ezy.olread.view.admin.BanedDialog
import rpl.ezy.olread.view.user.ProfileActivity

class UserAdapter(var mContext: Context, var data: ArrayList<MUser>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    var dialog: BanedDialog? = null

    var interfaceRefresh: InterfaceRefresh? = null

    fun interfaceRefresh(data: InterfaceRefresh) {
        this.interfaceRefresh = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Glide.with(mContext)
            .load(data[position].profil)
            .into(holder.image)
        holder.username.text = data[position].username
        holder.like.text = data[position].like.toString()

        if (data[position].status == 2) {
            holder.baned.visibility = View.VISIBLE
        } else {
            holder.baned.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            (mContext as Activity).startActivity(Intent(mContext, ProfileActivity::class.java).putExtra(USER_ID, data[position].user_id))
        }

        holder.menu.setOnClickListener {
            if(data[position].status != 2){
                dialog = BanedDialog(mContext, data[position].user_id, ConstantUtils.ACTIONBAN)

                dialog!!.message.setText("Yakin untuk baned ${data[position].username}")
                dialog!!.interfaceRefresh(object : BanedDialog.InterfaceRefresh {
                    override fun void(status: Boolean) {
                        interfaceRefresh!!.void(status)
                    }

                })

                dialog!!.show()
            } else {
                dialog = BanedDialog(mContext, data[position].user_id, ConstantUtils.ACTIONUNBAN)

                dialog!!.message.setText("Yakin untuk un baned ${data[position].username}")
                dialog!!.interfaceRefresh(object : BanedDialog.InterfaceRefresh {
                    override fun void(status: Boolean) {
                        interfaceRefresh!!.void(status)
                    }

                })

                dialog!!.show()
            }
        }
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val image = v.findViewById(R.id.img_profile) as CircleImageView
        val username = v.findViewById(R.id.name) as TextView
        val like = v.findViewById(R.id.jumlah) as TextView
        val menu = v.findViewById(R.id.menu) as ImageView
        val baned = v.findViewById(R.id.status) as TextView
    }

    interface InterfaceRefresh {
        fun void(status: Boolean)
    }
}