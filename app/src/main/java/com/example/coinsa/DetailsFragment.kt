package com.example.coinsa

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.coinsa.databinding.FragmentDetailsBinding
import com.example.coinsa.databinding.FragmentHomeBinding
import com.example.coinsa.model.CryptoCurrency

class DetailsFragment : Fragment() {

    private val detailsFragment by lazy {
        FragmentDetailsBinding.inflate(layoutInflater)
    }

    private val item:DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val data : CryptoCurrency = item.data!!

        init(data)
        loadGraph(data)
        setOnClick(data)
        return detailsFragment.root
    }


    fun setOnClick(item:CryptoCurrency){
        val oneMonth=detailsFragment.button
        val oneWeek=detailsFragment.button1
        val oneDay=detailsFragment.button2
        val fourHour=detailsFragment.button3
        val oneHour=detailsFragment.button4
        val fifteenMinute=detailsFragment.button5
        val clickListener=View.OnClickListener {
            when(it.id) {
                oneMonth.id -> loadGraphData(it,"M",item,oneDay,oneWeek,oneHour,fourHour,fifteenMinute)
                oneWeek.id ->  loadGraphData(it,"W",item,oneDay,oneMonth,oneHour,fourHour,fifteenMinute)
                oneDay.id ->  loadGraphData(it,"D",item,oneMonth,oneWeek,oneHour,fourHour,fifteenMinute)
                fourHour.id ->  loadGraphData(it,"4H",item,oneDay,oneWeek,oneHour,oneMonth,fifteenMinute)
                oneHour.id ->  loadGraphData(it,"1H",item,oneDay,oneWeek,oneMonth,fourHour,fifteenMinute)
                fifteenMinute.id ->  loadGraphData(it,"15",item,oneDay,oneWeek,oneHour,fourHour,oneMonth)
                else -> loadGraph(item)

            }
        }
        oneMonth.setOnClickListener(clickListener)
        oneWeek.setOnClickListener(clickListener)
        oneDay.setOnClickListener(clickListener)
        fourHour.setOnClickListener(clickListener)
        oneHour.setOnClickListener(clickListener)
        fifteenMinute.setOnClickListener(clickListener)

    }

    private fun loadGraphData(
        it: View?,
        s: String,
        item: CryptoCurrency,
        oneDay: AppCompatButton,
        oneWeek: AppCompatButton,
        oneHour: AppCompatButton,
        fourHour: AppCompatButton,
        fifteenMinute: AppCompatButton
    ) {
        disableButton(oneDay,oneWeek,oneHour,fourHour,fifteenMinute)

        it!!.setBackgroundResource(R.drawable.active_button)
        // load web view

        detailsFragment.detaillChartWebView.settings.javaScriptEnabled=true
        detailsFragment.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        val  url :String= "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d80&symbol=" + item.symbol
            .toString() + "USD&interval="+s+ "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

        detailsFragment.detaillChartWebView.loadUrl(url)

    }

    private fun disableButton(oneDay: AppCompatButton, oneWeek: AppCompatButton, oneHour: AppCompatButton, fourHour: AppCompatButton, fifteenMinute: AppCompatButton) {

        oneDay.background=null
        oneWeek.background=null
        oneHour.background=null
        fourHour.background=null
        fifteenMinute.background=null
    }


    fun loadGraph(data:CryptoCurrency){

        // load web view
        detailsFragment.detaillChartWebView.settings.javaScriptEnabled=true
        detailsFragment.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        val  url :String= "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d80&symbol=" + data.symbol
            .toString() + "USD&interval=D"  + "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

        detailsFragment.detaillChartWebView.loadUrl(url)
    }

    fun init(data:CryptoCurrency){

        detailsFragment.detailSymbolTextView.text=data.symbol
        Glide.with(requireActivity()).load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+data.id+".png").thumbnail(
            Glide.with(requireActivity()).load(R.drawable.spinner)).into(detailsFragment.detailImageView)

        detailsFragment.detailPriceTextView.text="$ "+"${String.format("%.02f", data.quotes?.get(0)?.price)}"
        if (data.quotes!![0]?.percentChange24h!! > 0){
            detailsFragment.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            detailsFragment.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
            detailsFragment.detailChangeTextView.text="+ ${String.format("%.02f",data.quotes[0]!!.percentChange24h)} %"
        }else{
            detailsFragment.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            detailsFragment.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)

            detailsFragment.detailChangeTextView.text=" ${String.format("%.02f",data.quotes[0]!!.percentChange24h)} %"
        }

    }
}