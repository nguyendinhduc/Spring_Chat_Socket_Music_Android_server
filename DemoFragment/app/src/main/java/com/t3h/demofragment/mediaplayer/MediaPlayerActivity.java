package com.t3h.demofragment.mediaplayer;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.ActivityMediaPlayerBinding;
import com.t3h.demofragment.main.MainFragment;
import com.t3h.demofragment.mediaplayer.fragment.ListMusicOfflineFragment;
import com.t3h.demofragment.mediaplayer.fragment.PlayerFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MediaPlayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content,
                        new MainFragment(),
                        MainFragment.class.getName())
                .commit();
    }

    public void openPlayFragment(MusicOffline item, MediaPlayer mp){
        PlayerFragment playerFragment = new PlayerFragment();
        playerFragment.setMp(mp);
        playerFragment.setMusicOffline(item);
        getSupportFragmentManager()
                .beginTransaction()
                .hide(getSupportFragmentManager()
                        .findFragmentByTag(ListMusicOfflineFragment.class.getName()))
                .add(R.id.content, playerFragment, PlayerFragment.class.getName())
                .addToBackStack(null)
                .commit();

    }
}
