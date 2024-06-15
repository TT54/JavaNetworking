package fr.tt54.networking.packet;

public abstract class Packet {

    public abstract String getName();
    public abstract void writeData(PacketDataOut out);
    public abstract void readData(PacketDataIn in);

}
