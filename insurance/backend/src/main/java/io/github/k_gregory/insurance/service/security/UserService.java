package io.github.k_gregory.insurance.service.security;

import io.github.k_gregory.insurance.entity.User;

import java.util.Collection;

public interface UserService {
    User register(String login, String password, Collection<String> roles);
}
