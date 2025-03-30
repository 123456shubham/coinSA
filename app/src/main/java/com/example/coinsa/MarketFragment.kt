package com.example.coinsa

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinsa.Api.RetrofitBuilder
import com.example.coinsa.adapter.MarketAdapter
import com.example.coinsa.adapter.TopMarketAdapter
import com.example.coinsa.databinding.FragmentMarketBinding
import com.example.coinsa.factory.MarketFactory
import com.example.coinsa.model.CryptoCurrency
import com.example.coinsa.repo.MarketRepo
import com.example.coinsa.sealed.ApiResponse
import com.example.coinsa.viewModel.MarketViewModel
import java.util.Locale


class MarketFragment : Fragment() {
    private val marketFragment by lazy {
        FragmentMarketBinding.inflate(layoutInflater)
    }

    private lateinit var list: List<CryptoCurrency>

    private lateinit var marketAdapter: MarketAdapter

    private val marketRepository by lazy {
        MarketRepo(
            RetrofitBuilder.getInstance(requireActivity().application)!!.api,
            requireActivity().application
        )
    }
    private lateinit var searchText:String

    private val postViewModel by lazy {
        ViewModelProvider(requireActivity(), MarketFactory(marketRepository))[MarketViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        list= listOf()
        init()
        searchCoin()
        return marketFragment.root
    }

    fun  init(){
        marketAdapter= MarketAdapter(requireActivity(),list,"market")

        marketFragment.currencyRecyclerView.setHasFixedSize(false)
        marketFragment.currencyRecyclerView.layoutManager = LinearLayoutManager(requireActivity(),
            LinearLayoutManager.VERTICAL,false)
        postViewModel.topMarketObserver()
        marketFragment.currencyRecyclerView.adapter=marketAdapter

        postViewModel.marketDataLiveData.observe(requireActivity()){
            when(it){
                is ApiResponse.Loading ->{}
                is ApiResponse.Success ->{
                    marketAdapter.updateData(it.data?.data!!.cryptoCurrencyList)
                    marketFragment.spinKitView.visibility=View.GONE
                }
                is ApiResponse.Error ->{}
            }
        }

    }

    fun searchCoin(){
        marketFragment.searchEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                searchText=p0.toString().lowercase()
                updateRecyclerView()

            }

        })
    }

    fun updateRecyclerView(){
        val data=ArrayList<CryptoCurrency>()
        for (item in list){
            val coinName=item.name!!.lowercase(Locale.getDefault())
            val coinSymbol=item.symbol!!.lowercase(Locale.getDefault())

            if (coinName.contains(searchText) || coinSymbol.contains(searchText)){
                data.add(item)
            }

        }
        marketAdapter.updateData(data)
    }

}