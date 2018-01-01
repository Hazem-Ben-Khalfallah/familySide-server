package com.blacknebula.familySide.connection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author hazem
 */
public interface CustomUserConnectionRepository {
    /**
     * Checks if there is a connection between 2 users
     *
     * @param userId1 first userId
     * @param userId2 second userId
     * @return Mono of Boolean
     * @should return true if there is a connection between 2 users
     * @should return true if there is a connection between 2 users but with reversed params
     * @should return false if there is no connection between 2 users
     */
    Mono<Boolean> checkConnectionExistence(String userId1, String userId2);

    /**
     * finds the list of connections related to a user. The returned collection will be filtered by status if it is not null.
     *
     * @param userId user id
     * @param status connection status. can be null
     * @return Flux of UserConnectionEntity
     *
     * @should return all connections if status is null
     * @should filter connections if status is not null
     * @should return empty flux if status is null and the user has no connections
     * @should return empty flux if the user has no connections with the specified status
     */
    Flux<UserConnectionEntity> findByUserIdAndConnectionStatus(String userId, UserConnectionStatusEnum status);
}
