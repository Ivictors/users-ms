package com.victor.usersms.repository;

import com.victor.usersms.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer> {
    boolean existsByEmail(String email);
    Optional<UserModel> findByUsername(String username);
}
