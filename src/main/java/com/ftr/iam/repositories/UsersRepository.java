package com.ftr.iam.repositories;

import com.ftr.iam.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {
    List<Users> findAllByUserId(UUID userId);
    Optional<Users> findByUsername(String username);
    Optional<Users> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
