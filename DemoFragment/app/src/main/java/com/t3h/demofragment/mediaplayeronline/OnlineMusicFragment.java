package com.t3h.demofragment.mediaplayeronline;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.t3h.demofragment.ServiceMusicOnline;
import com.t3h.demofragment.databinding.FragmentOnlineMusicBinding;
import com.t3h.demofragment.retrofit.RetrofitUtils;
import com.t3h.demofragment.retrofit.Services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnlineMusicFragment extends Fragment implements
        MusicOnlineAdapter.IMusicOnline, TextWatcher, SwipeRefreshLayout.OnRefreshListener {
    private Call<List<ItemMusicOnline>> ex;
    private FragmentOnlineMusicBinding binding;
    private ServiceConnection conn;
    private ServiceMusicOnline service;
    private Services services;
    private MusicOnlineAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnlineMusicBinding.inflate(
                inflater,
                container, false
        );
        services = RetrofitUtils.getServices();
        binding.rc.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MusicOnlineAdapter(this);
        binding.rc.setAdapter(adapter);
        eventInput();
        connectService();
        binding.refesh.setOnRefreshListener(this);
        return binding.getRoot();
    }

    private void connectService() {
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName,
                                           IBinder iBinder) {
                ServiceMusicOnline.MyBind bind =
                        (ServiceMusicOnline.MyBind) iBinder;
                service = bind.getService();
                if (service.checkEmpty()) {
                    searSong("Thi+thoi", service.getCurrentPage()+1);
                } else {
                    binding.rc.getAdapter().notifyDataSetChanged();
                }

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
        super.onDestroyView();
    }

    private void eventInput() {
        binding.edtSearch.addTextChangedListener(this);
    }

    public void searSong(String search, final int currentPage) {
        if (ex != null) {
            ex.cancel();
        }
        ex = services.searSong(search, currentPage);
        ex.enqueue(new Callback<List<ItemMusicOnline>>() {
            @Override
            public void onResponse(Call<List<ItemMusicOnline>> call,
                                   Response<List<ItemMusicOnline>> response) {
                adapter.setLoadingMore(false);
                binding.refesh.setRefreshing(false);
                if (response.body() == null){
                    return;
                }
                if ( currentPage == 1){
                    service.setMusicOnline(response.body());
                }else {
                    service.getOnlines().addAll(response.body());
                }
                binding.rc.getAdapter().notifyDataSetChanged();
                service.setCurrentPage(currentPage);

            }

            @Override
            public void onFailure(Call<List<ItemMusicOnline>> call, Throwable t) {
                Toast.makeText(OnlineMusicFragment.this.getActivity(),
                        t.getMessage(), Toast.LENGTH_SHORT).show();
                binding.refesh.setRefreshing(false);
            }
        });

    }

    @Override
    public void loadMore() {
        if ( service == null){
            return;
        }
        searSong(binding.edtSearch.getText().toString()
                , service.getCurrentPage()+1);
    }

    @Override
    public int getCount() {
        if (service == null) {
            return 1;
        }
        return service.sizeItemMusicOnline()+1;
    }

    @Override
    public ItemMusicOnline getData(int position) {
        return service.getData(position);
    }

    @Override
    public void onClickItem(int position) {
        if (service.getCurrentPosition() == position) {
            ((MediaPlayerOnlineActivity) getActivity())
                    .openPlayer();
            return;
        }
        service.onClickItem(position);
        ((MediaPlayerOnlineActivity) getActivity())
                .openPlayer();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        searSong(editable.toString().replace("  ", " ")
                .replace(" ", "+"), 1);
    }

    @Override
    public void onRefresh() {
        if ( adapter.isLoadingMore()){
            binding.refesh.setRefreshing(false);
            return;
        }
        searSong(binding.edtSearch.getText().toString(), 1);
    }
}
