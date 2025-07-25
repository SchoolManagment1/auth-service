package com.skm.authservice.service;


import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skm.authservice.dto.UserDTO;
import com.skm.authservice.dto.UserResponse;
import com.skm.authservice.entity.User;
import com.skm.authservice.enums.Role;
import com.skm.authservice.exception.ResourceNotFoundException;
import com.skm.authservice.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserResponse> getAllUsers(){
    	 logger.info("Fetching all users");
    	return userRepository.findAll().stream().map(this::userResponse).collect(Collectors.toList());
    	
    }
 private UserResponse userResponse(User user) {
	 UserResponse response=new UserResponse();
	 BeanUtils.copyProperties(user, response);
	 
	 return response;
 }
  private User user(UserResponse response) {
	  User user=new User();
	  BeanUtils.copyProperties(response, user);
	  return user;
  }
 
    public UserResponse getUserById(Long id) {
        logger.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        return userResponse(user);
    }

    public UserResponse updateUser(Long id, UserResponse userResponse) {
        logger.info("Updating user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        BeanUtils.copyProperties(userResponse, user);
        
       
        User updatedUser = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", id);

        return userResponse(updatedUser);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        logger.info("User deleted successfully with ID: {}", id);
    }
}
