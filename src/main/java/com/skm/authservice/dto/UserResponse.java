package com.skm.authservice.dto;

import com.skm.authservice.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
	private Long id;
	    private String firstName;
	    private String lastName;
	    private String phone;
	    private String email;
	    private String address;
	    private boolean status;
	    private Role role;
}
