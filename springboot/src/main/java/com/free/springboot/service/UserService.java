package com.free.springboot.service;

import com.free.springboot.entity.User;

public interface UserService {

	User findUserByName(String userName,String password);

}
