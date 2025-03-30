package com.example.coinsa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.coinsa.Api.RetrofitBuilder
import com.example.coinsa.adapter.TopLossGainAdapter
import com.example.coinsa.adapter.TopMarketAdapter
import com.example.coinsa.databinding.FragmentHomeBinding
import com.example.coinsa.factory.MarketFactory
import com.example.coinsa.repo.MarketRepo
import com.example.coinsa.sealed.ApiResponse
import com.example.coinsa.viewModel.MarketViewModel
import com.google.android.material.tabs.TabLayoutMediator

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
        setTabLayout()
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

    fun setTabLayout(){

        val adapter=TopLossGainAdapter(this)
        fragmentHomeBinding.contentViewPager.adapter=adapter
        fragmentHomeBinding.contentViewPager.registerOnPageChangeCallback(object  : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position==0){

                    fragmentHomeBinding.topGainIndicator.visibility=View.VISIBLE
                    fragmentHomeBinding.topLoseIndicator.visibility=View.GONE
                }else{
                    fragmentHomeBinding.topGainIndicator.visibility=View.GONE
                    fragmentHomeBinding.topLoseIndicator.visibility=View.VISIBLE
                }
            }
        })

        TabLayoutMediator(fragmentHomeBinding.tabLayout,fragmentHomeBinding.contentViewPager){
            tab,position ->
            var title= if (position==0){
                "Top Gainers"
            }else{
                "Top Losers"

            }

            tab.text=title
        }.attach()
    }


}