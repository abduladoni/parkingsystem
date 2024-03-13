package dev.abduladoni.parkingsystem.service.parkingfee;

import dev.abduladoni.parkingsystem.daos.ParkingTariffMetaDataRepository;
import dev.abduladoni.parkingsystem.model.ParkingTariffMetaData;
import dev.abduladoni.parkingsystem.model.ParkingVehicle;
import dev.abduladoni.parkingsystem.service.CacheService;
import dev.abduladoni.parkingsystem.service.CacheServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class ParkingFeeHelperImplTest {

    private ParkingFeeHelperImpl parkingFeeHelper;

    @BeforeEach
    public void setUp() {
        CacheService cacheService1 = new CacheServiceImpl(customImplParkingTariffMetaDataRepository());
        DailyParkingFeeStrategy dailyParkingFeeStrategy1 = new DailyParkingFeeStrategy(cacheService1);
        SundayParkingFeeStrategy sundayParkingFeeStrategy1 = new SundayParkingFeeStrategy();
        parkingFeeHelper = new ParkingFeeHelperImpl(dailyParkingFeeStrategy1, sundayParkingFeeStrategy1);
    }

    @Test
    @DisplayName("Calculate parking fee for Non Sunday daily parking")
    public void testCalculateParkingFeeForDailyParking() {
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStreetName("Test Street");
        parkingVehicle.setStartTime(LocalDateTime.of(2024, 3, 9, 9, 0));
        parkingVehicle.setEndTime(LocalDateTime.of(2024, 3, 9, 10, 0));

        BigDecimal result = parkingFeeHelper.calculateParkingFee(parkingVehicle);

        assertEquals(BigDecimal.valueOf(6), result);
    }

    @Test
    @DisplayName("Calculate parking fee for car registered on Saturday and unregistering on Monday")
    public void testCalculateParkingFeeForDailyParkingForMultipleDayParking() {
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStreetName("Test Street");
        parkingVehicle.setStartTime(LocalDateTime.of(2024, 3, 9, 8, 0));
        parkingVehicle.setEndTime(LocalDateTime.of(2024, 3, 11, 8, 0));

        BigDecimal result = parkingFeeHelper.calculateParkingFee(parkingVehicle);

        assertEquals(BigDecimal.valueOf(78), result);
    }

    @Test
    @DisplayName("Calculate parking fee for Sunday parking")
    public void testCalculateParkingFeeForSundayParking() {
        //Given
        ParkingVehicle parkingVehicle = new ParkingVehicle();
        parkingVehicle.setStartTime(LocalDateTime.of(2024, 3, 10, 9, 0));
        parkingVehicle.setEndTime(LocalDateTime.of(2024, 3, 10, 10, 0));

        //When
        BigDecimal result = parkingFeeHelper.calculateParkingFee(parkingVehicle);

        //Then
        assertEquals(BigDecimal.ZERO, result);
    }


    private ParkingTariffMetaDataRepository customImplParkingTariffMetaDataRepository(){
        ParkingTariffMetaDataRepository metaDataRepository = new ParkingTariffMetaDataRepository() {
            @Override
            public List<ParkingTariffMetaData> findAll() {
                ParkingTariffMetaData parkingTariffMetaData = new ParkingTariffMetaData();
                parkingTariffMetaData.setId(1L);
                parkingTariffMetaData.setStreetName("Test Street");
                parkingTariffMetaData.setTariffInCents(10);
                return List.of(parkingTariffMetaData);
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends ParkingTariffMetaData> S saveAndFlush(S entity) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> List<S> saveAllAndFlush(Iterable<S> entities) {
                return null;
            }

            @Override
            public void deleteAllInBatch(Iterable<ParkingTariffMetaData> entities) {

            }

            @Override
            public void deleteAllByIdInBatch(Iterable<Long> longs) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public ParkingTariffMetaData getOne(Long aLong) {
                return null;
            }

            @Override
            public ParkingTariffMetaData getById(Long aLong) {
                return null;
            }

            @Override
            public ParkingTariffMetaData getReferenceById(Long aLong) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> List<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public List<ParkingTariffMetaData> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> S save(S entity) {
                return null;
            }

            @Override
            public Optional<ParkingTariffMetaData> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(ParkingTariffMetaData entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends ParkingTariffMetaData> entities) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public List<ParkingTariffMetaData> findAll(Sort sort) {
                return null;
            }

            @Override
            public Page<ParkingTariffMetaData> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends ParkingTariffMetaData> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends ParkingTariffMetaData> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends ParkingTariffMetaData> boolean exists(Example<S> example) {
                return false;
            }

            @Override
            public <S extends ParkingTariffMetaData, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
                return null;
            }
        };

        return metaDataRepository;
    }

}