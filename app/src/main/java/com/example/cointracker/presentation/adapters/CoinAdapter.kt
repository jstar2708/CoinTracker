package com.example.cointracker.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cointracker.R
import com.example.cointracker.domain.model.Coin
import com.example.cointracker.domain.model.CoinDetail
import com.example.cointracker.domain.model.CoinDetailsList
import com.example.cointracker.domain.model.CoinExchangeRates

class CoinAdapter(): RecyclerView.Adapter<CoinViewHolder>() {

    private val list: ArrayList<Coin> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.coin_layout, parent, false)
        return CoinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        Glide.with(holder.icon.context).load(list[position].icon).into(holder.icon)
        holder.name.text = list[position].name
        holder.symbol.text = list[position].symbol
        holder.rate.text = list[position].rate
    }

    fun updateList(tempList: ArrayList<Coin>) {
        list.clear()
        list.addAll(tempList)
        notifyDataSetChanged()
    }
}

class CoinViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val icon: ImageView = itemView.findViewById(R.id.icon)
    val symbol: TextView = itemView.findViewById(R.id.symbol)
    val name: TextView = itemView.findViewById(R.id.name)
    val rate: TextView = itemView.findViewById(R.id.rate)
}