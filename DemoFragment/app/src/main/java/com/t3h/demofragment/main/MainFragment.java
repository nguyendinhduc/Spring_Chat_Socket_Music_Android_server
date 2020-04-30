package com.t3h.demofragment.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.demofragment.databinding.FragmentMainMusicBinding;
import com.t3h.demofragment.mediaplayer.fragment.ListMusicOfflineFragment;
import com.t3h.demofragment.mediaplayeronline.OnlineMusicFragment;
import com.t3h.demofragment.R;
import java.util.List;

public class MainFragment extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentMainMusicBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainMusicBinding.inflate(
                inflater,container,false
        );
        addOnlineFragment();
        binding.bottom.setOnNavigationItemSelectedListener(this);
        return binding.getRoot();
    }

    private void addOfflineFragment(){
        FragmentManager manager = getChildFragmentManager();
        ListMusicOfflineFragment fragment =
                (ListMusicOfflineFragment)manager
                        .findFragmentByTag(ListMusicOfflineFragment.class.getName());
        if (fragment != null && fragment.isVisible()){
            return;
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null && !fragment.isVisible()){
            hideAllFragment(manager,transaction);
            transaction.show(fragment);
            transaction.commit();
            return;
        }
        fragment = new ListMusicOfflineFragment();
        hideAllFragment(manager,transaction);
        transaction.add(R.id.content, fragment,
                fragment.getClass().getName());
        transaction.commit();
    }
    private void addOnlineFragment(){
        FragmentManager manager = getChildFragmentManager();
        OnlineMusicFragment fragment =
                (OnlineMusicFragment)manager.findFragmentByTag(OnlineMusicFragment.class.getName());
        if (fragment != null && fragment.isVisible()){
            return;
        }
        FragmentTransaction transaction = manager.beginTransaction();
        if (fragment != null && !fragment.isVisible()){
            hideAllFragment(manager,transaction);
            transaction.show(fragment);
            transaction.commit();
            return;
        }
        fragment = new OnlineMusicFragment();
        hideAllFragment(manager,transaction);
        transaction.add(R.id.content, fragment,
                fragment.getClass().getName());
        transaction.commit();
    }



    public static void hideAllFragment(FragmentManager manager,
                                FragmentTransaction transaction){
        //lay ra tat cac fragment duoc quan ly boi manager
        List<Fragment> fs =  manager.getFragments();
        for (Fragment f : fs) {
            if (f != null && f.isVisible()){
                transaction.hide(f);
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.online:
                addOnlineFragment();
                break;
            case R.id.offline:
                addOfflineFragment();
                break;
        }
        return true;
    }
}
