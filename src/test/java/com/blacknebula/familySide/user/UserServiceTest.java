package com.blacknebula.familySide.user;

import com.blacknebula.familySide.ApplicationTest;
import com.blacknebula.familySide.common.CustomException;
import com.google.common.collect.ImmutableSet;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
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
     * @see UserService#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldThrowAnExceptionIfUsernameIsEmptyOrNull() throws Exception {
        StepVerifier.create(
                // when
                userService.signUp(Mono.just(UserDto.newBuilder()
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
     * @see UserService#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldThrowAnExceptionIfEmailIsEmptyOrNull() throws Exception {
        StepVerifier.create(
                // when
                userService.signUp(Mono.just(UserDto.newBuilder()
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
     * @see UserService#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldThrowAnExceptionIfPasswordIsEmptyOrNull() throws Exception {
        StepVerifier.create(
                // when
                userService.signUp(Mono.just(UserDto.newBuilder()
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
     * @see UserService#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldThrowAnExceptionIfUsernameAlreadyExists() throws Exception {
        // given
        final String username = "Leo";

        StepVerifier.create(
                // given
                userRepository.save(UserEntity.newBuilder()
                        .username(username)//
                        .email("Layth@mail.com")//
                        .password("password")//
                        .build())
                        // when
                        .then(userService.signUp(Mono.just(UserDto.newBuilder()
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
     * @see UserService#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldCreateNewUserInDatabase() throws Exception {
        final String username = "Leo";

        StepVerifier.create(
                // when
                userService.signUp(Mono.just(UserDto.newBuilder()
                        .username(username)//
                        .password("password")//
                        .email("Leo@mail.com")//
                        .build()))
                        .thenMany(userRepository.findAll()) //
                        .map(UserEntity::getUsername) //
        )
                // then
                .expectNext(username)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return true if username already exists in database
     * @see UserService#checkUsernameExistence(String)
     */
    @Test
    public void checkUsernameExistence_shouldReturnTrueIfUsernameAlreadyExistsInDatabase() throws Exception {
        final String username = "Leo";
        // given
        StepVerifier.create(
                userRepository.save(UserEntity.newBuilder()
                        .username(username)//
                        .password("password")//
                        .email("Leo@mail.com")//
                        .build())
                        // when
                        .thenMany(userService.checkUsernameExistence(username))
        )
                // then
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return false if username does not exist in database
     * @see UserService#checkUsernameExistence(String)
     */
    @Test
    public void checkUsernameExistence_shouldReturnFalseIfUsernameDoesNotExistInDatabase() throws Exception {
        StepVerifier.create(
                // when
                userService.checkUsernameExistence("invalid_username") //
        )
                // then
                .expectNext(false)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies throw an exception if username is null
     * @see UserService#listFamilyMembers(reactor.core.publisher.Mono)
     */
    @Test
    public void listFamilyMembers_shouldThrowAnExceptionIfUsernameIsNull() throws Exception {
        StepVerifier.create(
                // when
                userService.listFamilyMembers(Mono.just("")))
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if username does not exist in database
     * @see UserService#listFamilyMembers(reactor.core.publisher.Mono)
     */
    @Test
    public void listFamilyMembers_shouldThrowAnExceptionIfUsernameDoesNotExistInDatabase() throws Exception {
        StepVerifier.create(
                // when
                userService.listFamilyMembers(Mono.just("invalid_username")))
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username does not exist");
                })
                .verify();
    }

    /**
     * @verifies return empty flux  if user has no family members
     * @see UserService#listFamilyMembers(reactor.core.publisher.Mono)
     */
    @Test
    public void listFamilyMembers_shouldReturnEmptyFluxIfUserHasNoFamilyMembers() throws Exception {
        // given
        final String username = "Leo";
        StepVerifier.create(
                userRepository.save(UserEntity.newBuilder()
                        .username(username)//
                        .password("password")//
                        .email("Leo@mail.com")//
                        .build())
                        // when
                        .thenMany(userService.listFamilyMembers(Mono.just(username)))
        )
                // then
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return flux of family members ids
     * @see UserService#listFamilyMembers(reactor.core.publisher.Mono)
     */
    @Test
    public void listFamilyMembers_shouldReturnFluxOfFamilyMembersIds() throws Exception {
        final String username = "Leo";
        final String[] familyMembersIds = {"Leo-father", "Leo-mother"};

        StepVerifier.create(
                // given
                userRepository.save(UserEntity.newBuilder()
                        .username(username)//
                        .password("password")//
                        .email("Leo@mail.com")//
                        .familyMembers(ImmutableSet.<String>builder()
                                .add(familyMembersIds)
                                .build())
                        .build())
                        // when
                        .thenMany(userService.listFamilyMembers(Mono.just(username)))
        )
                // then
                .expectNext(familyMembersIds)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies throw an exception if username is empty or null
     * @see UserService#findByUsername(String)
     */
    @Test
    public void findByUsername_shouldThrowAnExceptionIfUsernameIsEmptyOrNull() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies throw an exception if username does not exist in database
     * @see UserService#findByUsername(String)
     */
    @Test
    public void findByUsername_shouldThrowAnExceptionIfUsernameDoesNotExistInDatabase() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return User by username
     * @see UserService#findByUsername(String)
     */
    @Test
    public void findByUsername_shouldReturnUserByUsername() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }
}
