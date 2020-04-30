package com.t3h.demofragment.firstserver;
import com.t3h.demofragment.R;
import com.t3h.demofragment.databinding.FragmentRegisterBinding;
import com.t3h.demofragment.firstserver.model.BaseResponse;
import com.t3h.demofragment.firstserver.model.RegisterRequest;
import com.t3h.demofragment.firstserver.model.UserProfile;
import com.t3h.demofragment.retrofit.RetrofitUtils;
import com.t3h.demofragment.retrofit.Services;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment implements View.OnClickListener {
    private FragmentRegisterBinding binding;
    private Services services;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(
                inflater, container, false
        );
        services = RetrofitUtils.getServices();
        binding.btnRegister.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        RegisterRequest request = new RegisterRequest();
        request.setAvatar("http://vaiaodaimymy.com/wp-content/uploads/2017/06/V%E1%BA%A3i-%C3%A1o-d%C3%A0i-hoa-c%C3%BAc-AD-HG-HT1718-2.jpg");
        request.setUsername(binding.tvUsername.getText().toString());
        request.setPassword(binding.tvPassword.getText().toString());
        request.setDob(binding.tvDob.getText().toString());
        services.register(request)
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
    }
}
