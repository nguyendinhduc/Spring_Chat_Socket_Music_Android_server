package com.t3h.demofragment.viewpager;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.ItemMonthBinding;

import java.io.File;

public class MonthItemAdapter extends RecyclerView.Adapter<MonthItemAdapter.MonthViewHolder> {
    //B1 tao class ViewHolder
    static class MonthViewHolder extends RecyclerView.ViewHolder {
        private ItemMonthBinding binding;

        public MonthViewHolder(ItemMonthBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    //B2 tao interface
    public interface IMonth {
        int getCount();

        ItemData getData(int position);
    }

    private IMonth inter;

    public MonthItemAdapter(IMonth inter) {
        this.inter = inter;
    }

    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater =
                LayoutInflater.
                        from(viewGroup.getContext());
        ItemMonthBinding binding =
                ItemMonthBinding.inflate(
                        inflater,
                        viewGroup,
                        false
                );
        return new MonthViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder holder, int i) {
        ItemData itemData = inter.getData(i);
        holder.binding.tvTitle.setText(
                itemData.getTitle()
        );
        holder.binding.tvContent.setText(
                itemData.getContent()
        );
        if (itemData.getImage() != null ){
            Glide.with(holder.binding.ivImg)
                    .load(itemData.getImage())
//                    .load(new File())
                    .placeholder(R.color.colorAccent)
                    .error(R.color.colorAccent)
                    .into(holder.binding.ivImg);
        }
    }

    @Override
    public int getItemCount() {
        return inter.getCount();
    }
}
