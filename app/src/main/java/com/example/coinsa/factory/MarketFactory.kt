package com.example.coinsa.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinsa.repo.MarketRepo
import com.example.coinsa.viewModel.MarketViewModel

class MarketFactory(val marketRepo: MarketRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MarketViewModel::class.java)){
            MarketViewModel(this.marketRepo) as T
        }else{
            throw IllegalArgumentException("View Model Not Found")
        }
    }

}