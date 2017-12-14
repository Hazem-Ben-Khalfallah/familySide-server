package com.blacknebula.familySide.connection;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * Repository interface to manage {@link UserConnectionEntity} instances.
 */
interface UserConnectionRepository extends ReactiveCrudRepository<UserConnectionEntity, String>, CustomUserConnectionRepository {

}