package com.example.coinsa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coinsa.R
import com.example.coinsa.databinding.TopCurrencyLayoutBinding
import com.example.coinsa.model.CryptoCurrency

class TopMarketAdapter(val context: Context,val list: List<CryptoCurrency>) : RecyclerView.Adapter<TopMarketAdapter.ViewHolder>(){
   inner class ViewHolder(view : View)  : RecyclerView.ViewHolder(view){
       var binding= TopCurrencyLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMarketAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.top_currency_layout,parent,false))
    }

    override fun onBindViewHolder(holder: TopMarketAdapter.ViewHolder, position: Int) {

        val item=list[position]
        holder.binding.topCurrencyNameTextView.text=item.name
        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png").thumbnail(Glide.with(context).load(R.drawable.spinner)).into(holder.binding.topCurrencyImageView)

        if (item.quotes!![0]?.percentChange24h!! > 0){
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.topCurrencyChangeTextView.text="+ ${String.format("%.02f",item.quotes[0]!!.percentChange24h)} %"
        }else{
            holder.binding.topCurrencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.topCurrencyChangeTextView.text=" ${String.format("%.02f",item.quotes[0]!!.percentChange24h)} %"
        }
    }

    override fun getItemCount(): Int {

        return list.size
    }
}