package com.docomo.purchase_service.repository;

import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.bson.BsonObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.time.Instant;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@Component
public class MongoSubscriptionActivityRepository implements SubscriptionActivityRepository {

    private MongoTemplate mongoTemplate;

    @SuppressWarnings("unused")
    public MongoSubscriptionActivityRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public String addTransactionToSubscriptionAndUpdateStatus(String msisdn, Transaction transaction) {
        Update update = new Update()
            .setOnInsert("createdOn", Instant.now())
            .addToSet("trans", transaction)
            .inc("status", transaction.getAmount());
        UpdateResult updateResult = mongoTemplate.update(SubscriptionActivity.class)
                .matching(new Query(where("msisdn").is(msisdn)))
                .apply(update)
                .upsert();
        if (updateResult.getMatchedCount()==1) {
            return null;
        }
        BsonObjectId id = updateResult.getUpsertedId() == null ? null : updateResult.getUpsertedId().asObjectId();
        return id == null ? null : id.toString();
    }

    @Override
    public SubscriptionActivity findByMsisdn(String msisdin) {
        Query query = new Query(where("msisdn").is(msisdin));
        query.fields().exclude("trans");
        return mongoTemplate.findOne(query, SubscriptionActivity.class);
    }
}
