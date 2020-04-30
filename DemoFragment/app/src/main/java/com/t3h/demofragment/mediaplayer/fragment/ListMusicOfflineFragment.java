package com.t3h.demofragment.mediaplayer.fragment;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.demofragment.databinding.ActivityMediaPlayerBinding;
import com.t3h.demofragment.databinding.FragmentListMusicOfflineBinding;
import com.t3h.demofragment.mediaplayer.MediaPlayerActivity;
import com.t3h.demofragment.mediaplayer.MusicAdapter;
import com.t3h.demofragment.mediaplayer.MusicOffline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ListMusicOfflineFragment extends Fragment
        implements MusicAdapter.IMusic, MediaPlayer.OnCompletionListener, View.OnClickListener {
    private List<MusicOffline> musicOfflines;
    private FragmentListMusicOfflineBinding binding;
    private boolean isRunning;
    private MediaPlayer mp;
    private int currentPosition = -1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListMusicOfflineBinding.inflate(
                inflater, container, false
        );
        musicOfflines = new ArrayList<>();
        pareAllMusic();
        binding.rc.setLayoutManager(new LinearLayoutManager(
                getActivity()));
        binding.rc.setAdapter(new MusicAdapter(this));
        isRunning=true;
        initAsyn();
        binding.bottom.setOnClickListener(this);
        return binding.getRoot();
    }
    private void pareAllMusic(){
        Cursor c =
                getContext().getContentResolver().query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null,
                        null,
                        null,
                        "_display_name ASC"
                );
        List<Long> albumIds = new ArrayList<>();
        List<Long> artistIds = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        //_data, _display_name, duration
        int indexData = c.getColumnIndex("_data");
        int indexId = c.getColumnIndex("_id");
        int indexAlbum= c.getColumnIndex("album_id");
        int indexArtist= c.getColumnIndex("artist_id");
        int indexDisplayName = c.getColumnIndex("_display_name");
        int indexDuration = c.getColumnIndex("duration");
        int indexDateAdd = c.getColumnIndex("date_added");
        c.moveToFirst();
        while (!c.isAfterLast()){
            String data = c.getString(indexData);
            String name = c.getString(indexDisplayName);
            long duration = c.getLong(indexDuration);
            long date = c.getLong(indexDateAdd);
            long albumId =c.getLong(indexAlbum);
            if (albumId > 0){
                albumIds.add(albumId);
            }
            long artistId =c.getLong(indexArtist);
            if (artistId > 0 ){
                artistIds.add(artistId);
            }
            ids.add(c.getLong(indexId));
            musicOfflines.add(new MusicOffline(data, name,
                    duration, date, albumId));
            c.moveToNext();
        }
        c.close();
        if (ids.size() > 0){
            String srtId = "("+albumIds.get(0);
            for ( int i = 1; i < ids.size(); i++){
                srtId+=(","+albumIds.get(i));
            }
            srtId+=")";


            c =
                    getContext().getContentResolver().query(
                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            null,
                            "_id IN "+srtId,
                            null,
                            null
                    );
            int indexLink = c.getColumnIndex("album_art");
            int indexIdA = c.getColumnIndex("_id");
            c.moveToFirst();
//            album_art
            while (!c.isAfterLast()){
                String album = c.getString(indexLink);
                long id = c.getLong(indexIdA);
                for (MusicOffline musicOffline : musicOfflines) {
                    if (musicOffline.getAlbumId() == id){
                        musicOffline.setPathAlbum(album);
                    }
                }
                c.moveToNext();
            }

        }

    }

    @Override
    public int getCount() {
        if (musicOfflines == null){
            return 0;
        }
        return musicOfflines.size();
    }

    @Override
    public MusicOffline getData(int position) {
        return musicOfflines.get(position);
    }

    @Override
    public void onClickItem(int position) {
        if (mp != null){
            mp.release();
        }
        mp = new MediaPlayer();
        try {
            mp.setDataSource(musicOfflines.get(position).getPath());
            mp.setOnCompletionListener(this);
            mp.prepare();
            mp.start();
            currentPosition = position;
        } catch (IOException e) {
            e.printStackTrace();
            mp = null;
        }

    }

    private void initAsyn(){
        new AsyncTask<Void, Integer, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                while (isRunning){
                    if (mp != null){
                        publishProgress(mp.getCurrentPosition(), mp.getDuration());
                    }
                    SystemClock.sleep(500);
                }
                return null;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
//                binding.seek.setProgress(values[0] * 100/values[1]);
            }
        }.executeOnExecutor(
                Executors.newFixedThreadPool(1)
        );
    }

    @Override
    public void onDestroyView() {
        isRunning=false;
        super.onDestroyView();
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (currentPosition == musicOfflines.size()-1){
            return;
        }
        onClickItem(
                (currentPosition+1)
        );
    }

    @Override
    public void onClick(View view) {
        ((MediaPlayerActivity)(getActivity()))
                .openPlayFragment(musicOfflines.get(currentPosition),
                        mp);
    }
}
