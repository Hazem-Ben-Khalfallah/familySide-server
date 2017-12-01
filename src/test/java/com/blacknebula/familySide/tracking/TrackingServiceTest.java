package com.blacknebula.familySide.tracking;

import com.blacknebula.familySide.ApplicationTest;
import com.blacknebula.familySide.authentication.UserDto;
import com.blacknebula.familySide.authentication.UserService;
import com.blacknebula.familySide.common.CustomException;
import com.blacknebula.familySide.common.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

/**
 * @author hazem
 */
public class TrackingServiceTest extends ApplicationTest {

    @Autowired
    private TrackingService trackingService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrackingRepository trackingRepository;

    /**
     * @verifies save user coordinates in DB
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldSaveUserCoordinatesInDB() throws Exception {
        //given
        final String username = "Leo";
        userService.signIn(Mono.just(UserDto.newBuilder()
                .username(username)//
                .password("password")//
                .email("Leo@mail.com")//
                .build()))
                .doOnSuccess(aVoid -> StepVerifier.create(
                        // when
                        trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                                .username(username)//
                                .date(new Date()) //
                                .lat(1d) //
                                .lang(1d)
                                .build()))
                                .thenMany(trackingRepository.findAll()) //
                                .map(UserCoordinatesEntity::getUsername) //
                )
                        // then
                        .expectNext(username)
                        .expectComplete()
                        .verify());


    }

    /**
     * @verifies throw an exception if username is null
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfUsernameIsNull() throws Exception {
        StepVerifier.create(
                // when
                trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                        .username(null)//
                        .date(new Date()) //
                        .lat(1d) //
                        .lang(1d)
                        .build()))
                        .thenMany(trackingRepository.findAll()) //
                        .map(UserCoordinatesEntity::getUsername) //
        )
                // then
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if username does not exist
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfUsernameDoesNotExist() throws Exception {
        StepVerifier.create(
                // when
                trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                        .username("invalid_username")//
                        .date(new Date()) //
                        .lat(1d) //
                        .lang(1d)
                        .build()))
                        .thenMany(trackingRepository.findAll()) //
                        .map(UserCoordinatesEntity::getUsername) //
        )
                // then
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username does not exist");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if date is null
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfDateIsNull() throws Exception {
        StepVerifier.create(
                // when
                trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                        .username("Leo")//
                        .date(null) //
                        .lat(1d) //
                        .lang(1d)
                        .build()))
                        .thenMany(trackingRepository.findAll()) //
                        .map(UserCoordinatesEntity::getUsername) //
        )
                // then
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("date should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if date is in the future
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfDateIsInTheFuture() throws Exception {
        StepVerifier.create(
                // when
                trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                        .username("Leo")//
                        .date(DateUtils.addDays(new Date(), 1)) //
                        .lat(1d) //
                        .lang(1d)
                        .build()))
                        .thenMany(trackingRepository.findAll()) //
                        .map(UserCoordinatesEntity::getUsername) //
        )
                // then
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("date should be in the past");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if lat is null
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfLatIsNull() throws Exception {
        StepVerifier.create(
                // when
                trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                        .username("Leo")//
                        .date(new Date()) //
                        .lat(null) //
                        .lang(1d)
                        .build()))
                        .thenMany(trackingRepository.findAll()) //
                        .map(UserCoordinatesEntity::getUsername) //
        )
                // then
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("Latitude should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if lang is null
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfLangIsNull() throws Exception {
        StepVerifier.create(
                // when
                trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                        .username("Leo")//
                        .date(new Date()) //
                        .lat(1d) //
                        .lang(null)
                        .build()))
                        .thenMany(trackingRepository.findAll()) //
                        .map(UserCoordinatesEntity::getUsername) //
        )
                // then
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("Longitude should not be empty nor null");
                })
                .verify();
    }
}
