package fr.tt54.networking.common.events;

public interface NetworkListener {

    void onConnection(NetworkConnectionEvent event);
    void onMessageReceived(NetworkMessageEvent event);
    void onDisconnection(NetworkDisconnectionEvent event);

}
