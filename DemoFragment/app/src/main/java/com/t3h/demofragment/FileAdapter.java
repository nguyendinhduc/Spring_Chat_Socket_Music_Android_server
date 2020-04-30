package com.t3h.demofragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.demofragment.databinding.ItemFileBinding;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private IFile inter;

    public FileAdapter(IFile inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ItemFileBinding binding =
                ItemFileBinding.inflate(
                        LayoutInflater.from(viewGroup.getContext()),
                        viewGroup, false
                );
        return new FileViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final FileViewHolder fileViewHolder, int position) {
        ItemFile data = inter.getData(position);
        fileViewHolder.binding.tvName.setText(data.getTitle());
        fileViewHolder.binding.tvPath.setText(data.getPath());
        if ("D".equals(data.getTitle())) {
            fileViewHolder.binding.ivFile.setImageResource(
                    R.drawable.ic_folder_48dp
            );
        } else {
            fileViewHolder.binding.ivFile.setImageResource(
                    R.drawable.ic_insert_drive_file_48dp
            );

        }
        
        fileViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inter.onClickItem(fileViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inter.getCount();
    }

    public interface IFile {
        int getCount();

        ItemFile getData(int position);

        void onClickItem(int adapterPosition);
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        private ItemFileBinding binding;

        public FileViewHolder(ItemFileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
