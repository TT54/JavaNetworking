package fr.tt54.networking.exemple;

import fr.tt54.networking.client.ClientConnection;
import fr.tt54.networking.packet.PacketManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientMain {

    private static final String HOST = "localhost";
    private static final int PORT = 25777;

    public static void main(String[] args) {
        PacketManager.registerPacket("test_packet", TestPacket.class, TestPacket.TestPacketHandler.class);

        ClientConnection clientSide = new ClientConnection(HOST, PORT);
        while (!clientSide.connect()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        try {
            PacketManager packetManager = clientSide.getPacketManager();
            while((line = reader.readLine()) != null){
                packetManager.sendPacketToServer(new TestPacket(line, true));
            }
        } catch (IOException ignore){}
    }
}
