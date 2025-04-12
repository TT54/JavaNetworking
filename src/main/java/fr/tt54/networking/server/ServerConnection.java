package fr.tt54.networking.server;

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
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class ServerConnection extends NetworkConnection<ServerListener> {

    private final Socket clientSocket;
    private final Set<ServerListener> listeners = new HashSet<>();

    private PacketManager packetManager;
    private PrintWriter out;
    private BufferedReader in;

    public ServerConnection(Socket clientSocket) {
        this.clientSocket = clientSocket;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            this.actionListeners(listener -> listener.onConnection(
                    new NetworkConnectionEvent(this)
            ));

            this.packetManager = new PacketManager(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PacketManager getPacketManager() {
        return packetManager;
    }

    @Override
    public boolean isServerSide() {
        return true;
    }

    @Override
    public Socket getSocket() {
        return this.clientSocket;
    }

    @Override
    protected Set<ServerListener> getListeners() {
        return this.listeners;
    }

    @Override
    public void run() {
        if(out != null && in != null){
            try {
                String input;
                while (this.clientSocket.isConnected() && !this.clientSocket.isClosed() && (input = in.readLine()) != null){
                    final String message = input;
                    this.actionListeners(listener -> listener.onMessageReceived(
                            new NetworkMessageEvent(this, message)
                    ));
                }
            } catch (IOException e) {
                if(e instanceof SocketException){
                    System.out.println("Connection lost with " + this.clientSocket.getInetAddress().toString() + ":" + this.clientSocket.getPort());
                } else {
                    e.printStackTrace();
                }
            }

            this.actionListeners(listener -> listener.onDisconnection(
                    new NetworkDisconnectionEvent(this)
            ));
        }
    }

    @Override
    public void sendData(String data) {
        out.println(data);
    }

    @Override
    public void registerListener(ServerListener listener){
        this.listeners.add(listener);
    }
}
