package com.ecomm.usermgmt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecomm.usermgmt.domain.Roles;
import com.ecomm.usermgmt.repo.RoleRepo;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepo roleRepo;

	public Roles getRoleForRoleName(String role) {
		if(roleRepo.findByName(role) != null) {
			System.out.print(roleRepo.findByName(role).toString());
			return roleRepo.findByName(role);
		}
		return null;
	}
	
	
}
