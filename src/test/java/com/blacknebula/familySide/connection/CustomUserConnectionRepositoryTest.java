package com.blacknebula.familySide.connection;

import com.blacknebula.familySide.ApplicationTest;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

/**
 * @author hazem
 */
public class CustomUserConnectionRepositoryTest extends ApplicationTest {

    @Autowired
    private UserConnectionRepository userConnectionRepository;


    /**
     * @verifies return true if there is a connection between 2 users
     * @see CustomUserConnectionRepository#checkConnectionExistence(String, String)
     */
    @Test
    public void checkConnectionExistence_shouldReturnTrueIfThereIsAConnectionBetween2Users() throws Exception {
        StepVerifier.create(
                // given
                userConnectionRepository.save(UserConnectionEntity.newBuilder()
                        .userId1("1")
                        .userId2("2")
                        .build())
                        // when
                        .then(userConnectionRepository.checkConnectionExistence("1", "2")))
                // then
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return true if there is a connection between 2 users but with reversed params
     * @see CustomUserConnectionRepository#checkConnectionExistence(String, String)
     */
    @Test
    public void checkConnectionExistence_shouldReturnTrueIfThereIsAConnectionBetween2UsersButWithReversedParams() throws Exception {
        StepVerifier.create(
                // given
                userConnectionRepository.save(UserConnectionEntity.newBuilder()
                        .userId1("1")
                        .userId2("2")
                        .build())
                        // when
                        .then(userConnectionRepository.checkConnectionExistence("2", "1")))
                // then
                .expectNext(true)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return false if there is no connection between 2 users
     * @see CustomUserConnectionRepository#checkConnectionExistence(String, String)
     */
    @Test
    public void checkConnectionExistence_shouldReturnFalseIfThereIsNoConnectionBetween2Users() throws Exception {
        StepVerifier.create(
                // when
                userConnectionRepository.checkConnectionExistence("1", "2"))
                // then
                .expectNext(false)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return all connections if status is null
     * @see CustomUserConnectionRepository#findByUserIdAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUserIdAndConnectionStatus_shouldReturnAllConnectionsIfStatusIsNull() throws Exception {
        StepVerifier.create(
                // given
                userConnectionRepository.saveAll(ImmutableList.<UserConnectionEntity>builder()
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("1")
                                .userId2("2")
                                .status(UserConnectionStatusEnum.ACCEPTED)
                                .build())
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("1")
                                .userId2("3")
                                .status(UserConnectionStatusEnum.REFUSED)
                                .build())
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("4")
                                .userId2("1")
                                .status(UserConnectionStatusEnum.PENDING)
                                .build())
                        .build())
                        // when
                        .thenMany(userConnectionRepository.findByUserIdAndConnectionStatus("1", null)))
                // then
                .expectNextCount(3)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies filter connections if status is not null
     * @see CustomUserConnectionRepository#findByUserIdAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUserIdAndConnectionStatus_shouldFilterConnectionsIfStatusIsNotNull() throws Exception {
        StepVerifier.create(
                // given
                userConnectionRepository.saveAll(ImmutableList.<UserConnectionEntity>builder()
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("1")
                                .userId2("2")
                                .status(UserConnectionStatusEnum.ACCEPTED)
                                .build())
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("1")
                                .userId2("3")
                                .status(UserConnectionStatusEnum.REFUSED)
                                .build())
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("4")
                                .userId2("1")
                                .status(UserConnectionStatusEnum.PENDING)
                                .build())
                        .build())
                        // when
                        .thenMany(userConnectionRepository.findByUserIdAndConnectionStatus("1", UserConnectionStatusEnum.PENDING)))
                // then
                .expectNextCount(1)
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return empty flux if status is null and the user has no connections
     * @see CustomUserConnectionRepository#findByUserIdAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUserIdAndConnectionStatus_shouldReturnEmptyFluxIfStatusIsNullAndTheUserHasNoConnections() throws Exception {
        StepVerifier.create(
                // when
                userConnectionRepository.findByUserIdAndConnectionStatus("1", null))
                // then
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return empty flux if the user has no connections with the specified status
     * @see CustomUserConnectionRepository#findByUserIdAndConnectionStatus(String, UserConnectionStatusEnum)
     */
    @Test
    public void findByUserIdAndConnectionStatus_shouldReturnEmptyFluxIfTheUserHasNoConnectionsWithTheSpecifiedStatus() throws Exception {
        StepVerifier.create(
                // given
                userConnectionRepository.saveAll(ImmutableList.<UserConnectionEntity>builder()
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("1")
                                .userId2("2")
                                .status(UserConnectionStatusEnum.ACCEPTED)
                                .build())
                        .add(UserConnectionEntity.newBuilder()
                                .userId1("1")
                                .userId2("3")
                                .status(UserConnectionStatusEnum.REFUSED)
                                .build())
                        .build())
                        // when
                        .thenMany(userConnectionRepository.findByUserIdAndConnectionStatus("1", UserConnectionStatusEnum.PENDING)))
                // then
                .expectComplete()
                .verify();
    }
}
