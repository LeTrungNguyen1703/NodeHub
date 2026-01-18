package com.modulith.auctionsystem.users.domain;

import com.modulith.auctionsystem.users.domain.valueobject.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(Email email);
}
