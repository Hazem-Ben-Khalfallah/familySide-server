package com.blacknebula.familySide.authentication;

import com.blacknebula.familySide.common.CustomException;
import com.blacknebula.familySide.common.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;

@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * checks username exists
     *
     * @param username username
     * @return Mono of Boolean.
     * @should return true if username already exists in database
     * @should return false if username does not exist in database
     */
    public Mono<Boolean> checkUsernameExistence(String username) {
        return userRepository.existsByUsername(username);
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
    public Mono<Void> signIn(Mono<UserDto> userDto) {
        return userDto
                .map(u -> {
                    if (StringUtils.isEmpty(u.getUsername())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(u.getEmail())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "email should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(u.getPassword())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "password should not be empty nor null");
                    }
                    return u;
                })
                .zipWhen(u -> checkUsernameExistence(u.getUsername()))
                .map(zip -> {
                    if (zip.getT2()) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username already used");
                    }

                    return UserEntity.newBuilder()
                            .username(zip.getT1().getUsername())
                            .password(zip.getT1().getPassword())
                            .email(zip.getT1().getEmail())
                            .creationDate(new Date())
                            .build();
                })
                .flatMap(userRepository::save)
                .then();
    }

    /**
     * @param usernameMono Mono of username
     * @return Flux of family members username
     * @should throw an exception if username is null
     * @should throw an exception if username does not exist in database
     * @should return empty flux  if user has no family members
     * @should return flux of family members ids
     */
    public Flux<String> listFamilyMembers(Mono<String> usernameMono) {
        return usernameMono
                .map(username -> {
                    if (StringUtils.isEmpty(username)) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null");
                    }
                    return username;
                })
                .zipWhen(this::checkUsernameExistence)
                .map(zip -> {
                    if (!zip.getT2()) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username does not exist");
                    }
                    return zip.getT1();
                })
                .flatMap(userRepository::findByUsername)
                .flatMapMany(userEntity -> Flux.fromIterable(ObjectUtils.defaultIfNull(userEntity.getFamilyMembers(), Collections.emptySet())));
    }

}