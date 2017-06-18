package io.github.k_gregory.insurance.service.repository;

import io.github.k_gregory.insurance.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
