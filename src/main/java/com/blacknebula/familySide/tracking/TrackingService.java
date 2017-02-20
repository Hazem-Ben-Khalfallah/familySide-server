package com.blacknebula.familySide.tracking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
class TrackingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrackingService.class);

    private final TrackingRepository positionRepository;

    @Autowired
    public TrackingService(TrackingRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    Mono<Void> savePosition(Flux<UserCoordinatesDto> positions) {
        return positions
                .map((UserCoordinatesDto p) -> UserCoordinatesEntity.builder()
                        .username(p.getUsername())
                        .date(p.getDate())
                        .coordinates(new double[]{p.getLat(), p.getLang()})
                        .build())
                .flatMap(userCoordinatesEntity -> positionRepository.save(userCoordinatesEntity))
                .then();
    }

    Flux<UserCoordinatesDto> listFamilyMembersLastCoordinates() {
        return positionRepository.findAll()
                .map(userCoordinatesEntity -> UserCoordinatesDto.builder()
                        .username(userCoordinatesEntity.getUsername())
                        .date(userCoordinatesEntity.getDate())
                        .lat(userCoordinatesEntity.getCoordinates()[0])
                        .lang(userCoordinatesEntity.getCoordinates()[1])
                        .build());
    }
}