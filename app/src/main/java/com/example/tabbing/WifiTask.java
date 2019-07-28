package com.example.tabbing;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;

import com.example.tabbing.ui.main.listFragment.RecyclerViewAdapter;

import java.io.IOException;
import java.net.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WifiTask extends AsyncTask<Void, String, Void> {
  // -- constants --------------------------------------------------------------
  private static final int PORT = 23;
  private static final int LOCAL_PORT = 2000;

  private static final int SHORT_TIMESPAN = 500;
  private static final int LONG_TIMESPAN = 10000;

  private static final InetAddress BROADCAST_ADDRESS = getBroadcastAddress();
  private static final String BROADCAST_MSG = "/LAMPS_GATHER_AND_ANSWER";

  // -- nessecary fields -------------------------------------------------------

  private static DatagramSocket socket;

  public static Set<InetAddress> hosts = new HashSet<>();
  private static InetAddress endpoint = null;

  private static byte[] messageBytes = null;

  private int timespan = SHORT_TIMESPAN;
  private boolean running = true;

  // -- members ----------------------------------------------------------------

  @Override
  protected Void doInBackground(Void... arg) {
    long ts = System.currentTimeMillis();
    endpoint = null;

    try {
      socket = new DatagramSocket(LOCAL_PORT);
      socket.setBroadcast(true);
      socket.setSoTimeout(10);
    } catch (IOException e) {
      e.printStackTrace();
    }

    while (running) {
      handleOutgoingMessages();
      handleIncomingMessages();
      long now = System.currentTimeMillis();
      if (now - ts > timespan) {
        ts = now;
        broadcast();
      }
    }
    if (socket != null)
    socket.close();

    return null;
  }

  public static void send(String data) {
    messageBytes = data.getBytes();
  }

  private void broadcast() {
    try {
      byte[] bytes = BROADCAST_MSG.getBytes();
      DatagramPacket packet = new DatagramPacket(bytes, bytes.length,
                                                 BROADCAST_ADDRESS, PORT);
      socket.send(packet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static InetAddress getBroadcastAddress() {
    WifiManager wifiManager = (WifiManager) MainActivity.getContext()
                                            .getApplicationContext()
                                            .getSystemService(Context.WIFI_SERVICE);
    DhcpInfo dhcp = Objects.requireNonNull(wifiManager).getDhcpInfo();

    int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
    byte[] quads = new byte[4];
    for (int k = 0; k < 4; k++)
      quads[k] = (byte) ((broadcast >> k * 8) & 0xFF);
    InetAddress addr = null;
    try {
      addr = InetAddress.getByAddress(quads);
    } catch (UnknownHostException e) {
      System.out.println("could not resolve broadcast address");
    }
    return addr;
  }

  private void handleOutgoingMessages() {
    if (endpoint == null || messageBytes == null) return;
    try {
      DatagramPacket packet = new DatagramPacket(messageBytes,
                                                 messageBytes.length, endpoint,
                                                 PORT);
      socket.send(packet);
      messageBytes = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleIncomingMessages() {
    byte[] buf = new byte[256];
    DatagramPacket packet = new DatagramPacket(buf, buf.length);
    try {
      socket.receive(packet);
      timespan = LONG_TIMESPAN;
      hosts.add(packet.getAddress());
      if (hosts.size() == 1)
        endpoint = packet.getAddress();
      RecyclerViewAdapter.addItems(hosts);
      MainActivity.updateRecyclerView();
    } catch (SocketTimeoutException e) {
      // nop. This should just silence timeouts
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  void stop() {
    running = false;
  }

  void setEndpoint(InetAddress address) {
    endpoint = address;
  }
}
