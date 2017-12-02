package com.blacknebula.familySide.tracking;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface to manage {@link UserCoordinatesEntity} instances.
 */
interface TrackingRepository extends ReactiveCrudRepository<UserCoordinatesEntity, String>, CustomTrackingRepository {

    /**
     * Derived query selecting by {@code username}.
     *
     * @param username username
     * @return Flux of UserCoordinatesEntity
     * todo use stream instead
     */
    Flux<UserCoordinatesEntity> findByUsername(String username);
}