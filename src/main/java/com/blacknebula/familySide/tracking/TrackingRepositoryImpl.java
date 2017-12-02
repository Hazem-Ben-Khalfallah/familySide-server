package com.blacknebula.familySide.tracking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

/**
 * @author hazem
 */
public class TrackingRepositoryImpl implements CustomTrackingRepository {

    @Autowired
    private ReactiveMongoOperations mongoTemplate;

    @Override
    public Mono<UserCoordinatesEntity> findMostRecentByUsername(String username) {
        final Query query = new Query().addCriteria(Criteria.where("username").is(username))
                .with(new Sort(Sort.Direction.DESC, "date"))
                .limit(1);
        return mongoTemplate.findOne(query, UserCoordinatesEntity.class);
    }
}
