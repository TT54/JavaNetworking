package fr.tt54.networking.common.events;

import fr.tt54.networking.common.NetworkConnection;

import java.net.InetAddress;

public abstract class NetworkEvent {

    private final InetAddress clientAddress;
    private final InetAddress serverAddress;
    private final boolean serverSide;

    public NetworkEvent(NetworkConnection<?> networkConnection) {
        this.serverSide = networkConnection.isServerSide();

        this.clientAddress = this.serverSide ? networkConnection.getSocket().getInetAddress() : networkConnection.getSocket().getLocalAddress();
        this.serverAddress = !this.serverSide ? networkConnection.getSocket().getInetAddress() : networkConnection.getSocket().getLocalAddress();
    }

    public InetAddress getClientAddress() {
        return clientAddress;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public boolean isServerSide() {
        return serverSide;
    }
}
