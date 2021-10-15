package com.example.multicast

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.multicast.databinding.ActivityMainBinding
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket
import java.security.spec.ECField
import java.util.concurrent.ThreadLocalRandom

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var multicastGroup: String = "224.0.0.10"
    private var multicastPort: Int = 8888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    fun sendData(): Void? {
        var addr = InetAddress.getByName(multicastGroup)
        try {
            var serverSocket = DatagramSocket()
            for(i in 1..5) {
                var random = ThreadLocalRandom.current().nextInt(0,100).toString()
                var msg = "Sent message no $random";
                var msgPacket = DatagramPacket(msg.toByteArray(), msg.toByteArray().size, addr, multicastPort)
                serverSocket.send(msgPacket);

                println("Servers sendt packet with msg: $i");
                Thread.sleep(500)
            }
        }catch (e:Exception) {
            e.printStackTrace()
        }
        return null;
    }

    fun receiveData(): String? {
        var addr = InetAddress.getByName(multicastGroup);
        var buf = ByteArray(256)
        try{
            var clientSocket = MulticastSocket(multicastPort);
            clientSocket.joinGroup(addr)

            println("SUBSCRIBING")
            while (true) {
                var msgPacket = DatagramPacket(buf, buf.size)
                clientSocket.receive(msgPacket);

                var msg = String(buf, 0, buf.size);
                println("Socket received msg $msg");
                return msg;
            }
        }catch (e:Exception){
            e.printStackTrace();
        }

        return null;
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}