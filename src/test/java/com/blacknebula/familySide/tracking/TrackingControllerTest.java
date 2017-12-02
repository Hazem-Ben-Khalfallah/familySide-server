package com.blacknebula.familySide.tracking;

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

import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author hazem
 */
public class TrackingControllerTest extends ApplicationTest {

    @Mock
    private TrackingService trackingService;

    private WebTestClient client;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.client = WebTestClient.bindToController(new TrackingController(trackingService)).build();
    }

    /**
     * @verifies return 200 status
     * @see TrackingController#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldReturn200Status() throws Exception {
        // given
        Mockito.when(trackingService.savePosition(any())).thenReturn(Mono.empty());
        // when
        this.client.post()
                .uri("/position")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(UserCoordinatesDto.newBuilder()
                        .username("Leo")
                        .date(new Date())
                        .lat(1d)
                        .lang(1d)
                        .build()), UserCoordinatesDto.class)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see TrackingController#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(trackingService.savePosition(any())).thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username does not exist")));
        // when
        this.client.post()
                .uri("/position")
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(UserCoordinatesDto.newBuilder()
                        .username("Leo")
                        .date(new Date())
                        .lat(1d)
                        .lang(1d)
                        .build()), UserCoordinatesDto.class)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    /**
     * @verifies return 200 status
     * @see TrackingController#listFamilyMembersLastCoordinates(String)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldReturn200Status() throws Exception {
        // given
        Mockito.when(trackingService.listFamilyMembersLastCoordinates(any())).thenReturn(Flux.just(UserCoordinatesDto.newBuilder()
                .build()));
        // when
        this.client.get()
                .uri("/position/Leo/family")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see TrackingController#listFamilyMembersLastCoordinates(String)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        // given
        Mockito.when(trackingService.listFamilyMembersLastCoordinates(any())).thenReturn(Flux.error(new CustomException(HttpStatus.BAD_REQUEST, "username does not exist")));
        // when
        this.client.get()
                .uri("/position/Leo/family")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError();
    }
}
