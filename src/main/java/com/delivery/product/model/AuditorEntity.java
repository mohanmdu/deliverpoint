package com.delivery.product.model;

import java.util.Date;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditorEntity {

	@CreatedBy
	@Column(name = "CREATED_BY", nullable = false, updatable = false)
	private String createdBy;

	@CreatedDate
	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@LastModifiedBy
	@Column(name = "UPDATED_NAME", nullable = false)
	private String updatedName;

	@LastModifiedDate
	@Column(name = "UPDATED_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedDate;
}
