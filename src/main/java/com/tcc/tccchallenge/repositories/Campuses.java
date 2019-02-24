package com.tcc.tccchallenge.repositories;

import com.tcc.tccchallenge.models.Campus;
import com.tcc.tccchallenge.models.Student;
import org.springframework.data.repository.CrudRepository;

public interface Campuses extends CrudRepository<Campus, Long> {
    Campus findOneByCampusId(String campusId);
}
