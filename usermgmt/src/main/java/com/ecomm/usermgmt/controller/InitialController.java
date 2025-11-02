package com.ecomm.usermgmt.controller;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.usermgmt.domain.Roles;
import com.ecomm.usermgmt.domain.Users;
import com.ecomm.usermgmt.dto.LoginRequestDTO;
import com.ecomm.usermgmt.service.RoleService;
import com.ecomm.usermgmt.service.UserService;


@RestController
public class InitialController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginReq) {
		
		Users user = new Users();
		user.setUsername(loginReq.getUsername());
	    user.setPassword(loginReq.getPassword());
	    // look up the Roles entity by name
	    Roles role = roleService.getRoleForRoleName(loginReq.getRequestedRole().toLowerCase());
	    user.setRole(role);
	    try{
	    	String token = userService.verify(user);
	    	return ResponseEntity.ok(token);
	    }
	    catch(BadCredentialsException be){
	    	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
	    }
	    catch(AccessDeniedException ae) {
	    	return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User lacks requested role");
	    }
	    catch(Exception e) {
	    	log.error(""+e);
	    	return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	    }
	}
}
