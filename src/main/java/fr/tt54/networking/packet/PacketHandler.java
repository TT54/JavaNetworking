package fr.tt54.networking.packet;

import java.net.InetAddress;

public abstract class PacketHandler<T extends Packet, R extends Packet> {

    public abstract R readPacket(InetAddress sender, PacketManager packetManager, T packet);

}
