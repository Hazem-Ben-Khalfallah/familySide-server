package com.blacknebula.familySide.authentication;

import com.blacknebula.familySide.ApplicationTest;
import com.blacknebula.familySide.common.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author hazem
 */

public class UserServiceTest extends ApplicationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    /**
     * @verifies throw an exception if username is empty or null
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfUsernameIsEmptyOrNull() throws Exception {
        StepVerifier.create(
                // when
                userService.signIn(Mono.just(UserDto.builder()
                        .email("Leo@mail.com")//
                        .password("password")//
                        .build())))
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if email is empty or null
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfEmailIsEmptyOrNull() throws Exception {
        StepVerifier.create(
                // when
                userService.signIn(Mono.just(UserDto.builder()
                        .username("Leo")//
                        .password("password")//
                        .build())))
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("email should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if password is empty or null
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfPasswordIsEmptyOrNull() throws Exception {
        StepVerifier.create(
                // when
                userService.signIn(Mono.just(UserDto.builder()
                        .username("Leo")//
                        .email("Leo@mail.com")//
                        .build())))
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("password should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if username already exists
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfUsernameAlreadyExists() throws Exception {
        // given
        final String username = "Leo";

        StepVerifier.create(
                // given
                userRepository.save(UserEntity.builder()
                        .username(username)//
                        .email("Layth@mail.com")//
                        .password("password")//
                        .build())
                        // when
                        .then(userService.signIn(Mono.just(UserDto.builder()
                                .username(username)//
                                .password("password")
                                .email("Leo@mail.com")//
                                .build()))))
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username already used");
                })
                .verify();
    }

    /**
     * @verifies create new user in database
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldCreateNewUserInDatabase() throws Exception {
        final String username = "Leo";

        StepVerifier.create(
                // when
                userService.signIn(Mono.just(UserDto.builder()
                        .username("Leo")//
                        .password("password")//
                        .email("Leo@mail.com")//
                        .build()))
                        .then(userRepository.findOne(username)))
                // then
                .consumeNextWith(userEntity -> {
                    Assertions.assertThat(userEntity).isNotNull();
                    Assertions.assertThat(userEntity.getUsername()).isEqualTo(username);
                })
                .expectComplete()
                .verify();
    }
}
