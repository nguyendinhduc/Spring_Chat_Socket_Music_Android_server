package com.t3h.server.controller;

import com.t3h.server.DemoManager;
import com.t3h.server.model.database.UserProfile;
import com.t3h.server.model.request.LoginRequest;
import com.t3h.server.model.request.RegisterRequest;
import com.t3h.server.model.response.BaseResponse;
import com.t3h.server.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private DemoManager demoManager;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @PostMapping("/api/login")
    public Object login(
            @RequestBody LoginRequest request
            ){
        UserProfile userProfile=
                userProfileRepository.findOneByUsernamePassword(request.getUsername());
        if (userProfile == null){
            return new BaseResponse(false, "Your username don't exist");
        }
        if (!passwordEncoder.matches(request.getPassword(), userProfile.getPassword())){
            return new BaseResponse(false, "Your password invalid");
        }

        return new BaseResponse(userProfile);

    }
    @PostMapping(value = "/api/register")
    public Object register(
            @RequestBody RegisterRequest request
            ){
        UserProfile userProfile =
                userProfileRepository.findOneByUsernamePassword(request.getUsername());
        if (userProfile != null){
            return new BaseResponse(false,"This username exist");
        }
        userProfile = new UserProfile();
        userProfile.setAvatar(request.getAvatar());
        userProfile.setUsername(request.getUsername());

        userProfile.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        userProfile.setDob(request.getDob());
        userProfile = userProfileRepository.save(userProfile);
        return new BaseResponse(userProfile);
    }
}
