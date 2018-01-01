package com.blacknebula.familySide.connection;

import com.blacknebula.familySide.ApplicationTest;
import com.blacknebula.familySide.common.CustomException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * @author hazem
 */
public class UserConnectionControllerTest extends ApplicationTest {

    @Mock
    private UserConnectionService userConnectionService;

    private WebTestClient client;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.client = WebTestClient.bindToController(new UserConnectionController(userConnectionService)).build();
    }

    /**
     * @verifies return 200 status
     * @see UserConnectionController#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldReturn200Status() throws Exception {
        // given
        Mockito.when(userConnectionService.requestConnection(anyString(), any()))
                .thenReturn(Mono.empty());
        // when
        this.client.post()
                .uri("/user/{username}/connection", "Leo")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userConnectionService.requestConnection(anyString(), any()))
                .thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Dummy exception")));
        // when
        this.client.post()
                .uri("/user/{username}/connection", "Leo")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    /**
     * @verifies return 200 status
     * @see UserConnectionController#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturn200Status() throws Exception {
        // given
        Mockito.when(userConnectionService.listConnections(anyString(), any(UserConnectionStatusEnum.class)))
                .thenReturn(Flux.empty());
        // when
        this.client.get()
                .uri("/user/{username}/connection?status={status}", "Leo", UserConnectionStatusEnum.ACCEPTED)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userConnectionService.listConnections(anyString(), any(UserConnectionStatusEnum.class)))
                .thenReturn(Flux.error(new CustomException(HttpStatus.BAD_REQUEST, "Dummy exception")));
        // when
        this.client.get()
                .uri("/user/{username}/connection?status={status}", "Leo", UserConnectionStatusEnum.ACCEPTED)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    /**
     * @verifies return 200 status and update connection status to ccepted
     * @see UserConnectionController#acceptConnection(String, String)
     */
    @Test
    public void acceptConnection_shouldReturn200StatusAndUpdateConnectionStatusToAccepted() throws Exception {
        // given
        Mockito.when(userConnectionService.updateConnectionStatus(anyString(), anyString(), Mockito.eq(UserConnectionStatusEnum.ACCEPTED)))
                .thenReturn(Mono.empty());
        // when
        this.client.put()
                .uri("/user/{username}/connection/{connectionId}", "Leo", "connection-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#acceptConnection(String, String)
     */
    @Test
    public void acceptConnection_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userConnectionService.updateConnectionStatus(anyString(), anyString(), Mockito.eq(UserConnectionStatusEnum.ACCEPTED)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Dummy exception")));
        // when
        this.client.put()
                .uri("/user/{username}/connection/{connectionId}", "Leo", "connection-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    /**
     * @verifies return 200 status and update connection status to Refused
     * @see UserConnectionController#refuseConnection(String, String)
     */
    @Test
    public void refuseConnection_shouldReturn200StatusAndUpdateConnectionStatusToRefused() throws Exception {
        // given
        Mockito.when(userConnectionService.updateConnectionStatus(anyString(), anyString(), Mockito.eq(UserConnectionStatusEnum.REFUSED)))
                .thenReturn(Mono.empty());
        // when
        this.client.delete()
                .uri("/user/{username}/connection/{connectionId}", "Leo", "connection-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#refuseConnection(String, String)
     */
    @Test
    public void refuseConnection_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(userConnectionService.updateConnectionStatus(anyString(), anyString(), Mockito.eq(UserConnectionStatusEnum.REFUSED)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "Dummy exception")));
        // when
        this.client.delete()
                .uri("/user/{username}/connection/{connectionId}", "Leo", "connection-id")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
