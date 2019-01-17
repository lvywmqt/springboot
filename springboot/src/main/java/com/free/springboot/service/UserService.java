package com.free.springboot.service;

import com.free.springboot.dto.UserDTO;
import com.free.springboot.entity.User;

public interface UserService {

	User findUserByName(String userName,String password);

	User findUserByTelephone(String telephone);
	
	User addUserByPhone(String telephone);

	ServiceResult<UserDTO> findById(Long adminId);

}
