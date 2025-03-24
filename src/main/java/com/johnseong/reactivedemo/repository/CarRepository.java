package com.johnseong.reactivedemo.repository;

import com.johnseong.reactivedemo.repository.entity.CarEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface CarRepository extends R2dbcRepository<CarEntity, Integer> {
}
