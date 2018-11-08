package com.free.springboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.free.springboot.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	User findUserByName(String name);

	//User findUserByPhoneNumber();
}
