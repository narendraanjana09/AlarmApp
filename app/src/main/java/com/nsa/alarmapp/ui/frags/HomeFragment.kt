package com.nsa.alarmapp.ui.frags

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.nsa.alarmapp.R
import com.nsa.alarmapp.Util
import com.nsa.alarmapp.databinding.FragmentHomeBinding
import com.nsa.alarmapp.db.AlarmModel
import com.nsa.alarmapp.ui.adapter.AlarmAdapter
import com.nsa.alarmapp.ui.model.WeekDayModel
import com.nsa.alarmapp.viewmodel.AlarmViewModel
import com.nsa.alarmapp.viewmodel.MainViewModelFactory
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    lateinit var viewModel: AlarmViewModel
    private val alarmList= arrayListOf<AlarmModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        return binding.root
    }
    private fun showToast(message: String) {
        Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel=ViewModelProvider(this,MainViewModelFactory(requireActivity().application)).get(AlarmViewModel::class.java)

        binding.alarmsRv.adapter=AlarmAdapter(alarmList,object :AlarmAdapter.OnClickListener{
            override fun onClick(position: Int) {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAlarmFragment(
                        alarmList[position]
                    )
                )
            }

            override fun onClickSwitch(position: Int) {
                alarmList[position].isOn=!alarmList[position].isOn!!
                if(alarmList[position].isOn==true){
                    Util.checkAlarmAndSetAccordingly(alarmList[position],requireActivity(),false)
                }else{
                    Util.cancelAlarm(alarmList[position].id,requireActivity())
                }


                viewModel.update(alarmList[position])
                (binding.alarmsRv.adapter as AlarmAdapter).notifyItemChanged(position)
            }

        })

        viewModel.getAllAlarms().observe(viewLifecycleOwner){
            alarmList.clear()
            alarmList.addAll(it)

            (binding.alarmsRv.adapter as AlarmAdapter).notifyDataSetChanged()


        }


        binding.addAlarmBtn.setOnClickListener{
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAlarmFragment(
                    AlarmModel(
                        0,"","",false, arrayListOf<WeekDayModel>()
                    )
                )
            )
        }

    }
}