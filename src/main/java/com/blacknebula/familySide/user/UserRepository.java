package com.blacknebula.familySide.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

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
    Mono<UserEntity> findByUsername(String username);

}