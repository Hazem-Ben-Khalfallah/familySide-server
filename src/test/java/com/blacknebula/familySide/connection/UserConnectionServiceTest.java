package com.blacknebula.familySide.connection;

import com.blacknebula.familySide.ApplicationTest;
import com.blacknebula.familySide.common.CustomException;
import com.blacknebula.familySide.user.UserDto;
import com.blacknebula.familySide.user.UserService;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.blacknebula.familySide.connection.UserConnectionStatusEnum.ACCEPTED;
import static com.blacknebula.familySide.connection.UserConnectionStatusEnum.PENDING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * @author hazem
 */
public class UserConnectionServiceTest extends ApplicationTest {

    private UserConnectionService userConnectionService;
    @Mock
    private UserService userService;
    @Mock
    private UserConnectionRepository userConnectionRepository;

    @Before
    public void setup() {
        userConnectionService = new UserConnectionService(userConnectionRepository, userService);
    }

    /**
     * @verifies throw an exception if username is empty
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfUsernameIsEmpty() throws Exception {
        //given
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(null)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null")));
        when(userService.findByUsername(Mockito.eq(son)))
                .thenReturn(Mono.empty());

        StepVerifier //
                .create(
                        // when
                        userConnectionService.requestConnection(null, Mono.just(UserConnectionRequest.newBuilder()
                                .targetUsername(son)//
                                .build()))
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if targetUsername is empty
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfTargetUsernameIsEmpty() throws Exception {
        //given
        final String father = "Leo_father";
        StepVerifier //
                .create(
                        // when
                        userConnectionService.requestConnection(father, Mono.just(UserConnectionRequest.newBuilder()
                                .targetUsername(null)//
                                .build()))
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("Targeted username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if username does not exists
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfUsernameDoesNotExists() throws Exception {
        //given
        final String invalid_user = "invalid_user";
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(invalid_user)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User does not exist with username %s", invalid_user)));
        when(userService.findByUsername(Mockito.eq(son)))
                .thenReturn(Mono.empty());

        StepVerifier //
                .create(
                        // when
                        userConnectionService.requestConnection(invalid_user, Mono.just(UserConnectionRequest.newBuilder()
                                .targetUsername(son)//
                                .build()))
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
                    Assertions.assertThat(throwable.getMessage()).startsWith("User does not exist with username");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if targetUsername does not exists
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfTargetUsernameDoesNotExists() throws Exception {
        //given
        final String father = "Leo_father";
        final String invalidTarget = "invalid_target";
        when(userService.findByUsername(Mockito.eq(father)))
                .thenReturn(Mono.empty());
        when(userService.findByUsername(Mockito.eq(invalidTarget)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "User does not exist with username %s", invalidTarget)));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.requestConnection(father, Mono.just(UserConnectionRequest.newBuilder()
                                .targetUsername(invalidTarget)//
                                .build()))
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
                    Assertions.assertThat(throwable.getMessage()).startsWith("User does not exist with username");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if there is already an ongoing request for the same targetUsername
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldThrowAnExceptionIfThereIsAlreadyAnOngoingRequestForTheSameTargetUsername() throws Exception {
        //given
        final String father = "Leo_father";
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(father)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id("user-id")
                        .username(father)
                        .build()));
        when(userService.findByUsername(Mockito.eq(son)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id("target-id")
                        .username(son)
                        .build()));
        when(userConnectionRepository.checkConnectionExistence(anyString(), anyString()))
                .thenReturn(Mono.just(true));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.requestConnection(father, Mono.just(UserConnectionRequest.newBuilder()
                                .targetUsername(son)//
                                .build()))
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.TOO_MANY_REQUESTS);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase(String.format("There is already a connection between %s and %s", father, son));
                })
                .verify();
    }

    /**
     * @verifies create a connection between user an the target with a pending status
     * @see UserConnectionService#requestConnection(String, reactor.core.publisher.Mono)
     */
    @Test
    public void requestConnection_shouldCreateAConnectionBetweenUserAnTheTargetWithAPendingStatus() throws Exception {
        //given
        final String fatherId = "user-id-1";
        final String father = "Leo_father";
        final String sonId = "user-id-2";
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(father)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(fatherId)
                        .username(father)
                        .build()));
        when(userService.findByUsername(Mockito.eq(son)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(sonId)
                        .username(son)
                        .build()));
        when(userConnectionRepository.checkConnectionExistence(anyString(), anyString()))
                .thenReturn(Mono.just(false));
        when(userConnectionRepository.save(any()))
                .thenReturn(Mono.empty());

        StepVerifier //
                .create(
                        // when
                        userConnectionService.requestConnection(father, Mono.just(UserConnectionRequest.newBuilder()
                                .targetUsername(son)//
                                .build()))
                )
                // then
                .then(() -> Mockito.verify(userConnectionRepository, times(1))
                        .save(Mockito.eq(UserConnectionEntity.newBuilder()
                                .userId1(fatherId)
                                .userId2(sonId)
                                .status(PENDING)
                                .build())))
                .expectComplete()
                .verify();
    }

    /**
     * @verifies should throw an exception if username is empty
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldShouldThrowAnExceptionIfUsernameIsEmpty() throws Exception {
        //given
        when(userService.findByUsername(Mockito.eq(null)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null")));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.listConnections(null, PENDING)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies should throw an exception if username does not exists
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldShouldThrowAnExceptionIfUsernameDoesNotExists() throws Exception {
        //given
        final String invalidUser = "invalidUser";
        when(userService.findByUsername(Mockito.eq(invalidUser)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.NOT_FOUND, String.format("User does not exist with username %s", invalidUser))));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.listConnections(invalidUser, PENDING)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
                    Assertions.assertThat(throwable.getMessage()).startsWith("User does not exist with username");
                })
                .verify();
    }

    /**
     * @verifies list all connection requests related to a user
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldListAllConnectionRequestsRelatedToAUser() throws Exception {
        // given
        final String fatherId = "user-id-1";
        final String father = "Leo_father";
        final String motherId = "user-id-2";
        final String mother = "Leo_mother";
        final String sonId = "user-id-3";
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(father)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(fatherId)
                        .username(father)
                        .build()));

        when(userService.findById(Mockito.eq(sonId)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(sonId)
                        .username(son)
                        .build()));

        when(userService.findById(Mockito.eq(motherId)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(motherId)
                        .username(mother)
                        .build()));

        when(userConnectionRepository.findByUserIdAndConnectionStatus(Mockito.eq(fatherId), Mockito.eq(null)))
                .thenReturn(Flux.just(UserConnectionEntity.newBuilder()
                                .id("connection-1")
                                .userId1(fatherId)
                                .userId2(motherId)
                                .status(PENDING)
                                .build(),
                        UserConnectionEntity.newBuilder()
                                .id("connection-2")
                                .userId1(fatherId)
                                .userId2(sonId)
                                .status(ACCEPTED)
                                .build()));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.listConnections(father, null)
                )
                // then
                .expectNext(UserConnectionDto.newBuilder()
                                .status(PENDING)
                                .username(mother)
                                .connectionId("connection-1")
                                .build(),
                        UserConnectionDto.newBuilder()
                                .status(ACCEPTED)
                                .username(son)
                                .connectionId("connection-2")
                                .build())
                .verifyComplete();
    }

    /**
     * @verifies return filtered connection requests list related to a user
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturnFilteredConnectionRequestsListRelatedToAUser() throws Exception {
        // given
        final String fatherId = "user-id-1";
        final String father = "Leo_father";
        final String motherId = "user-id-2";
        final String mother = "Leo_mother";
        final String sonId = "user-id-3";
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(father)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(fatherId)
                        .username(father)
                        .build()));

        when(userService.findById(Mockito.eq(sonId)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(sonId)
                        .username(son)
                        .build()));

        when(userService.findById(Mockito.eq(motherId)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(motherId)
                        .username(mother)
                        .build()));

        when(userConnectionRepository.findByUserIdAndConnectionStatus(Mockito.eq(fatherId), Mockito.eq(ACCEPTED)))
                .thenReturn(Flux.just(
                        UserConnectionEntity.newBuilder()
                                .id("connection-2")
                                .userId1(fatherId)
                                .userId2(sonId)
                                .status(ACCEPTED)
                                .build()));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.listConnections(father, ACCEPTED)
                )
                // then
                .expectNext(
                        UserConnectionDto.newBuilder()
                                .status(ACCEPTED)
                                .username(son)
                                .connectionId("connection-2")
                                .build())
                .verifyComplete();
    }

    /**
     * @verifies return empty flux if the user has no connections
     * @see UserConnectionService#listConnections(String, UserConnectionStatusEnum)
     */
    @Test
    public void listConnections_shouldReturnEmptyFluxIfTheUserHasNoConnections() throws Exception {
        // given
        final String fatherId = "user-id-1";
        final String father = "Leo_father";
        final String motherId = "user-id-2";
        final String mother = "Leo_mother";
        final String sonId = "user-id-3";
        final String son = "Leo";
        when(userService.findByUsername(Mockito.eq(father)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(fatherId)
                        .username(father)
                        .build()));

        when(userService.findById(Mockito.eq(sonId)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(sonId)
                        .username(son)
                        .build()));

        when(userService.findById(Mockito.eq(motherId)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(motherId)
                        .username(mother)
                        .build()));

        when(userConnectionRepository.findByUserIdAndConnectionStatus(Mockito.eq(fatherId), Mockito.eq(null)))
                .thenReturn(Flux.empty());

        StepVerifier //
                .create(
                        // when
                        userConnectionService.listConnections(father, null)
                )
                // then
                .verifyComplete();
    }

    /**
     * @verifies throw an exception if username is empty
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfUsernameIsEmpty() throws Exception {
        //given
        final String connectionId = "connectionId";
        when(userService.findByUsername(Mockito.eq(null)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.BAD_REQUEST, "username should not be empty nor null")));
        when(userConnectionRepository.existsById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(true));
        when(userConnectionRepository.findById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(UserConnectionEntity.newBuilder()
                        .id(connectionId)
                        .build()));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.updateConnectionStatus(null, connectionId, ACCEPTED)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).startsWith("username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if connectionId is empty
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfConnectionIdIsEmpty() throws Exception {
        StepVerifier //
                .create(
                        // when
                        userConnectionService.updateConnectionStatus("Leo", null, ACCEPTED)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).startsWith("connectionId should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if username does not exists
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfUsernameDoesNotExists() throws Exception {
        //given
        final String invalidUser = "invalidUser";
        final String connectionId = "connectionId";
        when(userConnectionRepository.existsById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(true));
        when(userConnectionRepository.findById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(UserConnectionEntity.newBuilder()
                        .id(connectionId)
                        .build()));
        when(userService.findByUsername(Mockito.eq(invalidUser)))
                .thenReturn(Mono.error(new CustomException(HttpStatus.NOT_FOUND, String.format("User does not exist with username %s", invalidUser))));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.updateConnectionStatus(invalidUser, connectionId, ACCEPTED)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
                    Assertions.assertThat(throwable.getMessage()).startsWith("User does not exist with username");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if connectionId does not exists
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfConnectionIdDoesNotExists() throws Exception {
        //given
        final String invalidConnectionId = "InvalidConnectionId";
        when(userConnectionRepository.existsById(Mockito.eq(invalidConnectionId)))
                .thenReturn(Mono.just(false));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.updateConnectionStatus("Leo", invalidConnectionId, ACCEPTED)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.NOT_FOUND);
                    Assertions.assertThat(throwable.getMessage()).startsWith("connection does not exist with id");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if connectionId does not target the accepting user
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldThrowAnExceptionIfConnectionIdDoesNotTargetTheAcceptingUser() throws Exception {
        //given
        final String fatherId = "user-id-1";
        final String sonId = "user-id-1";
        final String son = "Leo";
        final String connectionId = "connectionId";
        when(userConnectionRepository.existsById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(true));
        when(userConnectionRepository.findById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(UserConnectionEntity.newBuilder()
                        .id(connectionId)
                        .userId1(fatherId)
                        .userId2("other_id")
                        .build()));
        when(userService.findByUsername(Mockito.eq(son)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(sonId)
                        .username(son)
                        .build()));

        StepVerifier //
                .create(
                        // when
                        userConnectionService.updateConnectionStatus(son, connectionId, ACCEPTED)
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.UNAUTHORIZED);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase(String.format("connection is not targeting the user %s", son));
                })
                .verify();
    }

    /**
     * @verifies change connection request status to accepted
     * @see UserConnectionService#updateConnectionStatus(String, String, UserConnectionStatusEnum)
     */
    @Test
    public void updateConnectionStatus_shouldChangeConnectionRequestStatusToAccepted() throws Exception {
        //given
        final String fatherId = "user-id-1";
        final String sonId = "user-id-1";
        final String son = "Leo";
        final String connectionId = "connectionId";
        when(userConnectionRepository.existsById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(true));
        when(userConnectionRepository.findById(Mockito.eq(connectionId)))
                .thenReturn(Mono.just(UserConnectionEntity.newBuilder()
                        .id(connectionId)
                        .userId1(fatherId)
                        .userId2(sonId)
                        .build()));
        when(userService.findByUsername(Mockito.eq(son)))
                .thenReturn(Mono.just(UserDto.newBuilder()
                        .id(sonId)
                        .username(son)
                        .build()));
        when(userConnectionRepository.save(any()))
                .thenReturn(Mono.empty());

        StepVerifier //
                .create(
                        // when
                        userConnectionService.updateConnectionStatus(son, connectionId, ACCEPTED)
                )
                // then
                .then(() -> Mockito.verify(userConnectionRepository, times(1))
                        .save(Mockito.eq(UserConnectionEntity.newBuilder()
                                .id(connectionId)
                                .userId1(fatherId)
                                .userId2(sonId)
                                .status(ACCEPTED)
                                .build())))
                .verifyComplete();
    }

}
