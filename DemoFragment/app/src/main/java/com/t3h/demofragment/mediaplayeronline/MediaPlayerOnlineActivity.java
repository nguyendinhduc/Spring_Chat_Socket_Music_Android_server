package com.t3h.demofragment.mediaplayeronline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.t3h.demofragment.R;
import com.t3h.demofragment.ServiceMusicOnline;
import com.t3h.demofragment.main.MainFragment;

public class MediaPlayerOnlineActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        startService();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content, new MainFragment(),
                        MainFragment.class.getName())
                .commit();
        openPlayClick(getIntent());
    }

    public void openPlayer() {
        getSupportFragmentManager()
                .beginTransaction()
                .hide(getSupportFragmentManager().findFragmentByTag(MainFragment.class.getName()))
                .add(R.id.content, new PlayOnlineFragment(), PlayOnlineFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    public OnlineMusicFragment getOnlineFragmentMusic() {
        return (OnlineMusicFragment)
                getSupportFragmentManager()
                        .findFragmentByTag(OnlineMusicFragment.class.getName());
    }

    private void startService() {
        //unbound
        Intent intent = new Intent();
        intent.setClassName(this, ServiceMusicOnline.class.getName());
        startService(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        openPlayClick(intent);
    }

    public void openPlayClick(Intent intent) {
        String key = intent.getStringExtra("KEY");
        if ("NEW_PLAY".equals(key)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    openPlay();
                }
            }, 300);
        }
    }

    private void openPlay() {
        FragmentManager manager =
                getSupportFragmentManager();
        PlayOnlineFragment fr = (PlayOnlineFragment)
                manager.findFragmentByTag(
                PlayOnlineFragment.class.getName());

        if (fr == null){
            openPlayer();
            return;
        }
        if (fr.isVisible() == false){
            FragmentTransaction transaction = manager.beginTransaction();
            MainFragment.hideAllFragment(manager, transaction);
            transaction.show(fr)
                    .commit();
        }
    }
}
