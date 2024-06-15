package fr.tt54.networking.server;

import fr.tt54.networking.packet.PacketManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MultiClientServerConnection {

    private final int port;
    private ServerSocket serverSocket;
    private final Map<String, PacketManager> connectionsPacketManagers = new HashMap<>();
    private PacketManager packetManager;

    public MultiClientServerConnection(int port) {
        this.port = port;
    }

    public void enable(){
        this.packetManager = new PacketManager(this);

        try {
            this.serverSocket = new ServerSocket(this.port);

            while (true){
                ServerConnection serverConnection = new ServerConnection(serverSocket.accept());
                serverConnection.start();

                String id = serverConnection.getSocket().getInetAddress().getHostAddress() + ":" + serverConnection.getSocket().getPort();
                connectionsPacketManagers.put(id, serverConnection.getPacketManager());

                System.out.println("new connection from " + id);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public PacketManager getClientPacketManager(String connection){
        return connectionsPacketManagers.get(connection);
    }

    public Collection<PacketManager> getClientsPacketManager(){
        return this.connectionsPacketManagers.values();
    }
}
