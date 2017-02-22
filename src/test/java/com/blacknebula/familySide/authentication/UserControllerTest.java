package com.blacknebula.familySide.authentication;

import com.blacknebula.familySide.ApplicationTest;
import org.junit.Before;
import org.junit.Ignore;
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
     * @see UserController#connect(reactor.core.publisher.Mono)
     */
    @Test
    public void connect_shouldReturn200Status() throws Exception {
        final String username = "Leo";
        // when
        this.client.post()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange(Mono.just(UserDto.builder()
                        .username(username)
                        .email("Leo@mail.com")
                        .password("password")
                        .build()), UserDto.class)
                .expectStatus()
                .isEqualTo(HttpStatus.OK.value());

        //then
        StepVerifier.create(userRepository.findOne(username))
                .expectNextCount(1);
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserController#connect(reactor.core.publisher.Mono)
     */
    @Test
    @Ignore
    public void connect_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        this.client.post()
                .uri("/user")
                .accept(MediaType.APPLICATION_JSON)
                .exchange(Mono.just(UserDto.builder()
                        .email("Leo@mail.com")
                        .password("password")
                        .build()), UserDto.class)
                .expectStatus().isBadRequest();
    }
}
