package com.example.coinsa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.coinsa.Api.RetrofitBuilder
import com.example.coinsa.adapter.MarketAdapter
import com.example.coinsa.adapter.TopMarketAdapter
import com.example.coinsa.databinding.FragmentHomeBinding
import com.example.coinsa.databinding.FragmentTopLossGainBinding
import com.example.coinsa.factory.MarketFactory
import com.example.coinsa.model.CryptoCurrency
import com.example.coinsa.repo.MarketRepo
import com.example.coinsa.sealed.ApiResponse
import com.example.coinsa.viewModel.MarketViewModel
import java.util.ArrayList
import java.util.Collections


class TopLossGainFragment : Fragment() {

    private val topLossGainFragment by lazy {
        FragmentTopLossGainBinding.inflate(layoutInflater)
    }

    private val marketRepository by lazy {
        MarketRepo(
            RetrofitBuilder.getInstance(requireActivity().application)!!.api,
            requireActivity().application
        )
    }

    private val marketViewModel by lazy {
        ViewModelProvider(requireActivity(), MarketFactory(marketRepository))[MarketViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        init()
        return topLossGainFragment.root
    }


    fun init(){

        val position=requireArguments().getInt("position")

        marketViewModel.topMarketObserver()
        marketViewModel.marketDataLiveData.observe(requireActivity()){
            when(it){
                is ApiResponse.Loading ->{}
                is ApiResponse.Success ->{

                    val dataItem=it.data?.data?.cryptoCurrencyList
                    Collections.sort(dataItem){
                        o1,o2 ->
                        (o2.quotes?.get(0)?.percentChange24h?.toInt())?.compareTo(o1.quotes?.get(0)?.percentChange24h!!.toInt())!!

                    }

                    topLossGainFragment.spinKitView.visibility=View.GONE

                    val list=ArrayList<CryptoCurrency>()
                    if (position==0){
                        list.clear()
                        for (i in 0..9){
                            list.add(dataItem?.get(i)!!)

                            topLossGainFragment.topGainLoseRecyclerView.adapter=MarketAdapter(requireActivity(),list,"home")


                        }
                    }else{

                        for (i in 0..9){
                            list.add(dataItem?.get(dataItem.size-1-i)!!)

                            topLossGainFragment.topGainLoseRecyclerView.adapter=MarketAdapter(requireActivity(),list,"home")


                        }

                    }

                }
                is ApiResponse.Error ->{}
            }
        }


    }
}