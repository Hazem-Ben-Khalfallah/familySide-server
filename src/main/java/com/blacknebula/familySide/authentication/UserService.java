package com.blacknebula.familySide.authentication;

import com.blacknebula.familySide.common.StringUtils;
import com.blacknebula.familySide.common.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param userDto user to be signed in
     * @return Mono void
     * @should throw an exception if username is empty or null
     * @should throw an exception if email is empty or null
     * @should throw an exception if password is empty or null
     * @should throw an exception if username already exists
     * @should create new user in database
     */
    Mono<Void> signIn(Mono<UserDto> userDto) {
        return userDto
                .doOnNext(u -> {
                    if (StringUtils.isEmpty(u.getUsername())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(u.getEmail())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "email should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(u.getPassword())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "password should not be empty nor null");
                    }
                })
                .and(u -> userRepository.exists(u.getUsername()))
                .map(u -> {
                    if (u.getT2()) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username already used");
                    }

                    return UserEntity.builder()
                            .username(u.getT1().getUsername())
                            .password(u.getT1().getPassword())
                            .email(u.getT1().getEmail())
                            .creationDate(new Date())
                            .build();
                })
                .flatMap(userRepository::save)
                .then();
    }
}