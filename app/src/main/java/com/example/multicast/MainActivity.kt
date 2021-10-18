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
import com.example.multicast.databinding.FragmentFirstBinding
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var multicastGroup: String = "224.0.0.10"
    private var multicastPort: Int = 8888
    private var client: Client = Client(multicastGroup,multicastPort)
    private var serverThread = Server(multicastGroup, multicastPort)
    private var tcpServer: TCPServer? = null;
    private var tcpClient: TCPClient? = null;

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



        var clientThread = Client(multicastGroup,multicastPort)
        Thread(clientThread).start()

        Thread(serverThread).start()



    }

    fun startTCPClient(binding: FragmentFirstBinding): Void? {
        tcpClient = TCPClient(binding)
        return null;
    }

    fun startTCPServer(binding: FragmentFirstBinding): Void? {
        tcpServer = TCPServer(binding)
        return null;
    }

    fun stopTCPServer(): Void? {
        tcpServer?.closeSocket();
        return null;
    }

    fun getAvailableDevicesString(): String {
        var str = ""
        var myIp = client.getIpAddress()
        for (device in serverThread.availableDevices) {
            var hostName = if (device == myIp)  "My device" else InetAddress.getByName(device).hostName;
            var strToAdd = "$hostName: $device, \n\n"
            str = str.plus(strToAdd)
        }
        return str
    }

    fun getAvailableDevices(): MutableList<String> {
        return serverThread.availableDevices
    }

    fun sendData(): Void? {
        client.sendMulticastData()
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