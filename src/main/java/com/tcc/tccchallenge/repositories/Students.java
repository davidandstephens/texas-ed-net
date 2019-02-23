package com.tcc.tccchallenge.repositories;

import com.tcc.tccchallenge.models.Student;
import org.springframework.data.repository.CrudRepository;

public interface Students extends CrudRepository<Student, Long> {

}
