package com.t3h.demofragment.viewpager;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.ActivityPageBinding;

public class PageActivity extends AppCompatActivity {
    private ActivityPageBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,
                R.layout.activity_page);
        binding.vp.setAdapter(new MonthAdapter(getSupportFragmentManager()));
        binding.tab.setupWithViewPager(  binding.vp);
    }
}
