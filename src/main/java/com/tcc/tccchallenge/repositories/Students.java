package com.tcc.tccchallenge.repositories;

import com.tcc.tccchallenge.models.Campus;
import com.tcc.tccchallenge.models.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface Students extends CrudRepository<Student, Long> {
    Student findOneByStudentId(String studentId);
    List<Student> findAllByNameContaining(String name);
    List<Student> findAllByGradeLevelContaining(String gradeLevel);
    List<Student> findAllByCampus(Campus campus);
    @Query(value = "select * from students where campus_id in (select id from campuses where campuses.campus_id = ?1)", nativeQuery = true)
    List<Student> campusIdSearch(String campusId);
    @Query(value = "select * from students where campus_id in (select id from campuses where campuses.name like CONCAT('%',?1,'%'))", nativeQuery = true)
    List<Student> campusNameSearch(String campusName);
}
