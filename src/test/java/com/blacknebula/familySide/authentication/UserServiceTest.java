package com.blacknebula.familySide.authentication;

import com.blacknebula.familySide.common.exception.CustomException;
import com.mongodb.BasicDBObject;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author hazem
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setUp() throws Exception {
        clearMongo();
        initMocks(this);
    }

    /**
     * Remove all data from collections (expect system collections). Faster than
     * a simple dropDatabase.
     */
    private void clearMongo() throws Exception {
        mongoTemplate.getDb().listCollectionNames()
                .forEach((Consumer<? super String>) name -> {
                    if (!name.startsWith("system.")) {
                        mongoTemplate.getDb().getCollection(name)
                                .deleteMany(new BasicDBObject());
                    }
                });
    }

    /**
     * @verifies throw an exception if username is empty or null
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfUsernameIsEmptyOrNull() throws Exception {
        try {
            // when
            userService.signIn(Mono.just(UserDto.builder()
                    .email("Leo@mail.com")//
                    .password("password")//
                    .build()))
                    .block();
            Assert.fail("shouldThrowAnExceptionIfUsernameIsEmptyOrNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringCase("username should not be empty nor null");
        }
    }

    /**
     * @verifies throw an exception if email is empty or null
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfEmailIsEmptyOrNull() throws Exception {
        try {
            // when
            userService.signIn(Mono.just(UserDto.builder()
                    .username("Leo")//
                    .password("password")//
                    .build()))
                    .block();
            Assert.fail("shouldThrowAnExceptionIfEmailIsEmptyOrNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringCase("email should not be empty nor null");
        }
    }

    /**
     * @verifies throw an exception if password is empty or null
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfPasswordIsEmptyOrNull() throws Exception {
        try {
            // when
            userService.signIn(Mono.just(UserDto.builder()
                    .username("Leo")//
                    .email("Leo@mail.com")//
                    .build()))
                    .block();
            Assert.fail("shouldThrowAnExceptionIfPasswordIsEmptyOrNull");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringCase("password should not be empty nor null");
        }
    }

    /**
     * @verifies throw an exception if username already exists
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldThrowAnExceptionIfUsernameAlreadyExists() throws Exception {
        // given
        final String username = "Leo";
        userRepository.save(UserEntity.builder()
                .username(username)//
                .email("Layth@mail.com")//
                .password("password")//
                .build())
                .block();

        try {
            // when
            userService.signIn(Mono.just(UserDto.builder()
                    .username(username)//
                    .password("password")
                    .email("Leo@mail.com")//
                    .build()))
                    .block();
            Assert.fail("shouldThrowAnExceptionIfUsernameAlreadyExists");
        } catch (CustomException e) {
            // then
            Assertions.assertThat(e.getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
            Assertions.assertThat(e.getMessage()).isEqualToIgnoringCase("username already used");
        }

    }

    /**
     * @verifies create new user in database
     * @see UserService#signIn(reactor.core.publisher.Mono)
     */
    @Test
    public void signIn_shouldCreateNewUserInDatabase() throws Exception {
        final String username = "Leo";

        // when
        userService.signIn(Mono.just(UserDto.builder()
                .username(username)//
                .password("password")
                .email("Leo@mail.com")//
                .build()))
                .block();

        // then
        final UserEntity userEntity = userRepository.findOne(username)
                .block();
        Assertions.assertThat(userEntity).isNotNull();

    }
}
