package com.example.farmfarm_refact.apiPayload;


import com.example.farmfarm_refact.apiPayload.code.BaseErrorCode;
import com.example.farmfarm_refact.apiPayload.exception.GeneralException;
import lombok.Getter;

@Getter
public class ExceptionHandler extends GeneralException {
    public ExceptionHandler(BaseErrorCode code) {
        super(code);
    }
}
