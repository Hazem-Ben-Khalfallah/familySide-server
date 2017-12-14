package com.blacknebula.familySide.connection;

import com.blacknebula.familySide.ApplicationTest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

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
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return 200 status
     * @see UserConnectionController#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturn200Status() throws Exception {
        // when
        this.client.get()
                .uri("/user/Leo/connection?status={status}", UserConnectionStatusEnum.ACCEPTED)
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
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return 200 status and update connection status to ccepted
     * @see UserConnectionController#acceptConnection(String, String)
     */
    @Test
    public void acceptConnection_shouldReturn200StatusAndUpdateConnectionStatusToAccepted() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#acceptConnection(String, String)
     */
    @Test
    public void acceptConnection_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return 200 status and update connection status to Refused
     * @see UserConnectionController#refuseConnection(String, String)
     */
    @Test
    public void refuseConnection_shouldReturn200StatusAndUpdateConnectionStatusToRefused() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }

    /**
     * @verifies return valid error status if an exception has been thrown
     * @see UserConnectionController#refuseConnection(String, String)
     */
    @Test
    public void refuseConnection_shouldReturnValidErrorStatusIfAnExceptionHasBeenThrown() throws Exception {
        //TODO auto-generated
        Assert.fail("Not yet implemented");
    }
}
