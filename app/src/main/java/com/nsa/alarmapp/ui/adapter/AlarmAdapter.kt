package com.nsa.alarmapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nsa.alarmapp.R
import com.nsa.alarmapp.Util
import com.nsa.alarmapp.databinding.AlarmLayoutItemBinding
import com.nsa.alarmapp.databinding.WeekdaysItemLayoutBinding
import com.nsa.alarmapp.db.AlarmModel
import com.nsa.alarmapp.ui.model.WeekDayModel

class AlarmAdapter(
    private val list:List<AlarmModel>,
    private val onClickListener: OnClickListener
    ):
    RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    interface OnClickListener{
        fun onClick(position: Int)
        fun onClickSwitch(position: Int)
    }

   inner class ViewHolder(private val binding: AlarmLayoutItemBinding): RecyclerView.ViewHolder(binding.root)  {
       init {
           binding.root.setOnClickListener{
               onClickListener.onClick(adapterPosition)
           }
       }

       fun setData(alarmModel: AlarmModel) {
           binding.alarmDaysTv.text=Util.getDaysString(alarmModel.repeatList)
           binding.alarmNameTv.text=alarmModel.alarmName
           binding.alarmTimeTv.text= Util.convertTo12Hours(alarmModel.time!!)
           binding.root.setOnClickListener {
               onClickListener.onClick(adapterPosition)
           }
           binding.alarmSwitch.setOnClickListener {
               onClickListener.onClickSwitch(adapterPosition)
           }

           if(alarmModel.isOn!!){
               binding.alarmSwitch.setImageDrawable(binding.root.context.getDrawable(R.drawable.switch_on))
           }else{
               binding.alarmSwitch.setImageDrawable(binding.root.context.getDrawable(R.drawable.switch_off))
           }
       }

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = AlarmLayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

}