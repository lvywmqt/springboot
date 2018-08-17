package com.free.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.free.springboot.entity.Role;
import com.free.springboot.entity.User;
import com.free.springboot.repository.RoleRepository;
import com.free.springboot.repository.UserRepository;
import com.free.springboot.service.RoleService;
import com.free.springboot.service.UserService;

@Service
public class


UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

	@Override
	public User findUserByName(String name,String password) {
		User user = userRepository.findUserByName(name);
		if(user == null){
			return null;
		}
		if(user.getPassword().equals(DigestUtils.md5(password))){
			return null;
		}
		List<Role> roles = roleRepository.findRolesByUserId(user.getId());
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if(roles == null || roles.isEmpty()) {
            return null;
        }
		roles.forEach(role ->  authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
		return user;
	}
	
}