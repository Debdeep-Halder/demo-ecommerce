package com.ecomm.usermgmt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecomm.usermgmt.domain.ApproveRequest;
import com.ecomm.usermgmt.domain.Roles;
import com.ecomm.usermgmt.domain.Users;
import com.ecomm.usermgmt.dto.LoginRequestDTO;
import com.ecomm.usermgmt.service.RoleService;
import com.ecomm.usermgmt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
//	@Autowired
//	private RedisService redisService;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody LoginRequestDTO registerReq) {
		Users user = new Users();
		user.setUsername(registerReq.getUsername());
	    user.setPassword(registerReq.getPassword());
		user.setEmail(registerReq.getEmail());
		Roles role = roleService.getRoleForRoleName(registerReq.getRequestedRole().toLowerCase());
	    user.setRole(role);
	    
		try {
			if(registerReq.getRequestedRole().equals("BUYER"))
				return ResponseEntity.ok(userService.registerBuyerService(user).toString());
			else {
				userService.raiseApproval(user);
				return ResponseEntity.ok("An Approval request is sent to the Admins.\n"
						+ "Once accepted you can login with your username "+registerReq.getUsername()+" and password.");
			}
		}
		catch(IllegalArgumentException ie) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ie.getMessage());
		}
	}
	
//	@GetMapping("/totalActive")
//	public ResponseEntity<Long> getTotalActive(){
//		return ResponseEntity.ok(redisService.totalActive());
//	}
//	
//	@GetMapping("/totalActive/{role}")
//	public ResponseEntity<Long> getTotalActiveByRole(@PathVariable String role){
//		return ResponseEntity.ok(redisService.totalActiveByRole(role));
//	}
	
//	
//	@GetMapping("/redisKeys/{username}")
//	public ResponseEntity<String> getRedisKeys(@PathVariable String username){
//		return ResponseEntity.ok(redisService.getKey(username));
//	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/requests")
	public ResponseEntity<List<ApproveRequest>> getRequests() {
		List<ApproveRequest> list = userService.getRequests();
		return ResponseEntity.ok(list);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/requests/approve/{id}")
	public ResponseEntity<String> approveRequest(@PathVariable int id){
		
		try {
			Users user = userService.approveRequest(id);
			return ResponseEntity.ok("The User "+user.getUsername()+" with role "+user.getRole().getName()+" is added successfully.");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/requests/reject/{id}")
	public ResponseEntity<String> rejectRequest(@PathVariable int id){
		
		try {
			ApproveRequest req = userService.rejectRequest(id);
			return ResponseEntity.ok("The User "+req.getRequestorUsername()+" with role "+req.getRole()+" is rejected by the Admin.");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/allUsers")
	public ResponseEntity<List<Users>> getAllUsers() {
		
		return ResponseEntity.ok(userService.getAllUsers());
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteUser(@RequestParam String username, @RequestParam String email, @RequestParam String role) {
		Users deletedUser = userService.deleteService(username, email, role);
		return ResponseEntity.ok("User " +deletedUser.toString()+" is deleted successfully.");
		
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request){
		String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer "))
			userService.logout(authHeader.substring(7));
		return ResponseEntity.noContent().build();
		
	}
}
