package fr.tt54.networking.client;

import fr.tt54.networking.common.NetworkConnection;
import fr.tt54.networking.common.events.NetworkConnectionEvent;
import fr.tt54.networking.common.events.NetworkDisconnectionEvent;
import fr.tt54.networking.common.events.NetworkMessageEvent;
import fr.tt54.networking.packet.PacketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientConnection extends NetworkConnection<ClientListener> {

    private final String host;
    private final int port;
    private final Set<ClientListener> listeners = new HashSet<>();
    private PacketManager packetManager;

    private Socket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientConnection(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected Set<ClientListener> getListeners() {
        return this.listeners;
    }

    @Override
    public Socket getSocket() {
        return this.serverSocket;
    }

    @Override
    public boolean isServerSide() {
        return false;
    }

    @Override
    public PacketManager getPacketManager() {
        return packetManager;
    }

    public boolean connect(){
        try {
            serverSocket = new Socket(host, port);
            out = new PrintWriter(serverSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            this.actionListeners(listener -> listener.onConnection(
                    new NetworkConnectionEvent(this)
            ));
            this.startListening();

            System.out.println("Connected to " + host + ":" + port);
            this.packetManager = new PacketManager(this);


            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public void startListening(){
        new Thread(() -> {
            try {
                String fromServer;
                while ((fromServer = in.readLine()) != null) {
                    final String message = fromServer;
                    this.actionListeners(listener -> listener.onMessageReceived(
                            new NetworkMessageEvent(this, message)
                    ));
                }
            } catch (IOException e){
                System.err.println(e.getMessage());
            }

            this.actionListeners(listener -> listener.onDisconnection(
                    new NetworkDisconnectionEvent(this)
            ));
        }).start();
    }

    public void registerListener(ClientListener listener){
        this.listeners.add(listener);
    }

    @Override
    public void sendData(String data) {
        out.println(data);
    }
}
