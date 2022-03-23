package com.chronno.zombie.server.services;

import com.chronno.zombie.game.messages.ChatMessage;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ServerBuilder extends Listener {

    private final Server gameServer;

    @Autowired
    public ServerBuilder(@Value("${server.ports.tcp}") Integer tcpPort,
                         @Value("${server.ports.udp}") Integer udpPort) throws IOException {
        this.gameServer = new Server();
        gameServer.start();
        gameServer.bind(tcpPort, udpPort);
        gameServer.addListener(this);
        gameServer.getKryo().register(ChatMessage.class);
        Log.info("Server started");
    }

    private void broadCast(Object message) {
        this.gameServer.sendToAllTCP(message);
    }


    @Override
    public void connected(Connection connection) {
        Log.info("Connection created");
    }

    @Override
    public void disconnected(Connection connection) {
        Log.info("Connection destroyed");
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof ChatMessage) {
            broadCast(object);
        }
    }

    @Override
    public void idle(Connection connection) {

    }
}
