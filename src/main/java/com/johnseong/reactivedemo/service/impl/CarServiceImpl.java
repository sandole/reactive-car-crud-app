package com.johnseong.reactivedemo.service.impl;

import com.johnseong.reactivedemo.controller.dto.CarDto;
import com.johnseong.reactivedemo.repository.CarRepository;
import com.johnseong.reactivedemo.repository.entity.CarEntity;
import com.johnseong.reactivedemo.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Override
    public Mono<CarDto> getCarById(Integer carId) {
        return carRepository.findById(carId)
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }

    @Override
    public Mono<CarDto> createCar(CarDto carDto) {
        return carRepository.save(CarEntity.builder()
                .brand(carDto.brand())
                .kilowatt(carDto.kilowatt())
                .build())
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }

    @Override
    public Mono<CarDto> updateCar(Integer carId, CarDto carDto) {
        return carRepository.findById(carId)
                .flatMap(carEntity -> carRepository.save(CarEntity.builder()
                        .id(carEntity.getId())
                        .brand(carDto.brand())
                        .kilowatt(carDto.kilowatt())
                        .build()))
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }

    @Override
    public Mono<Void> deleteCar(Integer carId) {
        return carRepository.deleteById(carId);
    }

    @Override
    public Flux<CarDto> getAllCars() {
        return carRepository.findAll()
                .map(carEntity -> new CarDto(carEntity.getId(), carEntity.getBrand(), carEntity.getKilowatt()));
    }
}
