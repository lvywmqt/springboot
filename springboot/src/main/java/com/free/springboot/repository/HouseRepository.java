package com.free.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.free.springboot.entity.House;

public interface HouseRepository extends CrudRepository<House, Long> {

}
