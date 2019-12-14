package rpl.ezy.olread.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import rpl.ezy.olread.GlideApp
import rpl.ezy.olread.R
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.utils.ConstantUtils
import rpl.ezy.olread.view.RecipeDetailActivity

class AcceptedRecipesAdapter(var mContext: Context, var data: ArrayList<MRecipe>): RecyclerView.Adapter<AcceptedRecipesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_vertical, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        GlideApp.with(mContext)
            .load(data[position].img_url)
            .transform(RoundedCorners(8))
            .into(holder.img_item)
        holder.tv_title.text = data[position].title
        holder.tv_kategori.text = data[position].kategori
        if(data[position].like != 0){
            holder.tv_like.text = "${data[position].like} likes"
        }

        Log.d("TES_RECIPES", "${data[position].recipe}")

        holder.itemView.setOnClickListener {
            (mContext as Activity).startActivity(
                Intent(mContext, RecipeDetailActivity::class.java)
                    .putExtra(ConstantUtils.RECIPE_ID, data[position].recipe_id))
        }

    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        var img_item = v.findViewById(R.id.img_item) as ImageView
        var tv_title = v.findViewById(R.id.tv_title) as TextView
        var tv_kategori = v.findViewById(R.id.tv_kategori) as TextView
        var tv_like = v.findViewById(R.id.tv_like) as TextView
    }

}