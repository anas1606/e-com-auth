package com.example.authentication.service;

import com.example.authentication.model.CustomerRegistrationModel;
import com.example.authentication.model.LoginModel;
import com.example.authentication.model.ResponseModel;

public interface CustomerAuthService {

    ResponseModel login (LoginModel loginModel);

    ResponseModel registration (CustomerRegistrationModel model);
}
