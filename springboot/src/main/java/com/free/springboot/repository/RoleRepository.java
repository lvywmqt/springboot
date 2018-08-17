package com.free.springboot.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.free.springboot.entity.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
	
	List<Role> findRolesByUserId(Long id);

}
