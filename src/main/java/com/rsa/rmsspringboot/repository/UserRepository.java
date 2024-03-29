package com.rsa.rmsspringboot.repository;

import com.rsa.rmsspringboot.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);

    List<User> findAll();

}
