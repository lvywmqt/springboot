package com.free.springboot.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.codec.digest.DigestUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.free.springboot.dto.UserDTO;
import com.free.springboot.entity.Role;
import com.free.springboot.entity.User;
import com.free.springboot.repository.RoleRepository;
import com.free.springboot.repository.UserRepository;
import com.free.springboot.service.RoleService;
import com.free.springboot.service.ServiceResult;
import com.free.springboot.service.UserService;

@Service
public class


UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ModelMapper modelMapper;

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

	@Override
	public User findUserByTelephone(String telephone) {
		/*if(telephone == null || ("").equals(telephone)){
			return null;
		}
		User user = userRepository.findUserByPhoneNumber();
		if(user == null){
			return null;
		}
		List<Role> roles = roleRepository.findRolesByUserId(user.getId());
        if (roles == null || roles.isEmpty()) {
            throw new DisabledException("权限非法");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName())));
        user.setAuthorityList(authorities);
        return user;*/
		return null;
	}

	@Override
	@Transactional
	public User addUserByPhone(String telephone) {
		return null;
	}

	@Override
	public ServiceResult<UserDTO> findById(Long adminId) {
		User user = userRepository.findOne(adminId);
		if(user == null){
			return ServiceResult.notFound();
		}
		return ServiceResult.of(modelMapper.map(user, UserDTO.class));
	}
	
}
