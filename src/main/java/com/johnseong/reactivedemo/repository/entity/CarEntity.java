package com.johnseong.reactivedemo.repository.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@Table("car")
public class CarEntity {

    @Id
    Integer id;

    String brand;

    Integer kilowatt;
}
