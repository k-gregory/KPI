package io.github.k_gregory.insurance.service.security.impl;

import io.github.k_gregory.insurance.entity.Role;
import io.github.k_gregory.insurance.entity.User;
import io.github.k_gregory.insurance.exception.UserExistsException;
import io.github.k_gregory.insurance.service.security.UserService;
import io.github.k_gregory.insurance.service.repository.RoleRepository;
import io.github.k_gregory.insurance.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(
            isolation = Isolation.SERIALIZABLE
    )
    public User register(String login, String password, Collection<String> roleNames) {
        User existentUser = userRepository.findOne(login);
        if (existentUser != null) {
            throw new UserExistsException(login);
        }

        User user = new User();
        String passwordHash = passwordEncoder.encode(password);
        List<Role> roles = roleRepository.findAll(roleNames);
        if (roles.size() != roleNames.size())
            throw new IllegalArgumentException("roleNames");

        user.setLogin(login);
        user.setPasswordHash(passwordHash);
        user.setRoles(new HashSet<>(roles));

        return userRepository.save(user);
    }
}
