package com.example.weather

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber


class CityAdapter(
        private val cityNameList: ArrayList<City>,
        private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    lateinit var onCityClickListener: OnCityClickListener

    interface OnItemClickListener {
        fun onItemClick(cityName: String)
        fun onButtonClick()
    }

    interface OnCityClickListener {
        fun onCityClick(position: Int)
        fun onLongClick(position: Int)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        Timber.d("CityAdapter onCreateViewHolder")
        return if (viewType == VIEW_TYPE_ITEM) {
            CityItemViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item, parent, false))
        } else {
            ButtonViewHolder(Button(parent.context).apply {
                text = context.resources.getString(R.string.add_city)
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
            })
        }
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        when (holder) {
            is CityItemViewHolder -> {
                Timber.d("CityAdapter onBindViewHolder")
                val cityNameItem = cityNameList[position]
                holder.cityNameTextView?.text = cityNameItem.name
                Timber.d("CityAdapter cityNameTextView.setText")
                holder.resultWeatherTextView?.text = cityNameItem.temper
                Timber.d("Че хранит мапа ${cityNameItem}${cityNameItem.temper}")
                if (cityNameItem.temper == null) onItemClickListener.onItemClick(cityNameItem.name!!)
                Timber.d("CityAdapter onItemClickListener.onItemClick")
            }
            is ButtonViewHolder -> {
                holder.itemView.setOnClickListener {
                    onItemClickListener.onButtonClick()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        Timber.d("CityAdapter getItemCount")
        return cityNameList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == cityNameList.size) {
            VIEW_TYPE_BUTTON
        } else {
            VIEW_TYPE_ITEM
        }
    }

    fun onTemperatureArrived(cityName: String, temperature: String) {
        Timber.d("CityAdapter onTemperatureArrived")
        val index = cityNameList.indexOfFirst { it.name == cityName }
        if (index == -1) return
        cityNameList[index].temper = temperature
        notifyItemChanged(index)
    }

    companion object {
        const val VIEW_TYPE_BUTTON = 0
        const val VIEW_TYPE_ITEM = 1
    }

    open class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class CityItemViewHolder(itemView: View) : CityViewHolder(itemView) {
        var cityNameTextView: TextView? = null
        var resultWeatherTextView: TextView? = null

        init {
            cityNameTextView = itemView.findViewById(R.id.tv_city_name)
            resultWeatherTextView = itemView.findViewById(R.id.tv_result_weather)

            itemView.setOnLongClickListener {
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle(R.string.are_you_sure_you_want_to_remove_the_city_from_the_list)
                builder.setPositiveButton(R.string.yes) { dialog, which ->
                    cityNameList.removeAt(adapterPosition)
                    notifyDataSetChanged()
                }
                builder.setNegativeButton(R.string.no) { dialog, which ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                true
            }
        }
    }

    class ButtonViewHolder(itemView: View) : CityViewHolder(itemView)
}