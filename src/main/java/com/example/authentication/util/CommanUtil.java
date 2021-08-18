package com.example.authentication.util;

import com.example.authentication.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CommanUtil {
    public ResponseModel create(String message, Object data, HttpStatus status) {
        ResponseModel rs = new ResponseModel();
        rs.setData(data);
        rs.setMessage(message);
        rs.setStatus(status);
        rs.setStatusCode(status.value());
        return rs;
    }
}
