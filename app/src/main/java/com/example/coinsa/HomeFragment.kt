package com.example.coinsa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinsa.Api.RetrofitBuilder
import com.example.coinsa.adapter.TopMarketAdapter
import com.example.coinsa.databinding.FragmentHomeBinding
import com.example.coinsa.factory.MarketFactory
import com.example.coinsa.repo.MarketRepo
import com.example.coinsa.sealed.ApiResponse
import com.example.coinsa.viewModel.MarketViewModel

class HomeFragment : Fragment() {

    private val fragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val marketRepository by lazy {
        MarketRepo(
            RetrofitBuilder.getInstance(requireActivity().application)!!.api,
            requireActivity().application
        )
    }

    private val postViewModel by lazy {
        ViewModelProvider(requireActivity(), MarketFactory(marketRepository))[MarketViewModel::class.java]
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        init()
        return fragmentHomeBinding.root
    }


    fun  init(){
        fragmentHomeBinding.topCurrencyRecyclerView.setHasFixedSize(false)
        fragmentHomeBinding.topCurrencyRecyclerView.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false)
        postViewModel.topMarketObserver()

        postViewModel.marketDataLiveData.observe(requireActivity()){
            when(it){
                is ApiResponse.Loading ->{}
                is ApiResponse.Success ->{
                    fragmentHomeBinding.topCurrencyRecyclerView.adapter = TopMarketAdapter(requireActivity(),it.data?.data!!.cryptoCurrencyList)
                }
                is ApiResponse.Error ->{}
            }
        }

    }

}