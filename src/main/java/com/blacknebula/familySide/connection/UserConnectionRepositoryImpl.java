package com.blacknebula.familySide.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author hazem
 */
public class UserConnectionRepositoryImpl implements CustomUserConnectionRepository {

    @Autowired
    private ReactiveMongoOperations mongoTemplate;

    @Override
    public Mono<Boolean> checkConnectionExistence(String userId1, String userId2) {
        final Criteria criteria1 = new Criteria().andOperator(where("userId1").is(userId1), where("userId2").is(userId2));
        final Criteria criteria2 = new Criteria().andOperator(where("userId2").is(userId1), where("userId1").is(userId2));
        final Criteria criteria1Or2 = new Criteria().orOperator(criteria1, criteria2);
        final Query query = new Query().addCriteria(criteria1Or2);
        return mongoTemplate.exists(query, UserConnectionEntity.class);
    }

    @Override
    public Flux<UserConnectionEntity> findByUserIdAndConnectionStatus(String userId, UserConnectionStatusEnum status) {
        final Criteria criteria = new Criteria() //
                .orOperator(where("userId1").is(userId), where("userId2").is(userId));
        if (status != null) {
            criteria.andOperator(where("status").is(status));
        }
        final Query query = new Query().addCriteria(criteria);
        return mongoTemplate.find(query, UserConnectionEntity.class);
    }
}
