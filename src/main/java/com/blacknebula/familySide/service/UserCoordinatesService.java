package com.blacknebula.familySide.service;

import com.blacknebula.familySide.model.PositionDto;
import com.blacknebula.familySide.model.UserCoordinates;
import com.blacknebula.familySide.repository.UserCoordinatesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class UserCoordinatesService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserCoordinatesService.class);
    @Autowired
    private UserCoordinatesRepository positionRepository;

    public Mono<Void> savePosition(Mono<PositionDto> positionDto) {
        return positionDto
                .map((PositionDto p) -> new UserCoordinates()
                        .withUsername(p.getUsername())
                        .atDate(new Date())
                        .withCoordinates(p.getLat(), p.getLang()))
                .flatMap(userCoordinates -> positionRepository.save(userCoordinates))
                .then();
    }

}