package com.docomo.purchase_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@ControllerAdvice
public class MethodArgumentNotValidAdvice {

    @Autowired
    private ObjectMapper objectMapper;

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String methodArgumentNotValidHandler(MethodArgumentNotValidException ex) throws Exception{
        Map<String, String> errorMap = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(n -> n.getField(), n -> "Invalid input ["+n.getCode()+"]"));
        return objectMapper.writeValueAsString(errorMap);
    }
}
