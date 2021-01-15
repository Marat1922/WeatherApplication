package com.example.weather

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class CityAdapter(
        private val cityNameList: List<City>,
        private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(cityName: String) {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        Log.d("MyTag", "CityAdapter onCreateViewHolder" )
        return CityViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        Log.d("MyTag", "CityAdapter onBindViewHolder" )
        val cityNameItem = cityNameList.get(position)
        holder.cityNameTextView?.setText(cityNameItem.name)
        Log.d("MyTag", "CityAdapter cityNameTextView.setText")
        holder.resultWeatherTextView?.setText(cityNameItem.temper)
        Log.d("MyTag", "Че хранит мапа ${cityNameItem}${cityNameItem.temper}")
        if(cityNameItem.temper == null) onItemClickListener.onItemClick(cityNameItem.name!!)
            Log.d("MyTag", "CityAdapter onItemClickListener.onItemClick")
    }

    override fun getItemCount(): Int {
        Log.d("MyTag", "CityAdapter getItemCount" )
        return cityNameList.size
    }

    class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cityNameTextView: TextView? = null
        var resultWeatherTextView: TextView? = null

        init {
            cityNameTextView = itemView.findViewById(R.id.tv_city_name)
            resultWeatherTextView = itemView.findViewById(R.id.tv_result_weather)
        }
    }

    fun onTemperatureArrived(cityName: String, temperature: String) {
        Log.d("MyTag", "CityAdapter onTemperatureArrived" )
       val index =  cityNameList.indexOfFirst { it.name == cityName }
        if (index == -1) return
        cityNameList.get(index).temper = temperature
        // Log.d("CityAdapter", "Че хранит мапа $temperature")
        notifyItemChanged(index)

    }
}