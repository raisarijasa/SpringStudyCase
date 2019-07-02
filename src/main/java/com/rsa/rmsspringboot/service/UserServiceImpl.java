package com.rsa.rmsspringboot.service;

import com.rsa.rmsspringboot.entity.Role;
import com.rsa.rmsspringboot.entity.User;
import com.rsa.rmsspringboot.repository.RoleRepository;
import com.rsa.rmsspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private RoleRepository roleRespository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRespository = roleRepository;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        List<Role> roles = new ArrayList<>();
        if (user.getRoleAdmin() != null && user.getRoleAdmin()) {
            Role role = roleRespository.findByRole("ROLE_ADMIN");
            if (role != null) {
                roles.add(role);
            }
        }
        if (user.getRoleUser() != null && user.getRoleUser()) {
            Role role = roleRespository.findByRole("ROLE_USER");
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(new HashSet<Role>(roles));
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void resetPassword(String email) {
        User user = findByEmail(email);
        if (user != null) {
            user.setPassword(passwordEncoder.encode("password"));
        }
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        UserDetails dd = new UserDetailsImpl(user);
        return new UserDetailsImpl(user);
    }

}