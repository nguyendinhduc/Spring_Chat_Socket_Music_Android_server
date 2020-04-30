package com.t3h.demofragment.firstserver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.t3h.demofragment.R;

public class FirstServerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_server);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new LoginFragment(),
                        LoginFragment.class.getName())
                .commit();
    }

    public void openRegister() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, new RegisterFragment(),
                        RegisterFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }
}
