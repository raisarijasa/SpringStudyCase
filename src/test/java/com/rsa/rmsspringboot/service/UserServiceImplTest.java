package com.rsa.rmsspringboot.service;

import com.rsa.rmsspringboot.entity.Role;
import com.rsa.rmsspringboot.entity.User;
import com.rsa.rmsspringboot.repository.RoleRepository;
import com.rsa.rmsspringboot.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl service;

    @Mock
    private User sampleUser;

    @Mock
    private Set<Role> sampleRoles;

    @Mock
    private Role role;

    private String encryptedPassword = "encryptedPassword";

    @Before
    public void init() {
        role = new Role(1L, "ROLE_ADMIN");
        sampleUser = new User();
        sampleRoles = new HashSet<>();
        sampleRoles.add(role);
        sampleUser.setId(1L);
        sampleUser.setEmail("test@email.com");
        sampleUser.setFullName("Full Name");
        sampleUser.setRoles(sampleRoles);
        sampleUser.setPassword("password");
        sampleUser.setRoleAdmin(true);

    }

    /**
     * findByEmail
     */
    @Test
    public void findByEmail_valid() {
        when(userRepository.findByEmail(sampleUser.getEmail())).thenReturn(sampleUser);
        User serviceResult = service.findByEmail(sampleUser.getEmail());

        Assert.assertEquals(serviceResult, sampleUser);
    }

    /**
     * Save
     */
    @Test
    public void save() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        User result = service.save(sampleUser);

        Assert.assertEquals(result.getEmail(), sampleUser.getEmail());
        Assert.assertEquals(result.getFullName(), sampleUser.getFullName());
        Assert.assertEquals(result.getRoles(), sampleUser.getRoles());
    }

    @Test
    public void update() {
        when(userRepository.save(sampleUser)).thenReturn(sampleUser);
        when(roleRepository.findByRole("ROLE_ADMIN")).thenReturn(role);

        User result = service.save(sampleUser);
        result.setFullName("Test Update");
        User serviceResult = service.save(result);

        Assert.assertEquals(serviceResult.getEmail(), sampleUser.getEmail());
        Assert.assertTrue(serviceResult.getFullName().contains("Test Update"));
        Assert.assertEquals(serviceResult.getRoles(), sampleUser.getRoles());
    }

    /**
     * findAll
     */
    @Test
    public void findAll() {
        List<User> users = new ArrayList<>();
        users.add(sampleUser);
        when(userRepository.findAll()).thenReturn(users);
        List<User> serviceResult = service.findAll();

        Assert.assertEquals(serviceResult, users);
    }

    /**
     * deleteById
     */
    @Test
    public void deleteById() {
        service.deleteById(sampleUser.getId());
        User serviceResult = service.findByEmail(sampleUser.getEmail());

        Assert.assertNull(serviceResult);
    }

    /**
     * loadUserByUsername
     */
    @Test
    public void loadUserByUsername() {
        Assert.assertTrue(true);
    }
}
