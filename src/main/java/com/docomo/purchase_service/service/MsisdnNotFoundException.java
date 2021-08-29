package com.docomo.purchase_service.service;

public class MsisdnNotFoundException extends RuntimeException{

    private String msisdn;

    public MsisdnNotFoundException(String msisdn) {
        super("MSISDN" + msisdn + " not found");
        this.msisdn = msisdn;
    }

    public String getMsisdn() {
        return msisdn;
    }
}
