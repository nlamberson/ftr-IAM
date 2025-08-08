package com.ftr.iam.repositories;

import com.ftr.iam.entities.UsersInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersInfoRepository extends JpaRepository<UsersInfo, UUID> {
    Optional<UsersInfo> findByUsername(String username);
    boolean existsByUsername(String username);
}
