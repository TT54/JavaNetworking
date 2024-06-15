package fr.tt54.networking.common.events;

import fr.tt54.networking.common.NetworkConnection;

public class NetworkDisconnectionEvent extends NetworkEvent{

    public NetworkDisconnectionEvent(NetworkConnection<?> networkConnection) {
        super(networkConnection);
    }
}
