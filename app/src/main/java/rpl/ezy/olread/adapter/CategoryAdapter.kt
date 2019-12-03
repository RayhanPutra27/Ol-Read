package rpl.ezy.olread.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import rpl.ezy.olread.R
import rpl.ezy.olread.model.MRecipe
import rpl.ezy.olread.response.ResponseCategory
import rpl.ezy.olread.response.ResponseRecipes

class CategoryAdapter(var context: Context, var data: ArrayList<MRecipe>):
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cat.text = data[position].kategori
        holder.itemView.setOnClickListener {
            Toast.makeText(context, data[position].kategori, Toast.LENGTH_SHORT).show()
        }
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var cat = v.findViewById(R.id.cat) as TextView
    }
}