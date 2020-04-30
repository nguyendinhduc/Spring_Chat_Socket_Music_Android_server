package com.t3h.demofragment;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.t3h.demofragment.mediaplayeronline.GetLinkMusic;
import com.t3h.demofragment.mediaplayeronline.ItemMusicOnline;
import com.t3h.demofragment.mediaplayeronline.MediaPlayerOnlineActivity;
import com.t3h.demofragment.retrofit.RetrofitUtils;
import com.t3h.demofragment.retrofit.Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceMusicOnline extends Service
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    private List<ItemMusicOnline> onlines = new ArrayList<>();
    private boolean isPreared;

    private MediaPlayer mpOnline;
    private int currentPosition = -1;
    private Services services;
    private Call<GetLinkMusic> ex;
    private int currentPage = 0;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        services = RetrofitUtils.getServices();
        Log.d("ServiceMusicOnline", "onCreate....");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //kieu 1: START_NOT_STICKY
        //kieu 2: START_REDELIVER_INTENT
        //kieu 3: START_STICKY
        String action = intent.getAction();
        Log.d("ServiceMusicOnline", "onStartCommand: " + action);
        captureAction(action);
        return START_NOT_STICKY;
    }

    private void captureAction(String action) {
        if (action == null) {
            return;
        }
        switch (action) {
            case "PREVIOUS":
                previous();
                break;
            case "PLAY":
                break;
            case "NEXT":
                next();
                break;
            default:
                break;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind(this);
    }

    public void setMusicOnline(List<ItemMusicOnline> itemMusicOnlines) {
        onlines = itemMusicOnlines;
    }

    public List<ItemMusicOnline> getOnlines() {
        if (onlines == null) {
            onlines = new ArrayList<>();
        }
        return onlines;
    }

    public int sizeItemMusicOnline() {
        if (onlines == null) {
            return 0;
        }
        return onlines.size();
    }

    public ItemMusicOnline getData(int position) {
        return onlines.get(position);
    }

    private void play(int position) {
        if (mpOnline != null) {
            mpOnline.release();
        }
        mpOnline = new MediaPlayer();
        try {
            mpOnline.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mpOnline.setDataSource(this,
                    Uri.parse(onlines.get(position)
                            .getLinkMusic()));
            mpOnline.setOnBufferingUpdateListener(this);
            mpOnline.setOnCompletionListener(this);
            mpOnline.setOnPreparedListener(this);
            mpOnline.prepareAsync();
            currentPosition = position;
            createNotification();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkEmpty() {
        return onlines == null || onlines.size() == 0;
    }

    public MediaPlayer getMp() {
        return mpOnline;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    public static class MyBind extends Binder {
        private ServiceMusicOnline service;

        public MyBind(ServiceMusicOnline service) {
            this.service = service;
        }

        public ServiceMusicOnline getService() {
            return service;
        }


    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mpOnline.start();
        isPreared = true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {

    }

    public ItemMusicOnline getCurrentItem() {
        return onlines.get(currentPosition);
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void next() {
        isPreared = false;
        if (currentPosition == onlines.size() - 1) {
            return;
        }
        currentPosition = currentPosition + 1;
        if (onlines.get(currentPosition).getLinkMusic() == null) {
            getLinkMp3(
                    onlines.get(currentPosition).getLinkSong(),
                    currentPosition
            );
        } else {
            play(currentPosition);
        }

    }

    public void previous() {
        isPreared = false;
        if (currentPosition <= 0) {
            return;
        }
        play(currentPosition - 1);
    }

    public void onClickItem(int position) {
        isPreared = false;
        if (onlines.get(position).getLinkMusic() != null) {
            play(position);
            currentPosition = position;
            return;
        }
        getLinkMp3(onlines.get(position).getLinkSong(), position);
        currentPosition = position;

    }

    public boolean isPreared() {
        return isPreared;
    }

    public void getLinkMp3(String linkHtml, final int position) {
        if (ex != null) {
            ex.cancel();
        }
        ex = services.getLinkMusic(linkHtml);
        ex.enqueue(new Callback<GetLinkMusic>() {
            @Override
            public void onResponse(Call<GetLinkMusic> call, Response<GetLinkMusic> response) {
                onlines.get(position).setLinkMusic(response.body().getLink());
                if (response.body() != null) {
                    play(position);
                }
            }

            @Override
            public void onFailure(Call<GetLinkMusic> call, Throwable t) {

            }
        });

    }


    private void createNotification() {
        ItemMusicOnline data = getCurrentItem();

        Intent intentOpen = new Intent();
        intentOpen.setClassName(this,
                MediaPlayerOnlineActivity.class.getName()
        );
        intentOpen.putExtra("KEY", "NEW_PLAY");

        //tao Pending
        PendingIntent pending = PendingIntent.getActivity(this, 100,
                intentOpen, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews small = new RemoteViews(getPackageName(),
                R.layout.layout_notification_small);
        small.setTextViewText(R.id.tv_name, data.getSongName());
        small.setTextViewText(R.id.tv_artist, data.getArtistName());
        RemoteViews big = new RemoteViews(getPackageName(),
                R.layout.layout_notification);
        big.setTextViewText(R.id.tv_name, data.getSongName());
        big.setTextViewText(R.id.tv_artist, data.getArtistName());

        Intent iPr = new Intent();
        iPr.setClass(this, ServiceMusicOnline.class);
        iPr.setAction("PREVIOUS");
        PendingIntent pPrevios = PendingIntent.getService(this,
                0, iPr, 0);

        Intent iPlay = new Intent();
        iPlay.setClass(this, ServiceMusicOnline.class);
        iPlay.setAction("PLAY");
        PendingIntent pPlay = PendingIntent.getService(this,
                0, iPlay, 0);

        Intent iNext = new Intent();
        iNext.setClass(this, ServiceMusicOnline.class);
        iNext.setAction("NEXT");
        PendingIntent pNext = PendingIntent.getService(this,
                0, iNext, 0);

        big.setOnClickPendingIntent(R.id.btn_previous, pPrevios);
        big.setOnClickPendingIntent(R.id.btn_play, pPlay);
        big.setOnClickPendingIntent(R.id.btn_next, pNext);

//        small.setOnClickPendingIntent(R.id.btn_previous, pPrevios);
//        small.setOnClickPendingIntent(R.id.btn_play, pPlay);
//        small.setOnClickPendingIntent(R.id.btn_next, pNext);

        Notification no = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_music_note_black_24dp)
//                .setLargeIcon(
//                        BitmapFactory.decodeResource(getResources(),
//                                R.drawable.icon)
//                )
//                .setContentTitle(data.getSongName())
//                .setContentText(data.getArtistName())
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(small)
                .setCustomBigContentView(big)
                .setContentIntent(pending)
                .build();
        startForeground(10, no);
    }
}
