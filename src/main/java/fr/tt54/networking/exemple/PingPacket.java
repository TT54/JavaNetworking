package fr.tt54.networking.exemple;

import fr.tt54.networking.packet.*;

import java.net.InetAddress;

public class PingPacket extends Packet {

    private int count;
    private long beginTime;

    public PingPacket(int count, long beginTime) {
        this.count = count;
        this.beginTime = beginTime;
    }

    public PingPacket() {
    }

    @Override
    public String getName() {
        return "ping_packet";
    }

    @Override
    public void writeData(PacketDataOut out) {
        out.writeInt(count);
        out.writeLong(beginTime);
    }

    @Override
    public void readData(PacketDataIn in) {
        this.count = in.readInt();
        this.beginTime = in.readLong();
    }

    public static class PingPacketHandler extends PacketHandler<PingPacket, PingPacket>{

        @Override
        public PingPacket readPacket(InetAddress sender, PacketManager packetManager, PingPacket packet) {
            if(packet.count == 10001){
                double ping = (System.currentTimeMillis() - packet.beginTime) / ((packet.count + 1) / 2d);
                System.out.println("Ping : " + ping + "ms");
                return null;
            }
            return new PingPacket(packet.count + 1, packet.beginTime);
        }
    }
}
