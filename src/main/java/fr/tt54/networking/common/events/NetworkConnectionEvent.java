package fr.tt54.networking.common.events;

import fr.tt54.networking.common.NetworkConnection;

public class NetworkConnectionEvent extends NetworkEvent{

    public NetworkConnectionEvent(NetworkConnection<?> networkConnection) {
        super(networkConnection);
    }
}
