package com.example.authentication.service;

import com.example.authentication.model.LoginModel;
import com.example.authentication.model.ResponseModel;
import com.example.authentication.model.VendorRegisterModel;

public interface VendorAuthService {

    ResponseModel login (LoginModel loginModel);
    ResponseModel registration (VendorRegisterModel model);
}
