package com.t3h.demofragment.chatsocket;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.ActivityChatSocketBinding;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ChatSocketActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChatSocketBinding binding;
    private static final String TAG = "ChatSocketActivity";
    private Socket socket;
    private Handler hd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(
                this, R.layout.activity_chat_socket
        );
        binding.btnSend.setOnClickListener(this);
        hd = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                binding.tvContent.setText(msg.obj+"");
            }
        };
    }

    @Override
    public void onClick(View view) {
        String to = binding.edtTo.getText().toString();
        String from = binding.edtFrom.getText().toString();
        String content = binding.edtContent.getText().toString();
        intSocket();
        if (socket != null){
//            emit is send content
            socket.emit("message",
                    from+"@@"+to+"@@"+content);
            binding.edtContent.setText("");
        }
    }

    private void intSocket(){
        if (socket != null){
            return;
        }
        try {
            socket = IO.socket("http://192.168.1.17:9092");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "EVENT_CONNECT: " + args);
                    socket.emit("connected",
                            binding.edtFrom.getText().toString());
                }
            });


            socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "EVENT_DISCONNECT: " + args);
                }
            });
            socket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d(TAG, "EVENT_CONNECT_ERROR: " + args);
                }
            });

            socket.on("receive", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    String s = (String)args[0];
                    Log.d(TAG, "s: " + s);
                    Message msg = new Message();
                    msg.obj=s;
                    msg.setTarget(hd);
                    msg.sendToTarget();
                }
            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            socket = null;
        }
    }
}
