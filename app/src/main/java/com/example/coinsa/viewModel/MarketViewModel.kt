package com.example.coinsa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coinsa.repo.MarketRepo
import kotlinx.coroutines.launch

class MarketViewModel(val marketRepo: MarketRepo) :ViewModel() {


    val marketDataLiveData = marketRepo.marketData
     fun topMarketObserver(){
         viewModelScope.launch {
             marketRepo.marketApi()
         }
    }
}