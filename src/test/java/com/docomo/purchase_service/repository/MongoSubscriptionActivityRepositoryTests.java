package com.docomo.purchase_service.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@DataMongoTest
public class MongoSubscriptionActivityRepositoryTests {

    @Autowired
    private MongoTemplate mongoTemplate;
    private MongoSubscriptionActivityRepository cut;

    @BeforeEach
    public void setUp() {
        this.cut = new MongoSubscriptionActivityRepository(mongoTemplate);
    }

    @AfterEach
    public void tearDown() {
        mongoTemplate.dropCollection("subscription");
    }

    @Test
    public void shouldInsertDocumentWhenMsisdnDoesNotExist() {
        MongoSubscriptionActivityRepository cut = new MongoSubscriptionActivityRepository(mongoTemplate);
        String msisdn = "msisdn";

        Transaction transaction = Transaction.of(50.0);
        String id = cut.addTransactionToSubscriptionAndUpdateStatus(msisdn, transaction);
        assertThat(id).isNotNull();

        List<SubscriptionActivity> actual = mongoTemplate.find(new Query(where("msisdn").is(msisdn)), SubscriptionActivity.class);
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);

        SubscriptionActivity first = actual.get(0);
        assertThat(first.getCreatedOn()).isNotNull();
        assertThat(first.getStatus()).isEqualTo(transaction.getAmount());
    }

    @Test
    public void shouldUpdateDocumentWhenMsisdnDoesExist() {
        MongoSubscriptionActivityRepository cut = new MongoSubscriptionActivityRepository(mongoTemplate);
        String msisdn = "msisdn";

        //set-up
        SubscriptionActivity existing = SubscriptionActivity.of(msisdn, 60.0);
        mongoTemplate.save(existing);

        Transaction transaction = Transaction.of(50.0);
        String id = cut.addTransactionToSubscriptionAndUpdateStatus(msisdn, transaction);
        assertThat(id).isNull();

        List<SubscriptionActivity> actual = mongoTemplate.find(new Query(where("msisdn").is(msisdn)), SubscriptionActivity.class);
        assertThat(actual).isNotNull();
        assertThat(actual.size()).isEqualTo(1);

        SubscriptionActivity first = actual.get(0);
        assertThat(first.getStatus()).isEqualTo(existing.getStatus() + transaction.getAmount());
    }

    @Test
    public void shouldFindDocument() {
        String msisdn = "msisdn";

        //set-up
        SubscriptionActivity existing = SubscriptionActivity.of(msisdn, 0);
        mongoTemplate.save(existing, "subscription");

        SubscriptionActivity actual = cut.findByMsisdn(msisdn);
        assertThat(actual).isNotNull();
    }

}
