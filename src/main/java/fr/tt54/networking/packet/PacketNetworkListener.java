package fr.tt54.networking.packet;

import fr.tt54.networking.client.ClientListener;
import fr.tt54.networking.common.events.NetworkConnectionEvent;
import fr.tt54.networking.common.events.NetworkDisconnectionEvent;
import fr.tt54.networking.common.events.NetworkMessageEvent;
import fr.tt54.networking.server.ServerListener;

import java.lang.reflect.InvocationTargetException;

public class PacketNetworkListener implements ServerListener, ClientListener {

    private final PacketManager packetManager;

    private boolean readingPacket = false;
    private String packetName;
    private int packetSize;
    private int readingIndex;
    private String[] packetContent;

    public PacketNetworkListener(PacketManager packetManager) {
        this.packetManager = packetManager;
    }

    @Override
    public void onConnection(NetworkConnectionEvent event) {
        System.out.println("Connected !");
    }

    @Override
    public void onMessageReceived(NetworkMessageEvent event) {
        String message = event.getMessage();
        if(readingPacket){
            readingIndex++;
            
            if(readingIndex == 1){
                packetName = message;
            } else if(readingIndex == 2){
                packetSize = Integer.parseInt(message);
                packetContent = new String[packetSize];
                packetContent[0] = PacketManager.PACKET_BEGIN_MARK;
                packetContent[1] = packetName;
                packetContent[2] = message;
            } else {
                packetContent[readingIndex] = message;
            }

            if(readingIndex == packetSize - 1){
                PacketDataIn in = new PacketDataIn(packetContent, packetName, packetSize);

                try {
                    this.packetManager.readPacket(in);
                } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }

                readingPacket = false;
                packetName = null;
                packetSize = 0;
                readingIndex = 0;
                packetContent = null;
            }
        } else if(message.equals(PacketManager.PACKET_BEGIN_MARK)){
            readingPacket = true;
            readingIndex = 0;
        }
    }

    @Override
    public void onDisconnection(NetworkDisconnectionEvent event) {

    }
}
