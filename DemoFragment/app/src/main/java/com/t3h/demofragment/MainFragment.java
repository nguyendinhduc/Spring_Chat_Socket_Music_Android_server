package com.t3h.demofragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.demofragment.databinding.FragmentMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements FileAdapter.IFile, View.OnClickListener {
    private List<ItemFile> itemFiles;
    private String parentPath;

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    private static final String TAG = "MainFragment";
    private FragmentMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate...............");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView...............");
        binding = FragmentMainBinding.inflate(inflater, container, false);
        getListItemFile(
               parentPath
        );
        binding.rc.setLayoutManager(new LinearLayoutManager(
                getActivity()
        ));
        binding.rc.setAdapter(new FileAdapter(this));
        binding.btnBack.setOnClickListener(this);
        return binding.getRoot();
    }

    private void getListItemFile(String parentPath) {
        itemFiles = new ArrayList<>();
        File fileParent = new File(parentPath);
        for (File file : fileParent.listFiles()) {
            ItemFile itemFile = new ItemFile();
            itemFile.setPath(file.getPath());
            itemFile.setTitle(file.getName());
            itemFile.setTypeFile(
                    file.isDirectory() ? "AHIHI" : null
            );
            itemFiles.add(itemFile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume...............");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause...............");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView...............");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroyView...............");
        super.onDestroy();
    }

    @Override
    public int getCount() {
        if (itemFiles == null){
            return 0;
        }
        return itemFiles.size();
    }

    @Override
    public ItemFile getData(int position) {
        return itemFiles.get(position);
    }

    @Override
    public void onClickItem(int position) {
        //check la la folder hay
        ItemFile item = itemFiles.get(position);
        if ("AHIHI".equals(item.getTypeFile())){
            ((MainActivity)getActivity()).addMainFragment(item.getPath(),
                    false);
        }
    }

    @Override
    public void onClick(View view) {
        getActivity().onBackPressed();
    }
}
