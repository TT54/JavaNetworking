package fr.tt54.networking.packet;

import fr.tt54.networking.client.ClientConnection;
import fr.tt54.networking.common.NetworkConnection;
import fr.tt54.networking.server.MultiClientServerConnection;
import fr.tt54.networking.server.ServerConnection;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketManager {

    public static final String PACKET_BEGIN_MARK = "8r1etv871packet_sending8r1etv871";

    private NetworkConnection<?> connection;
    private MultiClientServerConnection multiClientServerConnection;

    private final boolean serverSide;
    private static final Map<String, Class<? extends Packet>> registeredPackets = new HashMap<>();
    private static final Map<String, Class<? extends PacketHandler<?, ?>>> registeredPacketHandlers = new HashMap<>();

    public PacketManager(ClientConnection connection) {
        this.connection = connection;
        this.serverSide = this.connection.isServerSide();
        connection.registerListener(new PacketNetworkListener(this));
    }

    public PacketManager(ServerConnection connection) {
        this.connection = connection;
        this.serverSide = this.connection.isServerSide();
        connection.registerListener(new PacketNetworkListener(this));
    }

    public PacketManager(MultiClientServerConnection connection) {
        this.serverSide = true;
        this.multiClientServerConnection = connection;
    }

    private void sendPacket(Packet packet){
        if(connection != null) {
            PacketDataOut dataOut = new PacketDataOut(packet.getName());
            packet.writeData(dataOut);

            List<String> datas = dataOut.prepareForSending();
            for (String data : datas) {
                this.connection.sendData(data);
            }
        }
    }

    public void sendPacketToServer(Packet packet){
        if(!this.serverSide){
            this.sendPacket(packet);
        }
    }

    /**
     *
     * @param packet        Packet à envoyer
     * @param clientAddress String sous la forme XXX.XXX.XXX.XXX:PORT
     */
    public void sendPacketToClient(Packet packet, String clientAddress){
        if(this.multiClientServerConnection != null){
            this.multiClientServerConnection.getClientPacketManager(clientAddress).sendPacket(packet);
        }
    }

    public void sendPacketToEveryone(Packet packet){
        if(this.multiClientServerConnection != null){
            for(PacketManager packetManager : this.multiClientServerConnection.getClientsPacketManager()){
                packetManager.sendPacket(packet);
            }
        } else {
            System.err.println("This packet manager isn't bound to a MultiClientServerConnection");
        }
    }

    public void readPacket(PacketDataIn in) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<? extends Packet> packetClass = getPacketClass(in.getPacketName());
        Class<? extends PacketHandler<?, ?>> packetHandlerClass = getPacketHandlerClass(in.getPacketName());
        if(packetClass != null && packetHandlerClass != null){
            Packet packet = packetClass.getConstructor().newInstance();
            PacketHandler packetHandler = packetHandlerClass.getConstructor().newInstance();
            packet.readData(in);

            Packet response = packetHandler.readPacket(this.connection.getSocket().getInetAddress(), this, packet);
            if(response != null){
                this.sendPacket(response);
            }
        }
    }

    public static void registerPacket(String packetName, Class<? extends Packet> packetClass, Class<? extends PacketHandler<?, ?>> packetHandlerClass){
        if(registeredPackets.containsKey(packetName)){
            System.err.println("A packet with the name '" +packetName + "' already exists");
            return;
        }
        registeredPackets.put(packetName, packetClass);
        registeredPacketHandlers.put(packetName, packetHandlerClass);
    }

    public static Class<? extends Packet> getPacketClass(String packetName){
        return registeredPackets.get(packetName);
    }

    public static Class<? extends PacketHandler<?, ?>> getPacketHandlerClass(String packetName){
        return registeredPacketHandlers.get(packetName);
    }
}
