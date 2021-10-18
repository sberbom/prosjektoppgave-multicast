package com.example.multicast

import android.os.Handler
import com.example.multicast.databinding.FragmentFirstBinding
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket

class TCPServer(binding: FragmentFirstBinding) {

    private val fragmentFirstBindingbinding: FragmentFirstBinding
    private var serverSocket: ServerSocket? = null
    var serverThread: Thread? = null
    var updateConversationHandler: Handler

    fun closeSocket() {
        try {
            serverSocket!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    internal inner class ServerThread : Runnable {
        override fun run() {
            var socket: Socket? = null
            try {
                println(SERVERPORT)
                serverSocket = ServerSocket(SERVERPORT)
            } catch (e: IOException) {
                e.printStackTrace()
            }
            while (!Thread.currentThread().isInterrupted) {
                try {
                    socket = serverSocket!!.accept()
                    val commThread = CommunicationThread(socket)
                    Thread(commThread).start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    internal inner class CommunicationThread(private val clientSocket: Socket?) :
        Runnable {
        private var input: BufferedReader? = null
        override fun run() {
            while (!Thread.currentThread().isInterrupted) {
                try {
                    val read = input!!.readLine()
                    updateConversationHandler.post(updateUIThread(read))
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        internal inner class updateUIThread(private val msg: String) : Runnable {
            override fun run() {
                fragmentFirstBindingbinding.textviewFirst.setText("Client says: $msg\n")
            }
        }

        init {
            try {
                input = BufferedReader(InputStreamReader(clientSocket!!.getInputStream()))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val SERVERPORT = 7000
    }

    init {
        serverThread = Thread(ServerThread())
        serverThread!!.start()
        fragmentFirstBindingbinding = binding
        updateConversationHandler = Handler()
    }
}