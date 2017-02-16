package com.blacknebula.familySide.repository;

import com.blacknebula.familySide.model.UserCoordinates;
import org.springframework.data.mongodb.repository.InfiniteStream;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface to manage {@link UserCoordinates} instances.
 */
public interface UserCoordinatesRepository extends ReactiveCrudRepository<UserCoordinates, String> {

    /**
     * Derived query selecting by {@code username}.
     *
     * @param username username
     * @return Flux of UserCoordinates
     */
    Flux<UserCoordinates> findByUsername(String username);

    /**
     * Use a tailable cursor to emit a stream of entities as new entities are written to the capped collection.
     *
     * @return infinit Flux of UserCoordinates
     */
    @InfiniteStream
    Flux<UserCoordinates> findWithTailableCursorBy();
}