package com.modulith.auctionsystem.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface UserRepository extends JpaRepository<User, String> {

}