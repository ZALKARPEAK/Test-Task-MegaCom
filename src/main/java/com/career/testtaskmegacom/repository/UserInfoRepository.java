package com.career.testtaskmegacom.repository;

import com.career.testtaskmegacom.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    Boolean existsByEmail(String email);
    Optional<UserInfo> getUserAccountByEmail(String email);
    Optional<UserInfo> getByUserInfoUserName(String email);
}