package com.example.multicast

import android.view.View
import com.example.multicast.databinding.FragmentFirstBinding
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.lang.Exception
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException
import java.util.concurrent.ThreadLocalRandom

class TCPClient(binding: FragmentFirstBinding) {
    private var socket: Socket? = null
    private val fragmentFirstBinding: FragmentFirstBinding

    internal inner class ClientThread : Runnable {
        override fun run() {
            try {

                fragmentFirstBinding.buttonConnect1.setOnClickListener(View.OnClickListener {
                    val ip = fragmentFirstBinding.buttonConnect1.text;
                    println("Button1")
                    sendMsg(ip as String)
                })
                fragmentFirstBinding.buttonConnect2.setOnClickListener(View.OnClickListener {
                    val ip = fragmentFirstBinding.buttonConnect2.text;
                    sendMsg(ip as String)
                })
                fragmentFirstBinding.buttonConnect3.setOnClickListener(View.OnClickListener {
                    val ip = fragmentFirstBinding.buttonConnect3.text;
                    sendMsg(ip as String)
                })

            } catch (e1: UnknownHostException) {
                e1.printStackTrace()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        }
    }

    fun sendMsg(serverIp: String): Void? {
        try {
            println("TESTING ---------")
            val serverAddr = InetAddress.getByName(serverIp)
            socket = Socket(serverAddr, SERVERPORT)
            val str = ThreadLocalRandom.current().nextInt(0, 100).toString()
            val out = PrintWriter(
                BufferedWriter(
                    OutputStreamWriter(
                        socket!!.getOutputStream()
                    )
                ), true
            )
            println("SENDER : $str----------------")
            out.println(str)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    companion object {
        private const val SERVERPORT = 7000
    }

    init {
        Thread(ClientThread()).start()
        fragmentFirstBinding = binding
    }
}