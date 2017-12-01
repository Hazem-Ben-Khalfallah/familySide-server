package com.blacknebula.familySide.tracking;

import com.blacknebula.familySide.authentication.UserService;
import com.blacknebula.familySide.common.CustomException;
import com.blacknebula.familySide.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
class TrackingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingService.class);

    private final TrackingRepository positionRepository;
    private final UserService userService;

    @Autowired
    public TrackingService(TrackingRepository positionRepository, UserService userService) {
        this.positionRepository = positionRepository;
        this.userService = userService;
    }

    /**
     * @param positions Flux of user coordinates
     * @return Mono Void
     * @should save user coordinates in DB
     * @should throw an exception if username is null
     * @should throw an exception if username does not exist
     * @should throw an exception if date is null
     * @should throw an exception if date is in the future
     * @should throw an exception if lat is null
     * @should throw an exception if lang is null
     */
    Mono<Void> savePosition(Flux<UserCoordinatesDto> positions) {
        return positions
                .map(userCoordinatesDto -> {
                    if (StringUtils.isEmpty(userCoordinatesDto.getUsername())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(userCoordinatesDto.getLat())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "Latitude should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(userCoordinatesDto.getLang())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "Longitude should not be empty nor null");
                    }
                    if (StringUtils.isEmpty(userCoordinatesDto.getDate())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "date should not be empty nor null");
                    }
                    if (userCoordinatesDto.getDate().after(new Date())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "date should be in the past");
                    }
                    return userCoordinatesDto;
                })
                .flatMap(u -> Mono.zip(Mono.justOrEmpty(u), userService.checkUsernameExistence(u.getUsername())))
                .map(zip -> {
                    if (!zip.getT2()) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "username does not exist");
                    }
                    return UserCoordinatesEntity.newBuilder()
                            .username(zip.getT1().getUsername())
                            .date(zip.getT1().getDate())
                            .coordinates(new double[]{zip.getT1().getLat(), zip.getT1().getLang()})
                            .build();
                })
                //todo use bulk insert
                .flatMap(positionRepository::save)
                .then();
    }

    Flux<UserCoordinatesDto> listFamilyMembersLastCoordinates() {
        return positionRepository.findAll()
                .map(userCoordinatesEntity -> UserCoordinatesDto.newBuilder()
                        .username(userCoordinatesEntity.getUsername())
                        .date(userCoordinatesEntity.getDate())
                        .lat(userCoordinatesEntity.getCoordinates()[0])
                        .lang(userCoordinatesEntity.getCoordinates()[1])
                        .build());
    }
}