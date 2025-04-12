package fr.tt54.networking.exemple;

import fr.tt54.networking.packet.PacketManager;
import fr.tt54.networking.server.MultiClientServerConnection;

public class ServerMain {

    private static final int PORT = 25777;
    public static MultiClientServerConnection connection;

    public static void main(String[] args) {
        PacketManager.registerPacket("test_packet", TestPacket.class, TestPacket.TestPacketHandler.class);
        PacketManager.registerPacket("ping_packet", PingPacket.class, PingPacket.PingPacketHandler.class);

        connection = new MultiClientServerConnection(PORT);
        connection.enable();
    }

}
