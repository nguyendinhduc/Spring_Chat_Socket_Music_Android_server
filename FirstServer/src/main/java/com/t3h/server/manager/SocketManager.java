package com.t3h.server.manager;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class SocketManager {
    private SocketIOServer socketIOServer;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<String, SocketIOClient> ioClientMap = new HashMap<>();

    @PostConstruct
    private void inits() {
        Configuration config = new Configuration();
        config.setHostname("192.168.1.17");
        config.setPort(9092);
        socketIOServer = new SocketIOServer(config);
        socketIOServer.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                ///
                System.out.println("socketIOClient............");

            }
        });

        socketIOServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                System.out.println("addDisconnectListener............");
                for (String s : ioClientMap.keySet()) {
                    SocketIOClient so = ioClientMap.get(s);
                    if ( so == socketIOClient){
                        ioClientMap.remove(s);
                        break;
                    }
                }

            }
        });

        socketIOServer.addEventListener("connected", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s,
                               AckRequest ackRequest) throws Exception {
                ioClientMap.put(s, socketIOClient);
            }
        });

        socketIOServer.addEventListener("message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient socketIOClient, String s, AckRequest ackRequest) throws Exception {
                System.out.println("s: " + s);
                String[] ss = s.split("@@");
                //0: from
                //1: to
                //2: content
                //gui den thang to
//                socketIOClient.sendEvent("message", ss[2]);


                SocketIOClient b = ioClientMap.get(ss[1]);
                if (b == null){
                    return;
                }
                b.sendEvent("receive", ss[2]);
            }
        });
        socketIOServer.start();
    }

}
