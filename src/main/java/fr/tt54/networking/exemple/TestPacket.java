package fr.tt54.networking.exemple;

import fr.tt54.networking.packet.*;

import java.net.InetAddress;

public class TestPacket extends Packet {

    private String msg;
    private boolean sendBack;

    public TestPacket(String msg, boolean sendBack) {
        this.msg = msg;
        this.sendBack = sendBack;
    }

    public TestPacket() {
    }

    @Override
    public String getName() {
        return "test_packet";
    }

    @Override
    public void writeData(PacketDataOut out) {
        out.writeString(this.msg);
        out.writeBoolean(this.sendBack);
    }

    @Override
    public void readData(PacketDataIn in) {
        this.msg = in.readString();
        this.sendBack = in.readBoolean();
    }


    public static class TestPacketHandler extends PacketHandler<TestPacket, TestPacket>{

        @Override
        public TestPacket readPacket(InetAddress sender, PacketManager packetManager, TestPacket packet) {
            System.out.println(packet.msg);

            if(packet.msg.equalsIgnoreCase("Hello")){
                ServerMain.connection.getPacketManager().sendPacketToEveryone(new TestPacket("Tu es nul !", false));
            }

            return packet.sendBack ? new TestPacket("La réponse !", false) : null;
        }
    }
}
