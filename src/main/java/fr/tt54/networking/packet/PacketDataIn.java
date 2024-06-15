package fr.tt54.networking.packet;

public class PacketDataIn {

    private final String[] datas;
    private final String packetName;
    private final int size;
    private int pos = -1;

    public PacketDataIn(String[] datas) {
        this.datas = datas;
        this.packetName = datas[1];
        this.size = Integer.parseInt(datas[2]);
        pos = 2;
    }

    public PacketDataIn(String[] datas, String packetName, int size) {
        this.datas = datas;
        this.packetName = packetName;
        this.size = size;
        pos = 2;
    }

    public String getPacketName() {
        return packetName;
    }

    public int getSize() {
        return size;
    }

    private String read(){
        pos++;
        return datas[pos];
    }

    public String readString(){
        return this.read();
    }

    public int readInt(){
        return Integer.parseInt(this.read());
    }

    public long readLong(){
        return Long.parseLong(this.read());
    }

    public short readShort(){
        return Short.parseShort(this.read());
    }

    public byte readByte(){
        return Byte.parseByte(this.read());
    }

    public boolean readBoolean(){
        int i = this.readInt();
        return i == 1;
    }

    public float readFloat(){
        return Float.parseFloat(this.read());
    }

    public double readDouble(){
        return Double.parseDouble(this.read());
    }
}
