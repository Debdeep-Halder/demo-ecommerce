package com.ecomm.usermgmt.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecomm.usermgmt.domain.ApproveRequest;
import com.ecomm.usermgmt.domain.RequestStatus;

public interface ApprovalsRepo extends JpaRepository<ApproveRequest, Object>{
	
	ApproveRequest findByRequestorUsername(String username);
	
	@Query(value = "SELECT COUNT(requestor_email) FROM approvals WHERE requestor_email = ?1 AND role = ?2 AND status = ?3",nativeQuery = true)
	int existsByRequestorEmailAndRoleAndStatus(String email, String role, String status);

	@Query(value = "SELECT * FROM approvals WHERE requestor_email = ?1",nativeQuery = true)
	String findByEmail(String email);

}
