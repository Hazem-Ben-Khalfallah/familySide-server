package com.blacknebula.familySide.user;

import com.blacknebula.familySide.ApplicationTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author hazem
 */
public class UserControllerTest extends ApplicationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;

    private WebTestClient client;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.client = WebTestClient.bindToController(new UserController(userService)).build();
    }


    /**
     * @verifies return 200 status
     * @see UserController#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldReturn200Status() throws Exception {
        final String username = "Leo";
        // when
        this.client.post()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(UserDto.newBuilder()
                        .username(username)
                        .email("Leo@mail.com")
                        .password("password")
                        .build()), UserDto.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatus.OK.value());

        //then
        StepVerifier.create(userRepository.findByUsername(username))
                .expectNextMatches(userEntity -> username.equals(userEntity.getUsername()));
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserController#signUp(reactor.core.publisher.Mono)
     */
    @Test
    public void signUp_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        this.client.post()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(UserDto.newBuilder()
                        .email("Leo@mail.com")
                        .password("password")
                        .build()), UserDto.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
