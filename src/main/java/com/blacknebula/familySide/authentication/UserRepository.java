package com.blacknebula.familySide.authentication;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

/**
 * Repository interface to manage {@link UserEntity} instances.
 */
interface UserRepository extends ReactiveCrudRepository<UserEntity, String>, CustomUserRepository {

    /**
     * Derived query selecting by {@code username}.
     *
     * @param username username
     * @return Flux of UserCoordinates
     */
    Flux<UserEntity> findByUsername(String username);

    /**
     * Use a tailable cursor to emit a stream of entities as new entities are written to the capped collection.
     *
     * @return infinite Flux of UserCoordinates
     */
    Flux<UserEntity> findWithTailableCursorBy();
}