package com.blacknebula.familySide.web;


import com.blacknebula.familySide.model.PositionDto;
import com.blacknebula.familySide.service.UserCoordinatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserCoordinatesController {

    private final UserCoordinatesService userCoordinatesService;

    @Autowired
    public UserCoordinatesController(UserCoordinatesService userCoordinatesService) {
        this.userCoordinatesService = userCoordinatesService;
    }

    @RequestMapping(path = "/position", method = RequestMethod.POST)
    public Mono<Void> savePosition(@RequestBody Flux<PositionDto> positionDto) {
        return this.userCoordinatesService.savePosition(positionDto);
    }

}