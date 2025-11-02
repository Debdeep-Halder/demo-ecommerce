package com.ecomm.usermgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecomm.usermgmt.domain.Roles;

@Repository
public interface RoleRepo extends JpaRepository<Roles, Object>{
	
	Roles findByName(String roleName);
}
