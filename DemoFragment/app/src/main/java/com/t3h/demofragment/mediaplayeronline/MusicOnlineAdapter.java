package com.t3h.demofragment.mediaplayeronline;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.t3h.demofragment.R;
import com.t3h.demofragment.mediaplayer.MusicOffline;

public class MusicOnlineAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private IMusicOnline inter;
    private boolean hasLoadMore;
    private boolean loadingMore;

    public MusicOnlineAdapter(IMusicOnline inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        if ( type == 0){
            View view = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_music_online, viewGroup, false);
            return new MusicOnlineViewHolder(view);
        }else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_loadmore, viewGroup, false);
            return new ProgressLoading(v);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if ( position == getItemCount()-1){
            return 1;
        }
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewH, int position) {
        if ( position == getItemCount()-1 && !loadingMore ){
            //load more
            loadingMore=true;
            inter.loadMore();
            return;
        }
        final MusicOnlineViewHolder holder = (MusicOnlineViewHolder)viewH;
        ItemMusicOnline data = inter.getData(position);
        holder.tvArtist.setText(data.getArtistName());
        holder.tvName.setText(data.getSongName());
        if ( data.getLinkImage() == null || data.getLinkImage().equals("") ){
            Glide.with(holder.ivImg)
                    .load(R.drawable.ic_music_video_red_700_48dp)
                    .into(holder.ivImg);
        }else {
            Glide.with(holder.ivImg)
                    .load(data.getLinkImage())
                    .error(R.drawable.ic_music_video_red_700_48dp)
                    .placeholder(R.drawable.ic_music_video_red_700_48dp)
                    .into(holder.ivImg);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.onClickItem(holder.getAdapterPosition());
            }
        });

    }

    public boolean isLoadingMore() {
        return loadingMore;
    }

    public void setLoadingMore(boolean loadingMore) {
        this.loadingMore = loadingMore;
    }

    @Override
    public int getItemCount() {
        return inter.getCount();
    }

    public interface IMusicOnline{
        int getCount();
        ItemMusicOnline getData(int position);
        void onClickItem(int position);
        void loadMore();
    }
    static class MusicOnlineViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImg;
        private TextView tvName, tvArtist;
        public MusicOnlineViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            tvName = itemView.findViewById(R.id.tv_name);
            tvArtist = itemView.findViewById(R.id.tv_artist);
        }
    }

    static class ProgressLoading extends RecyclerView.ViewHolder{

        public ProgressLoading(@NonNull View itemView) {
            super(itemView);
        }
    }
}
