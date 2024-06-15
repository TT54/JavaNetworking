package fr.tt54.networking.common.events;

import fr.tt54.networking.common.NetworkConnection;

public class NetworkMessageEvent extends NetworkEvent{

    private final String message;

    public NetworkMessageEvent(NetworkConnection<?> connection, String message) {
        super(connection);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
