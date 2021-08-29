package com.docomo.purchase_service.controller;

import com.docomo.purchase_service.service.MsisdnNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("unused")
@ControllerAdvice
public class MsisdnNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(MsisdnNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String msisdnNotFoundHandler(Exception ex) {
        return ex.getMessage();
    }
}
