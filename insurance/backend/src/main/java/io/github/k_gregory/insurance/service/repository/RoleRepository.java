package io.github.k_gregory.insurance.service.repository;

import io.github.k_gregory.insurance.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
