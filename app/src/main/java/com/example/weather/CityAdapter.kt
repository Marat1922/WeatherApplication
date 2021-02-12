package com.example.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber


class CityAdapter(
        private val cityNameList: List<City>,
        private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    interface OnItemClickListener {
       fun onItemClick(cityName: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        Timber.d("CityAdapter onCreateViewHolder")
        return CityViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        Timber.d("CityAdapter onBindViewHolder")
        val cityNameItem = cityNameList[position]
        holder.cityNameTextView?.text = cityNameItem.name
        Timber.d("CityAdapter cityNameTextView.setText")
        holder.resultWeatherTextView?.text = cityNameItem.temper
        Timber.d("Че хранит мапа ${cityNameItem}${cityNameItem.temper}")
        if (cityNameItem.temper == null) onItemClickListener.onItemClick(cityNameItem.name!!)
        Timber.d("CityAdapter onItemClickListener.onItemClick")
    }

    override fun getItemCount(): Int {
        Timber.d("CityAdapter getItemCount")
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
        Timber.d("CityAdapter onTemperatureArrived")
        val index = cityNameList.indexOfFirst { it.name == cityName }
        if (index == -1) return
        cityNameList[index].temper = temperature
        notifyItemChanged(index)
    }
}