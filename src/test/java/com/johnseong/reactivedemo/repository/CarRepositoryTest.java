package com.johnseong.reactivedemo.repository;

import com.johnseong.reactivedemo.repository.entity.CarEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataR2dbcTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void setUp() {
        // Clean up the car table
        Mono<Void> deleteAll = carRepository.deleteAll();

        StepVerifier.create(deleteAll)
                .verifyComplete();
    }

    @Test
    void testSaveAndFindById() {
        // Create a car entity
        CarEntity carEntity = CarEntity.builder()
                .brand("Tesla")
                .kilowatt(350)
                .build();

        // Save the car
        Mono<CarEntity> savedCarMono = carRepository.save(carEntity);

        // Find the car by id
        Mono<CarEntity> foundCarMono = savedCarMono.flatMap(savedCar ->
                carRepository.findById(savedCar.getId()));

        // Verify the car was saved and found correctly
        StepVerifier.create(foundCarMono)
                .expectNextMatches(foundCar ->
                        foundCar.getBrand().equals("Tesla") &&
                                foundCar.getKilowatt().equals(350))
                .verifyComplete();
    }

    @Test
    void testFindAll() {
        // Create and save multiple car entities
        CarEntity tesla = CarEntity.builder().brand("Tesla").kilowatt(350).build();
        CarEntity audi = CarEntity.builder().brand("Audi").kilowatt(300).build();
        CarEntity bmw = CarEntity.builder().brand("BMW").kilowatt(280).build();

        Flux<CarEntity> savedCarsFlux = carRepository.saveAll(Flux.just(tesla, audi, bmw));

        // Wait for all cars to be saved then find all
        Flux<CarEntity> allCarsFlux = savedCarsFlux.thenMany(carRepository.findAll());

        // Verify we get all three cars
        StepVerifier.create(allCarsFlux.collectList())
                .expectNextMatches(cars -> cars.size() == 3)
                .verifyComplete();
    }

    @Test
    void testUpdate() {
        // Create and save a car entity
        CarEntity car = CarEntity.builder()
                .brand("Porsche")
                .kilowatt(400)
                .build();

        // First save the car
        Mono<CarEntity> updateSequence = carRepository.save(car)
                .flatMap(savedCar -> {
                    // Then update it
                    savedCar.setKilowatt(450);
                    return carRepository.save(savedCar);
                });

        // Verify the update worked
        StepVerifier.create(updateSequence)
                .expectNextMatches(updatedCar ->
                        updatedCar.getBrand().equals("Porsche") &&
                                updatedCar.getKilowatt().equals(450))
                .verifyComplete();
    }

    @Test
    void testDelete() {
        // Create and save a car entity
        CarEntity car = CarEntity.builder()
                .brand("Ford")
                .kilowatt(250)
                .build();

        // Save the car, then delete it, then try to find it
        Mono<CarEntity> saveAndDeleteSequence = carRepository.save(car)
                .flatMap(savedCar -> carRepository.deleteById(savedCar.getId())
                        .then(carRepository.findById(savedCar.getId())));

        // Verify the car is no longer found after deletion
        StepVerifier.create(saveAndDeleteSequence)
                .verifyComplete(); // Should complete empty as the car was deleted
    }
}