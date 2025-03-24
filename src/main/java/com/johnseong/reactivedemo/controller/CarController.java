package com.johnseong.reactivedemo.controller;

import com.johnseong.reactivedemo.controller.dto.CarDto;
import com.johnseong.reactivedemo.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/car")
public class CarController {

    private final CarService carService;

    @GetMapping("/{carId}")
    Mono<CarDto> getCarById(@PathVariable("carId") Integer carId) {
        return carService.getCarById(carId);
    }

    @PostMapping
    Mono<CarDto> createCar(@RequestBody CarDto carDto) {
        return carService.createCar(carDto);
    }

    @PutMapping("/{carId}")
    Mono<CarDto> updateCar(@PathVariable("carId") Integer carId, @RequestBody CarDto carDto) {
        return carService.updateCar(carId, carDto);
    }

    @DeleteMapping("/{carId}")
    Mono<Void> deleteCar(@PathVariable("carId") Integer carId) {
        return carService.deleteCar(carId);
    }

    @GetMapping("/all")
    Flux<CarDto> getAllCars() {
        return carService.getAllCars();
    }
}
