package com.nicholassavilerobinson.SharpControl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SharpTV {

    private static final int PORT = 10002;

    private final String hostname;
    private Socket socket;
    private DataOutputStream socketUpStream;
    private BufferedReader socketDownStream;

    public SharpTV(String hostname) {
        this.hostname = hostname;
        this.socket = null;
        this.socketUpStream = null;
        this.socketDownStream = null;
    }

    public boolean connect() throws SharpTVException{
        if (!this.isConnected()) {
            try {
                this.socket = new Socket(this.hostname, PORT);
                this.socketUpStream = new DataOutputStream(this.socket.getOutputStream());
                this.socketDownStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            } catch (IOException e) {
                throw new SharpTVException("Unable to connect to: " + this.hostname);
            }
        }
        return true;
    }

    public boolean disconnect() throws SharpTVException {
        if (this.isConnected()) {
            try {
                this.socket.close();
            } catch (IOException e) {
                throw new SharpTVException("Unable to disconnect from: " + this.hostname);
            }
        }
        return true;
    }

    public boolean isConnected() {
        return (this.socket != null);
    }

    public boolean write(String data) throws SharpTVException {
        try {
            this.socketUpStream.writeBytes(data);
        } catch (IOException e) {
            throw new SharpTVException("Unable to read from: " + this.hostname);
        }
        return true;
    }

    public String read() throws SharpTVException{
        try {
            return this.socketDownStream.readLine();
        } catch (IOException e) {
            throw new SharpTVException("Unable to read from: " + this.hostname);
        }
    }
}
