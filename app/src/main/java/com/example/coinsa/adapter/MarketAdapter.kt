package com.example.coinsa.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coinsa.HomeFragmentDirections
import com.example.coinsa.R
import com.example.coinsa.databinding.CurrencyItemLayoutBinding
import com.example.coinsa.model.CryptoCurrency

class MarketAdapter(val context: Context, val list: List<CryptoCurrency>) : RecyclerView.Adapter<MarketAdapter.ViewModel>() {

    inner  class  ViewModel (view: View): RecyclerView.ViewHolder(view){

        var binding= CurrencyItemLayoutBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketAdapter.ViewModel {
        return ViewModel(CurrencyItemLayoutBinding.inflate(LayoutInflater.from(context),parent,false).root)
    }

    override fun onBindViewHolder(holder: MarketAdapter.ViewModel, position: Int) {
        val item = list[position]

        holder.binding.currencyNameTextView.text=item.name

        Glide.with(context).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id+".png").thumbnail(
            Glide.with(context).load(R.drawable.spinner)).into(holder.binding.currencyImageView)

        Glide.with(context).load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+item.id+".png").thumbnail(
            Glide.with(context).load(R.drawable.spinner)).into(holder.binding.currencyChartImageView)

        holder.binding.currencyPriceTextView.text="${String.format("%.02f", item.quotes?.get(0)?.price)}"
        if (item.quotes!![0]?.percentChange24h!! > 0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text="+ ${String.format("%.02f",item.quotes[0]!!.percentChange24h)} %"
        }else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text=" ${String.format("%.02f",item.quotes[0]!!.percentChange24h)} %"
        }
        holder.itemView.setOnClickListener {
            findNavController(it).navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(item))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
