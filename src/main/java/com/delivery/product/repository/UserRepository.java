package com.delivery.product.repository;


import com.delivery.product.enumeration.UserStatus;
import com.delivery.product.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE (u.emailId = ?1 OR u.mobileNumber = ?1) AND u.status = ?2")
    Optional<UserEntity> findByMobileNumber(String email, UserStatus userStatus);

    @Query("SELECT u FROM UserEntity u WHERE u.userId <> ?1 AND (u.emailId = ?2 OR u.mobileNumber = ?3)")
    Optional<UserEntity> findByEmailMobileUserId(Long userId, String mobileNumber, String emailId);
}
