package com.t3h.demofragment.mediaplayer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.ItemMonthBinding;
import com.t3h.demofragment.databinding.ItemMusicBinding;

import java.io.File;
import java.text.SimpleDateFormat;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> {
    static class MusicHolder extends RecyclerView.ViewHolder {
        private ItemMusicBinding binding;

        public MusicHolder(ItemMusicBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface IMusic {
        int getCount();

        MusicOffline getData(int position);
        void onClickItem(int position);
    }

    private IMusic inter;

    public MusicAdapter(IMusic inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MusicHolder(
                ItemMusicBinding.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        viewGroup,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull final MusicHolder musicHolder, int i) {
        MusicOffline data = inter.getData(i);
        musicHolder.binding.tvName.setText(data.getName());
        musicHolder.binding.tvDuration.setText(
                new SimpleDateFormat("mm:ss")
                        .format(data.getDuration() )
        );
        if (data.getPathAlbum() == null ||data.getPathAlbum().equals("") ){
            Glide.with(musicHolder.itemView.getContext())
                    .load(R.drawable.ic_music_video_red_700_48dp)
                    .into(musicHolder.binding.ivImg);
        }else {
            Glide.with(musicHolder.itemView.getContext())
                    .load(new File(data.getPathAlbum()))
                    .placeholder(R.drawable.ic_music_video_red_700_48dp)
                    .error(R.drawable.ic_music_video_red_700_48dp)
                    .into(musicHolder.binding.ivImg);
        }

        musicHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.onClickItem(musicHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inter.getCount();
    }
}
