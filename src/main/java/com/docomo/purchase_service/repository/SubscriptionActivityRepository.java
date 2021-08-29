package com.docomo.purchase_service.repository;

public interface SubscriptionActivityRepository {

    String addTransactionToSubscriptionAndUpdateStatus(String msisdn, Transaction transaction);

    SubscriptionActivity findByMsisdn(String msisdn);
}
