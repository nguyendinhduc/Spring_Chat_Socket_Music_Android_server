package com.t3h.demofragment.firstserver;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.FragmentLoginBinding;
import com.t3h.demofragment.firstserver.model.BaseResponse;
import com.t3h.demofragment.firstserver.model.LoginRequest;
import com.t3h.demofragment.firstserver.model.UserProfile;
import com.t3h.demofragment.retrofit.RetrofitUtils;
import com.t3h.demofragment.retrofit.Services;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener {
    private FragmentLoginBinding binding;
    private Services services;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(
                inflater, container, false
        );
        binding.btnLogin.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);
        services = RetrofitUtils.getServices();
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_register:
                ((FirstServerActivity)getActivity())
                        .openRegister();
                break;
            case R.id.btn_login:
                //login
                final LoginRequest request = new LoginRequest();
                request.setPassword(binding.tvPassword.getText().toString());
                request.setUsername(binding.tvUsername.getText().toString());
                services.login(request)
                        .enqueue(new Callback<BaseResponse<UserProfile>>() {
                            @Override
                            public void onResponse(Call<BaseResponse<UserProfile>> call, Response<BaseResponse<UserProfile>> response) {
                                if (response.body().isSuccess()){
                                    Toast.makeText(getContext(), "Success",
                                            Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), response.body().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse<UserProfile>> call, Throwable t) {
                                Toast.makeText(getContext(), t.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                break;
        }
    }
}
