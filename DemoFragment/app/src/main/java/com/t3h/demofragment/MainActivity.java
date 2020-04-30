package com.t3h.demofragment;

import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addMainFragment(
                Environment.getExternalStorageDirectory().getPath(),true
        );
    }

    //qua trinh dua fragment vao trong activity
    public void addMainFragment(String path, boolean isRoot){
        index++;
        //lay FragmentManager
        FragmentManager manager =getSupportFragmentManager();
        //create Transaction
        FragmentTransaction transaction = manager.beginTransaction();

        //tao fragment
        MainFragment mainFragment = new MainFragment();
        mainFragment.setParentPath(path);
        if (!isRoot){
            transaction.setCustomAnimations(
                    R.anim.open_r_t_l,
                    R.anim.exit_r_t_l,
                    R.anim.open_l_t_r,
                    R.anim.exit_l_t_r
            );
        }

        transaction.replace(R.id.content,
                mainFragment,
                MainFragment.class.getName());
//        if (!isRoot){
        if (!isRoot){
            //xem lai
            transaction.addToBackStack(null) ;
        }
        transaction.commit();

    }




}
