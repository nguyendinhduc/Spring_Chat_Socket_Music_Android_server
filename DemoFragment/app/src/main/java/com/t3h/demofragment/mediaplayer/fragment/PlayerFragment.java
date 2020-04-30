package com.t3h.demofragment.mediaplayer.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.t3h.demofragment.databinding.FragmentPlayBinding;
import com.t3h.demofragment.mediaplayer.MusicOffline;

import java.io.File;
import java.text.SimpleDateFormat;

public class PlayerFragment extends Fragment {
    private MusicOffline musicOffline;
    private MediaPlayer mp;

    public void setMusicOffline(MusicOffline musicOffline) {
        this.musicOffline = musicOffline;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    private FragmentPlayBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayBinding.inflate(
                inflater, container, false
        );
        binding.tvName.setText(musicOffline.getName());
        binding.tvTotal.setText(
                new SimpleDateFormat("mm:ss")
                .format(musicOffline.getDuration())
        );
        if (musicOffline.getPathAlbum() != null &&
                !musicOffline.getPathAlbum().equals("")){
            Glide.with(getContext())
                    .load(new File(musicOffline.getPathAlbum()))
                    .into(binding.ivAvatar);
        }
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return binding.getRoot();
    }
}
