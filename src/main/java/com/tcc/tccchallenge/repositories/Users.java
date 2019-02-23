package com.tcc.tccchallenge.repositories;


import com.tcc.tccchallenge.models.User;
import org.springframework.data.repository.CrudRepository;

public interface Users extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
