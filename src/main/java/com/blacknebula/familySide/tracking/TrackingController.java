package com.blacknebula.familySide.tracking;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class TrackingController {

    private final TrackingService trackingService;


    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @RequestMapping(path = "/position", method = RequestMethod.POST)
    public Mono<Void> savePosition(@RequestBody Flux<UserCoordinatesDto> positionDto) {
        return trackingService.savePosition(positionDto);
    }

    @RequestMapping(path = "/position", method = RequestMethod.GET)
    public Flux<UserCoordinatesDto> listFamilyMembersLastCoordinates() {
        return trackingService.listFamilyMembersLastCoordinates();
    }

}