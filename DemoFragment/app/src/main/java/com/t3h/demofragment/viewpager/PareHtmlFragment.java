package com.t3h.demofragment.viewpager;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.demofragment.databinding.FragmentPareHtmlBinding;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PareHtmlFragment extends Fragment implements MonthItemAdapter.IMonth, SwipeRefreshLayout.OnRefreshListener {
    private FragmentPareHtmlBinding binding;
    private List<ItemData> itemDatas;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPareHtmlBinding.inflate(
                inflater,
                container,
                false
        );
        //choQuay
        binding.refresh.setOnRefreshListener(this);
        binding.refresh.setRefreshing(true);
        binding.rc.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.rc.setAdapter(new MonthItemAdapter(this));

        pareHtml();
        return binding.getRoot();
    }

    private void pareHtml(){
        String link = getArguments().getString("PATH");
        String key = getArguments().getString("KEY");
        new AsyncTask<String, Void, List<ItemData>>(){
            @Override
            protected List<ItemData> doInBackground(String... values) {
                List<ItemData> itemDatas =new ArrayList<>();
                try {
                    Document c = Jsoup.connect(values[0]).get();
                    Elements els = c.select("div."+values[1]);
                    for (Element el : els) {
                        String title =
                                el.select("h3").text();
                        String content = el.select("p").text();
                        String image =el.select("p").first()
                                .select("img.lazyload")
                                .attr("data-src");
                        String linkDetail = el.select("a")
                                .attr("href");
                        itemDatas.add(
                                new ItemData(title,
                                        content,
                                        image,
                                        linkDetail)
                        );
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return itemDatas;
            }

            @Override
            protected void onPostExecute(List<ItemData> itemData) {
                itemDatas = itemData;
                binding.rc.getAdapter().notifyDataSetChanged();
                binding.refresh.setRefreshing(false);
            }
        }.execute(link, key);
    }

    @Override
    public int getCount() {
        if (itemDatas == null){
            return 0;
        }
        return itemDatas.size();
    }

    @Override
    public ItemData getData(int position) {
        return itemDatas.get(position);
    }

    @Override
    public void onRefresh() {
        pareHtml();

    }
}
