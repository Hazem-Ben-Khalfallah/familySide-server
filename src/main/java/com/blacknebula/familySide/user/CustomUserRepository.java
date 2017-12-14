package com.blacknebula.familySide.user;

import reactor.core.publisher.Mono;

/**
 * @author hazem
 */
public interface CustomUserRepository {

    /**
     * Check existence by by {@code username}.
     *
     * @param username username
     * @return Mono of Boolean
     */
    Mono<Boolean> existsByUsername(String username);
}
