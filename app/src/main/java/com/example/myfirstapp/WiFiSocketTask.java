package com.example.myfirstapp;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.*;

public class WiFiSocketTask extends AsyncTask<Void, String, Void> {
    private static final String host = "192.168.178.28";
    private static final int port = 23;
    private static InetAddress address ;
    private DatagramSocket socket = null;
    private boolean disconnectSignal = false;

    @Override
    protected Void doInBackground(Void... arg) {
        try {
            address = InetAddress.getByName(host);
            socket = new DatagramSocket();
            while (!disconnectSignal) {
                handleOutgoingMessages();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        socket.close();
        return null;
    }

    private byte[] messageBytes;

    void queueMessage(String data) {
        messageBytes = data.getBytes();
    }

    private void handleOutgoingMessages() {
        if (messageBytes == null) return;

        try {
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length, address, port);
            socket.send(packet);
            messageBytes = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void requestDisconnect() {
        disconnectSignal = true;
    }
}