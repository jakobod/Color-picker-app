package com.example.tabbing;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.example.tabbing.ui.main.listFragment.RecyclerViewAdapter;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Set;

public class WifiTask extends AsyncTask<Void, String, Void> {
    public static Set<InetAddress> hosts = new HashSet<>();
    public static InetAddress endpoint = null;
    private static final int port = 23;
    private static final int localPort = 2000;

    private static String messageBytes;
    private static DatagramSocket socket;
    private static final String BROADCAST_MSG = "/LAMPS_GATHER_AND_ANSWER";
    private boolean disconnectSignal = false;

    @Override
    protected Void doInBackground(Void... arg) {
        long ts = System.currentTimeMillis();
        endpoint = null;
        try {
            socket = new DatagramSocket(localPort);
            socket.setBroadcast(true);
            socket.setSoTimeout(10);

            while (!disconnectSignal) {
                handleOutgoingMessages();
                handleIncomingMessages();
                long now = System.currentTimeMillis();
                if (now - ts > 5000) {
                    ts = now;
                    broadcast();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null)
                socket.close();
        }


        return null;
    }

    public static void send(String data) {
        messageBytes = data;
    }


    public static void broadcast() {
        try {
            byte[] bytes = BROADCAST_MSG.getBytes();
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length, getBroadcastAddress(), port);
            socket.send(packet);
            System.out.println("broadcast!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static InetAddress getBroadcastAddress() throws IOException {
        WifiManager wifiManager = (WifiManager) MainActivity.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifiManager.getDhcpInfo();

        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] quads = new byte[4];
        for (int k = 0; k < 4; k++)
            quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        return InetAddress.getByAddress(quads);
    }

    private void handleOutgoingMessages() {
        if (endpoint == null) {
            System.out.println("endpoint is null!");
            return;
        }
        try {
            byte[] bytes = messageBytes.getBytes();
                DatagramPacket packet = new DatagramPacket(bytes, bytes.length, endpoint, port);
            socket.send(packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIncomingMessages() {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        try {
            socket.receive(packet);
            System.out.println("received answer from " + packet.getAddress());
            hosts.add(packet.getAddress());
            RecyclerViewAdapter.addItems(hosts);
            MainActivity.updateRecyclerView();
        } catch (SocketTimeoutException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void requestDisconnect() {
        disconnectSignal = true;
    }


}
