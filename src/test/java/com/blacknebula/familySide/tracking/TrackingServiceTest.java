package com.blacknebula.familySide.tracking;

import com.blacknebula.familySide.ApplicationTest;
import com.blacknebula.familySide.user.UserService;
import com.blacknebula.familySide.common.CustomException;
import com.blacknebula.familySide.common.DateUtils;
import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Comparator;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;

/**
 * @author hazem
 */
public class TrackingServiceTest extends ApplicationTest {

    private TrackingService trackingService;
    @Mock
    private UserService userService;
    @Autowired
    private TrackingRepository trackingRepository;

    @Before
    public void setup() {
        trackingService = new TrackingService(trackingRepository, userService);
    }

    /**
     * @verifies save user coordinates in DB
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldSaveUserCoordinatesInDB() throws Exception {
        //given
        final String username = "Leo";
        Mockito.when(userService.checkUsernameExistence(username)).thenReturn(Mono.just(true));

        StepVerifier //
                .create(
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
                .verify();


    }

    /**
     * @verifies throw an exception if username is null
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfUsernameIsNull() throws Exception {
        StepVerifier //
                .create(
                        // when
                        trackingService.savePosition(Flux.just(UserCoordinatesDto.newBuilder()
                                .username(null)//
                                .date(new Date()) //
                                .lat(1d) //
                                .lang(1d)
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
     * @verifies throw an exception if username does not exist
     * @see TrackingService#savePosition(reactor.core.publisher.Flux)
     */
    @Test
    public void savePosition_shouldThrowAnExceptionIfUsernameDoesNotExist() throws Exception {
        // given
        Mockito.when(userService.checkUsernameExistence(Mockito.anyString())).thenReturn(Mono.just(false));

        StepVerifier //
                .create(
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
        StepVerifier //
                .create(
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
        StepVerifier //
                .create(
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
        StepVerifier //
                .create(
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
        StepVerifier //
                .create(
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
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("Longitude should not be empty nor null");
                })
                .verify();
    }

    /**
     * @verifies throw an exception if username does not exist
     * @see TrackingService#listFamilyMembersLastCoordinates(Mono)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldThrowAnExceptionIfUsernameDoesNotExist() throws Exception {
        // given
        Mockito.when(userService.listFamilyMembers(any())) //
                .thenReturn(Flux.error(new CustomException(HttpStatus.BAD_REQUEST, "username does not exist"), true));

        StepVerifier //
                .create(
                        // when
                        trackingService.listFamilyMembersLastCoordinates(Mono.just("invalid_username"))
                )
                // then
                .consumeErrorWith(throwable -> {
                    Assertions.assertThat(throwable).isInstanceOf(CustomException.class);
                    Assertions.assertThat(((CustomException) throwable).getCustomErrorCode()).isEqualByComparingTo(HttpStatus.BAD_REQUEST);
                    Assertions.assertThat(throwable.getMessage()).isEqualToIgnoringCase("username does not exist");
                })
                .verify();
    }

    /**
     * @verifies return all related family members coordinates
     * @see TrackingService#listFamilyMembersLastCoordinates(Mono)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldReturnAllRelatedFamilyMembersCoordinates() throws Exception {
        // given
        final String username = "Leo";
        final String father = "Leo_father";
        final String mother = "Leo_mother";
        final Date now = new Date();
        Mockito.when(userService.listFamilyMembers(any())).thenReturn(Flux.just(father, mother));

        StepVerifier //
                .create(
                        trackingRepository.saveAll(ImmutableList.<UserCoordinatesEntity>builder() //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(father)
                                        .date(DateUtils.addDays(now, -2)) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(mother)
                                        .date(now) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(mother)
                                        .date(now) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .build())
                                // when
                                .thenMany(trackingService.listFamilyMembersLastCoordinates(Mono.just(username)))
                )
                // then
                .expectNextCount(2)
                .expectComplete()
                .verify();

    }

    /**
     * @verifies return related family members latest coordinates
     * @see TrackingService#listFamilyMembersLastCoordinates(Mono)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldReturnRelatedFamilyMembersLatestCoordinates() throws Exception {
        // given
        final String username = "Leo";
        final String father = "Leo_father";
        final String mother = "Leo_mother";
        final Date now = new Date();

        Mockito.when(userService.listFamilyMembers(any())).thenReturn(Flux.just(father, mother));

        StepVerifier //
                .create(
                        trackingRepository.saveAll(ImmutableList.<UserCoordinatesEntity>builder() //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(father)
                                        .date(DateUtils.addDays(now, -2)) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(father)
                                        .date(DateUtils.addDays(now, -1)) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(father)
                                        .date(now) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .add(UserCoordinatesEntity.newBuilder()//
                                        .username(mother)
                                        .date(now) //
                                        .coordinates(1d, 1d) //
                                        .build()) //
                                .build())
                                // when
                                .thenMany(trackingService.listFamilyMembersLastCoordinates(Mono.just(username)))
                                // sort output alphabetically
                                .sort(Comparator.comparing(UserCoordinatesDto::getUsername))
                )
                // then
                .expectNextMatches(userCoordinatesDto -> father.equals(userCoordinatesDto.getUsername()) && now.equals(userCoordinatesDto.getDate()))
                .expectNextCount(1)
                .expectComplete()
                .verify();

    }

    /**
     * @verifies return empty flux if the caller has no signed family members
     * @see TrackingService#listFamilyMembersLastCoordinates(Mono)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldReturnEmptyFluxIfTheCallerHasNoSignedFamilyMembers() throws Exception {
        // given
        final String username = "Leo";
        Mockito.when(userService.listFamilyMembers(any())).thenReturn(Flux.empty());
        // when
        StepVerifier //
                .create(
                        // when
                        trackingService.listFamilyMembersLastCoordinates(Mono.just(username))
                )
                // then
                .expectComplete()
                .verify();
    }

    /**
     * @verifies return empty flux if there is no coordinates for family members
     * @see TrackingService#listFamilyMembersLastCoordinates(Mono)
     */
    @Test
    public void listFamilyMembersLastCoordinates_shouldReturnEmptyFluxIfThereIsNoCoordinatesForFamilyMembers() throws Exception {
        // given
        final String username = "Leo";
        Mockito.when(userService.listFamilyMembers(any())).thenReturn(Flux.just("Leo_father", "Leo_mother"));
        // when
        StepVerifier //
                .create(
                        // when
                        trackingService.listFamilyMembersLastCoordinates(Mono.just(username))
                )
                // then
                .expectComplete()
                .verify();
    }
}
