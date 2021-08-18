package com.example.authentication.controller;

import com.example.authentication.model.CustomerRegistrationModel;
import com.example.authentication.model.LoginModel;
import com.example.authentication.model.ResponseModel;
import com.example.authentication.service.imp.CustomerAuthServiceImp;
import com.example.authentication.util.CommanUtil;
import com.example.authentication.util.Message;
import com.example.authentication.util.ObjectMapperUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("auth/customer/")
public class CustomerAuthController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAuthController.class);

    @Autowired
    private CustomerAuthServiceImp customerAuthServiceImp;

    @GetMapping("ping")
    public String ping() {
        return "pinging auth.... Success";
    }

    @PostMapping("login")
    public ResponseModel customerLogin(@RequestBody LoginModel loginModel) {
        return customerAuthServiceImp.login(loginModel);
    }

    @PostMapping("register")
    public ResponseModel register(HttpServletRequest req, @RequestParam("data") String data, MultipartHttpServletRequest multipartRequest) {
        try {
            CustomerRegistrationModel model;
            model = ObjectMapperUtil.getObjectMapper().readValue(data, CustomerRegistrationModel.class);

            final MultipartFile[] image = {null};
            multipartRequest.getFileMap().entrySet().stream().forEach(e -> {
                switch (e.getKey()) {
                    case "profile":
                        image[0] = e.getValue();
                        break;
                    default:
                        logger.warn("Some thing Wrong Add new product image fetch");
                        break;
                }
            });
            model.setProfileURL(image[0]);
            return customerAuthServiceImp.registration(model);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception {}", e.getMessage());
            return new CommanUtil().create(Message.SOMTHING_WRONG, null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
