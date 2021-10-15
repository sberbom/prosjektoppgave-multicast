package com.example.multicast

import java.lang.Exception
import java.net.*
import java.util.*

class Client(multicastGroup: String, multicastPort: Int): Runnable {

    var multicastPort: Int = multicastPort;
    var multicastGroup: String = multicastGroup;

    fun sendData(): Void? {
        var addr = InetAddress.getByName(multicastGroup)
        try {
            var serverSocket = DatagramSocket()
            var msg = "${getIpAddress()}"
            var msgPacket = DatagramPacket(msg.toByteArray(), msg.toByteArray().size, addr, multicastPort)
            serverSocket.send(msgPacket);
        }catch (e: Exception) {
            e.printStackTrace()
        }
        return null;
    }

    override fun run() {
        try{
            while (true){
                println("Sending data")
                sendData()
                Thread.sleep(2000)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    private fun getIpAddress(): String? {
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val intf: NetworkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = intf.inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        return inetAddress.hostAddress
                    }
                }
            }
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
        return null
    }
}