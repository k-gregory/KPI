package io.github.k_gregory.insurance.service.security.impl;

import io.github.k_gregory.insurance.entity.User;
import io.github.k_gregory.insurance.service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findOne(username);
        if (user == null) throw new UsernameNotFoundException("Can't find " + username);

        List<SimpleGrantedAuthority> authorityList = user
                .getRoles()
                .stream().map(r -> new SimpleGrantedAuthority(r.getRoleName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                username, user.getPasswordHash(), authorityList
        );
    }
}
