package com.nicholassavilerobinson.SharpControl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SharpTV {

    private static final int PORT = 10002;

    private String hostname;
    private Socket socket;
    private DataOutputStream socketUpStream;
    private BufferedReader socketDownStream;

    public SharpTV(String hostname) {
        this.hostname = hostname;
        socket = null;
        socketUpStream = null;
        socketDownStream = null;
    }

    public boolean connect() throws SharpTVException{
        if (!isConnected()) {
            try {
                socket = new Socket(hostname, PORT);
                socketUpStream = new DataOutputStream(socket.getOutputStream());
                socketDownStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                throw new SharpTVException("Unable to connect to: " + hostname);
            }
        }
        return true;
    }

    public boolean disconnect() throws SharpTVException {
        if (isConnected()) {
            try {
                socket.close();
            } catch (IOException e) {
                throw new SharpTVException("Unable to disconnect from: " + hostname);
            }
        }
        return true;
    }

    public boolean isConnected() {
        return (socket != null);
    }

    public boolean write(String data) throws SharpTVException {
        try {
            socketUpStream.writeBytes(data);
        } catch (IOException e) {
            throw new SharpTVException("Unable to read from: " + hostname);
        }
        return true;
    }

    public String read() throws SharpTVException{
        try {
            return socketDownStream.readLine();
        } catch (IOException e) {
            throw new SharpTVException("Unable to read from: " + hostname);
        }
    }
}
