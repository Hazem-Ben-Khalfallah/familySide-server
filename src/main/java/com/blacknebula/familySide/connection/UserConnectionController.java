package com.blacknebula.familySide.connection;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.blacknebula.familySide.connection.UserConnectionStatusEnum.ACCEPTED;
import static com.blacknebula.familySide.connection.UserConnectionStatusEnum.REFUSED;

/**
 * @author hazem
 */
@RestController
public class UserConnectionController {

    private UserConnectionService userConnectionService;

    public UserConnectionController(UserConnectionService userConnectionService) {
        this.userConnectionService = userConnectionService;
    }

    /**
     * @param userConnectionDtoMono Mono of userConnectionDto
     * @param username              username of the request emitter
     * @return Mono void
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/user/{username}/connection", method = RequestMethod.POST)
    public Mono<Void> requestConnection(@PathVariable String username, @RequestBody Mono<UserConnectionRequest> userConnectionDtoMono) {
        return userConnectionService.requestConnection(username, userConnectionDtoMono);
    }

    /**
     * @param username username of the request destination
     * @return Mono void
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/user/{username}/connection", method = RequestMethod.GET)
    public Flux<UserConnectionDto> listConnections(@PathVariable String username, @RequestParam(name = "status", required = false) UserConnectionStatusEnum status) {
        return userConnectionService.listConnections(username, status);
    }


    /**
     * @param username     username of the request destination
     * @param connectionId user connection id
     * @return Mono void
     * @should return 200 status and update connection status to accepted
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/user/{username}/connection/{connectionId}", method = RequestMethod.PUT)
    public Mono<Void> acceptConnection(@PathVariable String username, @PathVariable String connectionId) {
        return userConnectionService.updateConnectionStatus(username, connectionId, ACCEPTED);
    }

    /**
     * @param username     username of the request destination
     * @param connectionId user connection id
     * @return Mono void
     * @should return 200 status and update connection status to Refused
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/user/{username}/connection/{connectionId}", method = RequestMethod.DELETE)
    public Mono<Void> refuseConnection(@PathVariable String username, @PathVariable String connectionId) {
        return userConnectionService.updateConnectionStatus(username, connectionId, REFUSED);
    }


}
