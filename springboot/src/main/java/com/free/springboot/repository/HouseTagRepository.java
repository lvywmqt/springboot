package com.free.springboot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.free.springboot.entity.HouseTag;


/**
 * Created by 瓦力.
 */
public interface HouseTagRepository extends CrudRepository<HouseTag, Long> {
    HouseTag findByNameAndHouseId(String name, Long houseId);

    List<HouseTag> findAllByHouseId(Long id);

    List<HouseTag> findAllByHouseIdIn(List<Long> houseIds);
}
