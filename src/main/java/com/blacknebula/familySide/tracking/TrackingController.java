package com.blacknebula.familySide.tracking;


import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TrackingController {

    private final TrackingService trackingService;


    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    /**
     * @param userCoordinatesDtoFlux flux of UserCoordinatesDto
     * @return Mono void
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/position", method = RequestMethod.POST)
    public Mono<Void> savePosition(@RequestBody Flux<UserCoordinatesDto> userCoordinatesDtoFlux) {
        return trackingService.savePosition(userCoordinatesDtoFlux);
    }

    /**
     * @param username username
     * @return flux of UserCoordinatesDto
     * @should return 200 status
     * @should return valid error status if an exception has been thrown
     */
    @RequestMapping(path = "/position/{username}/family", method = RequestMethod.GET)
    public Flux<UserCoordinatesDto> listFamilyMembersLastCoordinates(@PathVariable String username) {
        return trackingService.listFamilyMembersLastCoordinates(Mono.just(username));
    }

}