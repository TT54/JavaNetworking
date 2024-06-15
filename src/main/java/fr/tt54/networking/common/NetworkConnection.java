package fr.tt54.networking.common;

import fr.tt54.networking.common.events.NetworkListener;
import fr.tt54.networking.packet.PacketManager;
import fr.tt54.networking.utils.ActionStuff;

import java.net.Socket;
import java.util.Set;

public abstract class NetworkConnection<T extends NetworkListener> extends Thread{

    public abstract void registerListener(T listener);
    public abstract void sendData(String data);
    public abstract boolean isServerSide();
    public abstract Socket getSocket();
    public abstract PacketManager getPacketManager();
    protected abstract Set<T> getListeners();

    protected void actionListeners(ActionStuff<T> actionStuff){
        for(T listener : getListeners()){
            actionStuff.doAction(listener);
        }
    }
}
