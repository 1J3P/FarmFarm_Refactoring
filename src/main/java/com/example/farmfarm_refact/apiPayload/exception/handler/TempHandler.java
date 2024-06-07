package com.example.farmfarm_refact.apiPayload.exception.handler;


import com.example.farmfarm_refact.apiPayload.code.BaseErrorCode;
import com.example.farmfarm_refact.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
