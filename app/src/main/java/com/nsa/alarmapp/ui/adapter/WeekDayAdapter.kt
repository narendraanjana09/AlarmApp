package com.nsa.alarmapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nsa.alarmapp.R
import com.nsa.alarmapp.databinding.WeekdaysItemLayoutBinding
import com.nsa.alarmapp.ui.model.WeekDayModel

class WeekDayAdapter(
    private val list:List<WeekDayModel>,
    private val onClickListener: OnClickListener
    ):
    RecyclerView.Adapter<WeekDayAdapter.ViewHolder>() {

    interface OnClickListener{
        fun onClick(position: Int)
    }

   inner class ViewHolder(private val binding: WeekdaysItemLayoutBinding): RecyclerView.ViewHolder(binding.root)  {
       init {
           binding.root.setOnClickListener{
               onClickListener.onClick(adapterPosition)
           }
       }
        fun setData(weekDayModel: WeekDayModel) {
            binding.textView.text=weekDayModel.day


            if(weekDayModel.selected){
                binding.textView.setTextColor(binding.root.context.getColor(R.color.white))
                binding.root.setCardBackgroundColor(binding.root.context.getColor(R.color.primary))
            }else{
                binding.textView.setTextColor(binding.root.context.getColor(R.color.black))
                binding.root.setCardBackgroundColor(binding.root.context.getColor(R.color.white))
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = WeekdaysItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}