package com.t3h.demofragment.mediaplayeronline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.ConnectionService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.t3h.demofragment.R;
import com.t3h.demofragment.ServiceMusicOnline;
import com.t3h.demofragment.databinding.FragmentPlayOnlineBinding;

import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;

public class PlayOnlineFragment extends Fragment implements View.OnClickListener {
    private FragmentPlayOnlineBinding binding;
    private ServiceConnection conn;
    private ServiceMusicOnline service;
    private boolean isRunning;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayOnlineBinding.inflate(
                inflater,
                container,
                false
        );
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        init();
        initConnect();
        isRunning=true;
        startAsyn();
        return binding.getRoot();
    }

    private void initConnect() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName,
                                           IBinder iBinder) {
                ServiceMusicOnline.MyBind bind =
                        (ServiceMusicOnline.MyBind) iBinder;
                service = bind.getService();
                updateInfo();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        Intent intent = new Intent();
        intent.setClassName(getContext(),
                ServiceMusicOnline.class.getName());
        //ket noi den service
        getContext().bindService(intent, conn,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroyView() {
        getContext().unbindService(conn);
        isRunning=false;
        super.onDestroyView();
    }

    private void init() {
        updateInfo();
        binding.btnNext.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
    }

    private void updateInfo() {
        if (service == null) {
            return;
        }
        ItemMusicOnline itemt =
                service.getCurrentItem();
        if (itemt == null) {
            return;
        }
        binding.tvName.setText(itemt.getSongName());
        binding.tvSinger.setText(itemt.getArtistName());
        if (itemt.getLinkImage() != null) {
            Glide.with(binding.ivAvatar)
                    .load(itemt.getLinkImage())
                    .error(R.drawable.icon)
                    .placeholder(R.drawable.icon)
                    .into(binding.ivAvatar);
        }
    }

    @Override
    public void onClick(View view) {
        if (service == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.btn_next:
                service.next();
                updateInfo();
                break;
            case R.id.btn_previous:
                service.previous();
                updateInfo();
                break;
        }
    }

    private void startAsyn() {
        new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                while (isRunning) {
                    SystemClock.sleep(300);
                    if (service == null || !service.isPreared()) {
                        continue;
                    }

                    MediaPlayer mp = service.getMp();
                    publishProgress(mp.getDuration(),
                            mp.getCurrentPosition());
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                binding.tvTotal.setText(
                        new SimpleDateFormat("mm:ss").format(values[0])
                );
                binding.tvStart.setText(
                        new SimpleDateFormat("mm:ss").format(values[1])
                );
                binding.seek.setProgress(
                        (int)(values[1]*100/values[0])
                );
            }
        }.executeOnExecutor(Executors.newFixedThreadPool(1));
    }
}
