package com.nsa.alarmapp.ui.frags


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nsa.alarmapp.R
import com.nsa.alarmapp.Util
import com.nsa.alarmapp.Util.cancelAlarm
import com.nsa.alarmapp.Util.convertTo12Hours
import com.nsa.alarmapp.databinding.FragmentAlarmBinding
import com.nsa.alarmapp.db.AlarmModel
import com.nsa.alarmapp.ui.adapter.WeekDayAdapter
import com.nsa.alarmapp.ui.model.WeekDayModel
import com.nsa.alarmapp.viewmodel.AlarmViewModel
import com.nsa.alarmapp.viewmodel.MainViewModelFactory
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*

class AlarmFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: FragmentAlarmBinding
    val args: AlarmFragmentArgs by navArgs()
    private val weekDayList= arrayListOf<WeekDayModel>(
        WeekDayModel("Mon",false),
        WeekDayModel("Tue",false),
        WeekDayModel("Wed",false),
        WeekDayModel("Thu",false),
        WeekDayModel("Fri",false),
        WeekDayModel("Sat",false),
        WeekDayModel("Sun",false),
        )

    lateinit var viewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_alarm, container, false)
        return binding.root
    }

    private var repeatEveryDay=false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel= ViewModelProvider(this, MainViewModelFactory(requireActivity().application)).get(AlarmViewModel::class.java)


        binding.alarmNameEd.setText(args.alarmModel.alarmName)
        time=args.alarmModel.time!!
        if(time.isNotEmpty()) {
            binding.timeEd.setText("${convertTo12Hours(time)}")
        }

        if(!args.alarmModel.repeatList.isNullOrEmpty()){
            weekDayList.clear()
            weekDayList.addAll(args.alarmModel.repeatList)
            
            binding.deleteBtn.visibility=View.VISIBLE
            repeatEveryDay= checkRepeatList()
            changeRepeatSwitch()
        }

        binding.deleteBtn.setOnClickListener {
            viewModel.delete(args.alarmModel)
            cancelAlarm(args.alarmModel.id,requireActivity())
            findNavController().popBackStack()

        }

        binding.closeBtn.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.weekDaysRv.adapter=WeekDayAdapter(
            weekDayList
            ,object :WeekDayAdapter.OnClickListener{
                override fun onClick(position: Int) {
                    weekDayList[position].selected=!weekDayList[position].selected
                    (binding.weekDaysRv.adapter as WeekDayAdapter).notifyItemChanged(position)

                    if(!weekDayList[position].selected){
                        repeatEveryDay=false
                        changeRepeatSwitch()
                    }else{
                       repeatEveryDay= checkRepeatList()
                        changeRepeatSwitch()
                    }

                }

            }
        )
        binding.saveBtn.setOnClickListener {

            var alarmName=binding.alarmNameEd.text.toString().trim()
           if(alarmName.isEmpty()){
               alarmName="Alarm"
           }
           if(args.alarmModel.alarmName!!.isEmpty()){
               val alarmModel=AlarmModel(
                   id = 0,
                   alarmName = alarmName,
                   time = time,
                   isOn = true,
                   repeatList = weekDayList
               )
           viewModel.insert(
               alarmModel =alarmModel
           )
               viewModel.alarmIdLiveData.observe(viewLifecycleOwner){

                   alarmModel.id=it

                   Util.checkAlarmAndSetAccordingly(alarmModel,requireContext(),false)


                   findNavController().popBackStack()
               }

           }else{
               cancelAlarm(args.alarmModel.id,requireActivity())
               val alarmModel=AlarmModel(
                   id = args.alarmModel.id,
                   alarmName = alarmName,
                   time = time,
                   isOn = true,
                   repeatList = weekDayList
               )
               viewModel.update(
                   alarmModel
               )
               Util.checkAlarmAndSetAccordingly(alarmModel,requireContext(),false)
               findNavController().popBackStack()
           }

        }


       binding.everyDaySwitch.setOnClickListener {
           repeatEveryDay=!repeatEveryDay
           weekDayList.forEach {
               it.selected=repeatEveryDay
           }
           (binding.weekDaysRv.adapter as WeekDayAdapter).notifyDataSetChanged()

           changeRepeatSwitch()
       }
        
        
        

        binding.timeEd.setOnClickListener {
            val c = Calendar.getInstance()

            var hour = c.get(Calendar.HOUR_OF_DAY)
            var minute = c.get(Calendar.MINUTE)
            if(!time.isEmpty()){
                hour=time.split(":")[0].toInt()
                minute= time.split(":")[1].toInt()
            }


            val timePickerDialog: TimePickerDialog = TimePickerDialog.newInstance(
                this@AlarmFragment
                ,hour
                ,minute
                ,false
                )
            timePickerDialog.accentColor=Color.parseColor("#B67272")
            timePickerDialog.show(childFragmentManager, "Datepickerdialog")
        }
    }




    private fun checkRepeatList(): Boolean {
        weekDayList.forEach {
            if(!it.selected){
                return false
            }
        }
        return true

    }

    private fun changeRepeatSwitch() {
        if(repeatEveryDay){
            binding.everyDaySwitch.setImageDrawable(requireContext().getDrawable(R.drawable.switch_on))
        }else{
            binding.everyDaySwitch.setImageDrawable(requireContext().getDrawable(R.drawable.switch_off))
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()
    }


    private var time="00:00"
    override fun onTimeSet(view: TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int) {
        time="$hourOfDay:$minute"
        binding.timeEd.setText("${convertTo12Hours(time)}")
    }
}