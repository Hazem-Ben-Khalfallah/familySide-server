package com.blacknebula.familySide.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

/**
 * @author hazem
 */
public class UserRepositoryImpl implements CustomUserRepository {
    @Autowired
    private ReactiveMongoOperations mongoTemplate;

    @Override
    public Mono<Boolean> existsByUsername(String username) {
        final Query query = new Query().addCriteria(Criteria.where("username").is(username));
        return mongoTemplate.exists(query, UserEntity.class);
    }
}
