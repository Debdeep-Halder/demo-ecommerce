package com.ecomm.usermgmt.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "approvals")
public class ApproveRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable=false)
	private String requestorUsername;
	@Column(nullable=false)
	private String requestorPassword;
	@Column(nullable=false)
	private String requestorEmail;
	@Column(nullable=false)
	private String role;
	@Enumerated(EnumType.STRING) @Column(nullable=false)
	private RequestStatus status;
	@Column(nullable=false)
	private Date requestTime;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="decided_by", referencedColumnName = "id", nullable = true)
	private Users decidedBy;
	private Date decidedAt;
	
	@Version
	int version;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequestorUsername() {
		return requestorUsername;
	}

	public void setRequestorUsername(String requestorUsername) {
		this.requestorUsername = requestorUsername;
	}

	public String getRequestorPassword() {
		return requestorPassword;
	}

	public void setRequestorPassword(String requestorPassword) {
		this.requestorPassword = requestorPassword;
	}

	public String getRequestorEmail() {
		return requestorEmail;
	}

	public void setRequestorEmail(String requestorEmail) {
		this.requestorEmail = requestorEmail;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public Users getDecidedBy() {
		return decidedBy;
	}

	public void setDecidedBy(Users decidedBy) {
		this.decidedBy = decidedBy;
	}

	public Date getDecidedAt() {
		return decidedAt;
	}

	public void setDecidedAt(Date decidedAt) {
		this.decidedAt = decidedAt;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "ApproveRequest [id=" + id + ", requestorUsername=" + requestorUsername + ", requestorPassword="
				+ requestorPassword + ", requestorEmail=" + requestorEmail + ", role=" + role + ", status=" + status
				+ ", requestTime=" + requestTime + ", decidedBy=" + decidedBy.toString() + ", decidedAt=" + decidedAt
				+ ", version=" + version + "]";
	}
	
}
