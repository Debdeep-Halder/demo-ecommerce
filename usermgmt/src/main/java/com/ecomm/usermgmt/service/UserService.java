package com.ecomm.usermgmt.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecomm.usermgmt.config.UsernameRoleAuthenticationToken;
import com.ecomm.usermgmt.domain.ApproveRequest;
import com.ecomm.usermgmt.domain.RequestStatus;
import com.ecomm.usermgmt.domain.Roles;
import com.ecomm.usermgmt.domain.Users;
import com.ecomm.usermgmt.repo.ApprovalsRepo;
import com.ecomm.usermgmt.repo.RoleRepo;
import com.ecomm.usermgmt.repo.UserRepo;


@Service
public class UserService {
	
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ApprovalsRepo approvalsRepo;
	@Autowired
	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private RedisService redisService;
	
	
    public UserService(AuthenticationManager authManager, JwtService jwtService) {
        this.authManager = authManager;
        this.jwtService = jwtService;
    }
	
    public Users registerBuyerService(Users user) {
		log.info(user.getRole().getName());
    	
    	if(checkUsernameExists(user.getUsername(),user.getRole().getName()))
			throw new IllegalArgumentException("Username already exists for this role.");
    	if(checkEmailExists(user.getEmail(),user.getRole().getName()))
			throw new IllegalArgumentException("EmailID already exists for a different username for this role.");
    	return saveUser(user);
    }

	public boolean checkUsernameExists(String username, String role) {
		if(userRepo.existsByUsernameAndRole(username, role) > 0)
			return true;
		return false;
	}
    
	private boolean checkEmailExists(String email, String role) {
		if(userRepo.existsByEmailAndRole(email, role)> 0)
			return true;
		return false;
	}

	@Transactional
	public void raiseApproval(Users user) {
		ApproveRequest applicant = new ApproveRequest();
		
		
		if(approvalsRepo.existsByRequestorEmailAndRoleAndStatus(user.getEmail(), user.getRole().getName(), RequestStatus.pending.name()) > 0 ) 
			throw new IllegalArgumentException("There already exists a Request with the same Email, Role and it is in Pending Status.");
		
		applicant.setRequestorUsername(user.getUsername());
		applicant.setRequestorEmail(user.getEmail());
		applicant.setRequestorPassword(user.getPassword());
		applicant.setRole(user.getRole().getName());
		applicant.setStatus(RequestStatus.pending);
		applicant.setRequestTime(new Date(System.currentTimeMillis()));
		
		if(approvalsRepo.save(applicant) == null)
			throw new IllegalArgumentException("The Approval Request is not registered. Please check with the Engineer.");
		
	}

	public Users approveRequest(int requestId) {
		
		try {
			ApproveRequest request = approvalsRepo.findById(requestId)
				    .orElseThrow(() -> new IllegalArgumentException("Approval request not found with ID " + requestId));
			
			String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
			Users adminUser = userRepo.findByUsername(adminUsername);
			
			Users user = new Users();
			user.setUsername(request.getRequestorUsername());
			user.setPassword(request.getRequestorPassword());
			user.setEmail(request.getRequestorEmail());
			Roles role = roleService.getRoleForRoleName(request.getRole().toLowerCase());
			user.setRole(role);
			
			if(checkUsernameExists(user.getUsername(),user.getRole().getName()))
				throw new IllegalArgumentException("Username already exists for this role.");
			if(checkEmailExists(user.getEmail(),user.getRole().getName()))
				throw new IllegalArgumentException("EmailID already exists for a different username for this role.");
			request.setStatus(RequestStatus.approved);
			request.setDecidedAt(new Date(System.currentTimeMillis()));
			request.setDecidedBy(adminUser);
			approvalsRepo.save(request);
			log.info("The requested user is Approved by the admin {}",adminUser);
			return saveUser(user);
		} 
		catch (ObjectOptimisticLockingFailureException e) {
			throw new IllegalStateException("This request was already decided by another admin. Please refresh the list.");
		}
		
	}
	
	@Transactional
	public ApproveRequest rejectRequest(int requestId) {
		try {
			ApproveRequest request = approvalsRepo.findById(requestId)
				    .orElseThrow(() -> new IllegalArgumentException("Approval request not found with ID " + requestId));
			
			String adminUsername = SecurityContextHolder.getContext().getAuthentication().getName();
			Users adminUser = userRepo.findByUsername(adminUsername);
			
			request.setStatus(RequestStatus.rejected);
			request.setDecidedAt(new Date(System.currentTimeMillis()));
			request.setDecidedBy(adminUser);
			log.info("The Approval request for "+request.getRequestorUsername()+" is rejected by the Admin "+adminUser);
			return request;
		} 
		catch (ObjectOptimisticLockingFailureException  e) {
			throw new IllegalStateException("This request was already decided by another admin. Please refresh the list.");
		}
		
	}
	
    private Users saveUser(Users user) {
    	Users savedUser =  userRepo.save(user);
    	savedUser.setPassword("Password is Encrypted");
    	log.info("The User {} is saved successfully.", savedUser.toString());
    	return savedUser;
	}
    
    
	
	@Transactional
	public Users deleteService(String username, String email, String role) {
		
		Users user = userRepo.findByUsernameAndEmail(username, email);
		if(user==null) {
			log.error("User not found with username: " + username + " and email: " + email);
			throw new RuntimeException("User not found with username: " + username + " and email: " + email);
		}
		if(!user.getRole().getName().equals(role)) {
			log.error("User with username: " + username + " doesnt have the right role chosen " + role);
			throw new RuntimeException("User with username: " + username + " doesnt have the right role chosen " + role);
		}
		int count =  userRepo.deleteByUsernameAndEmailAndRole(username, email, role);
		log.info("Deleted Rows count:" + count);
		return user;
	}

	public String verify(Users user) {
		
		Authentication auth;
	    try {
	        auth = authManager.authenticate(
	            new UsernameRoleAuthenticationToken(
	                user.getUsername(),
	                user.getPassword(),
	                user.getRole().getName()
	            )
	        );
	    } catch (AuthenticationException e) {
	        throw new BadCredentialsException("Username/password/role are incorrect.", e);
	    }
	    
//		if(auth.isAuthenticated())
//			log.info("Username "+user.getUsername()+" and password exists in the DB. Checking whether the Role "+user.getRole().getName()+"exists.");
//		else {
//			log.info("Username "+user.getUsername()+" and password doesnt exists in the DB.");
//			throw new BadCredentialsException ("Username and password credentials is incorrect.");
//		}
					
		Set<String> authorities = auth.getAuthorities().stream()
		        .map(GrantedAuthority::getAuthority)
		        .collect(java.util.stream.Collectors.toSet());
		
		if(authorities.contains("ROLE_"+user.getRole().getName().toUpperCase())) {
			log.info("Username "+user.getUsername()+" with the Role "+user.getRole().getName()+" exists in the DB.");
			String Token =  jwtService.generateToken(user.getUsername(), authorities);
			try {
				redisService.setKey(user.getUsername(), user.getRole().getName());
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			log.info("Checkout the JWT Token "+ Token);
			return Token;
		}
		else {
			log.info("Username "+user.getUsername()+" with the Role "+user.getRole().getName()+" doesnt exists in the DB.");
			throw new AccessDeniedException("User does not have requested role");
		}
	}

	public List<Users> getAllUsers() {
		return userRepo.findAll();
	}

	public List<ApproveRequest> getRequests() {
		List<ApproveRequest> list =  approvalsRepo.findAll();
		list.removeIf(obj -> !obj.getStatus().equals(RequestStatus.pending));
		list.forEach(obj -> System.out.print(obj.getRequestorUsername()+"\t"+obj.getRequestorEmail()+"\t"+obj.getRequestTime()));
		return list;
	}

	public void logout(String token) {
		
		String username = jwtService.extractUserName(token);
		String role = jwtService.extractUserRole(token);
		redisService.logout(username, role);
		SecurityContextHolder.clearContext();
	}
	
}

