package com.ecomm.usermgmt.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecomm.usermgmt.domain.Users;


@Repository
public interface UserRepo extends JpaRepository<Users, Object>{
	
	Users findByUsername(String username);
	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query(value = "DELETE u FROM users u INNER JOIN roles r ON u.role_id = r.id WHERE u.username = ?1 AND u.email = ?2 AND r.name = ?3", nativeQuery = true)
	int deleteByUsernameAndEmailAndRole(String username, String email, String role);
	
	Users findByUsernameAndEmail(String username, String email);
	
	@Query(value = "SELECT COUNT(username) FROM users u INNER JOIN roles r ON u.role_id = r.id WHERE u.username = ?1 AND r.name = ?2",nativeQuery = true)
	int existsByUsernameAndRole(String username, String role);
	
	@Query(value = "SELECT COUNT(email) FROM users u INNER JOIN roles r ON u.role_id = r.id WHERE u.email = ?1 AND r.name = ?2",nativeQuery = true)
	int existsByEmailAndRole(String email, String role);

	
	Optional<Users> findByUsernameAndRoleName(String username, String requestedRole);

}
