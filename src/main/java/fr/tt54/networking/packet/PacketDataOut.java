package fr.tt54.networking.packet;

import java.util.ArrayList;
import java.util.List;

public class PacketDataOut {

    private List<String> datas = new ArrayList<>();

    public PacketDataOut(String packetName) {
        datas.add(PacketManager.PACKET_BEGIN_MARK);
        datas.add(packetName);
        datas.add("packetSize");
    }

    public void writeString(String str){
        this.datas.add(str);
    }

    public void writeInt(int i){
        this.datas.add(String.valueOf(i));
    }

    public void writeDouble(double d){
        this.datas.add(String.valueOf(d));
    }

    public void writeLong(long l){
        this.datas.add(String.valueOf(l));
    }

    public void writeFloat(float f){
        this.datas.add(String.valueOf(f));
    }

    public void writeShort(short s){
        this.datas.add(String.valueOf(s));
    }

    public void writeByte(byte b){
        this.datas.add(String.valueOf(b));
    }

    public void writeBoolean(boolean b){
        this.datas.add(String.valueOf(b ? 1 : 0));
    }

    public List<String> prepareForSending(){
        this.datas.set(2, String.valueOf(this.datas.size()));
        return this.datas;
    }
}
