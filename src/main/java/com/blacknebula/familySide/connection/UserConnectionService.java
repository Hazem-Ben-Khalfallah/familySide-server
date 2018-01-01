package com.blacknebula.familySide.connection;

import com.blacknebula.familySide.common.CustomException;
import com.blacknebula.familySide.common.StringUtils;
import com.blacknebula.familySide.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.blacknebula.familySide.connection.UserConnectionStatusEnum.PENDING;

/**
 * @author hazem
 */
@Service
class UserConnectionService {

    private final UserConnectionRepository userConnectionRepository;
    private final UserService userService;

    @Autowired
    public UserConnectionService(UserConnectionRepository userConnectionRepository, UserService userService) {
        this.userConnectionRepository = userConnectionRepository;
        this.userService = userService;
    }


    /**
     * @param username              username
     * @param userConnectionDtoMono Mono of userConnectionDto
     * @return empty Mono
     * @should throw an exception if username is empty
     * @should throw an exception if targetUsername is empty
     * @should throw an exception if username does not exists
     * @should throw an exception if targetUsername does not exists
     * @should throw an exception if there is already an ongoing request for the same targetUsername
     * @should create a connection between user an the target with a pending status
     */
    Mono<Void> requestConnection(String username, Mono<UserConnectionRequest> userConnectionDtoMono) {
        return userConnectionDtoMono
                .map(userConnectionRequest -> {
                    if (StringUtils.isEmpty(userConnectionRequest.getTargetUsername())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST, "Targeted username should not be empty nor null");
                    }
                    return userConnectionRequest;
                })
                .flatMap(userConnectionRequest -> Mono.zip(
                        userService.findByUsername(username),
                        userService.findByUsername(userConnectionRequest.getTargetUsername())
                ))
                .flatMap(zip -> Mono.zip(
                        userConnectionRepository.checkConnectionExistence(zip.getT1().getId(), zip.getT2().getId()),
                        Mono.justOrEmpty(zip.getT1()),
                        Mono.justOrEmpty(zip.getT2())
                ))
                .map(zip -> {
                    if (zip.getT1()) {
                        throw new CustomException(HttpStatus.TOO_MANY_REQUESTS,
                                "There is already a connection between %s and %s", zip.getT2().getUsername(), zip.getT3().getUsername());
                    }
                    final UserConnectionEntity userConnectionEntity = UserConnectionEntity.newBuilder()
                            .userId1(zip.getT2().getId())
                            .userId2(zip.getT3().getId())
                            .status(PENDING)
                            .build();
                    return userConnectionRepository.save(userConnectionEntity);
                })
                .then();

    }

    /**
     * @param username username
     * @param status   user connection status
     * @return Flux of UserConnectionDto
     * @should should throw an exception if username is empty
     * @should should throw an exception if username does not exists
     * @should list all connection requests related to a user
     * @should return filtered connection requests list related to a user
     * @should return empty flux if the user has no connections
     */
    Flux<UserConnectionDto> listConnections(String username, UserConnectionStatusEnum status) {
        return userService.findByUsername(username)
                .flatMapMany(userDto -> userConnectionRepository.findByUserIdAndConnectionStatus(userDto.getId(), status))
                .flatMap(userConnectionEntity -> Mono.zip(Mono.justOrEmpty(userConnectionEntity), userService.findById(userConnectionEntity.getUserId2())))
                .map(zip -> UserConnectionDto.newBuilder()
                        .connectionId(zip.getT1().getId())
                        .username(zip.getT2().getUsername())
                        .status(zip.getT1().getStatus())
                        .build());
    }

    /**
     * @param username     username
     * @param connectionId connection id
     * @param status       connection new status
     * @return empty Mono
     * @should throw an exception if username is empty
     * @should throw an exception if connectionId is empty
     * @should throw an exception if username does not exists
     * @should throw an exception if connectionId does not exists
     * @should throw an exception if connectionId does not target the accepting user
     * @should change connection request status to accepted
     */
    Mono<Void> updateConnectionStatus(String username, String connectionId, UserConnectionStatusEnum status) {
        if (StringUtils.isEmpty(connectionId)) {
            return Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "connectionId should not be empty nor null"));
        }
        return Mono.just(connectionId)
                .flatMap(userConnectionRepository::existsById)
                .flatMap(connectionExists -> {
                    if (!connectionExists) {
                        throw new CustomException(HttpStatus.NOT_FOUND, "connection does not exist with id %s", connectionId);
                    }

                    return userConnectionRepository.findById(connectionId);
                })
                .flatMap(userConnectionEntity -> Mono.zip(Mono.just(userConnectionEntity), userService.findByUsername(username)))
                .map(zip -> {
                    if (!zip.getT1().getUserId2().equals(zip.getT2().getId())) {
                        throw new CustomException(HttpStatus.UNAUTHORIZED, "connection is not targeting the user %s", username);
                    }
                    zip.getT1().setStatus(status);
                    return userConnectionRepository.save(zip.getT1());
                })
                .then();
    }
}
