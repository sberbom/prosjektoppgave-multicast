package com.example.multicast

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.multicast.databinding.FragmentFirstBinding
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.widget.TextView


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonSend.setOnClickListener {
            (activity as MainActivity).sendData()
        }



        binding.buttonSubscribe.setOnClickListener {
            var devicesString = (activity as MainActivity).getAvailableDevicesString()
            binding.textMsg.text = devicesString

            var availableDevices = (activity as MainActivity).getAvailableDevices()
            var counter = 1
            for(device in availableDevices) {
                if(counter == 1 ) {
                    binding.buttonConnect1.text = device;
                }
                else if(counter == 2 ) {
                    binding.buttonConnect2.text = device;
                }
                else if(counter == 3 ) {
                    binding.buttonConnect3.text = device;
                }
                counter++;
            }
        }

        (activity as MainActivity).startTCPServer(binding);

        (activity as MainActivity).startTCPClient(binding);


    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).stopTCPServer();
        _binding = null
    }
}