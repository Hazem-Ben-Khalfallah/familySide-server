package com.blacknebula.familySide.tracking;

import reactor.core.publisher.Mono;

/**
 * @author hazem
 */
public interface CustomTrackingRepository {
    /**
     * find Most recent position for a given user
     *
     * @param username username
     */
    Mono<UserCoordinatesEntity> findMostRecentByUsername(String username);
}
