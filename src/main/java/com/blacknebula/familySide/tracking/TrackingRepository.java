package com.blacknebula.familySide.tracking;

import org.springframework.data.mongodb.repository.InfiniteStream;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface to manage {@link UserCoordinatesEntity} instances.
 */
interface TrackingRepository extends ReactiveCrudRepository<UserCoordinatesEntity, String> {

    /**
     * Derived query selecting by {@code username}.
     *
     * @param username username
     * @return Flux of UserCoordinatesEntity
     */
    Flux<UserCoordinatesEntity> findByUsername(String username);

    /**
     * Use a tailable cursor to emit a stream of entities as new entities are written to the capped collection.
     *
     * @return infinit Flux of UserCoordinatesEntity
     */
    @InfiniteStream
    Flux<UserCoordinatesEntity> findWithTailableCursorBy();
}