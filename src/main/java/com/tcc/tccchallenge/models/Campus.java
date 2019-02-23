package com.tcc.tccchallenge.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="campuses")
public class Campus {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String campusId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campus")
    @JsonBackReference
    private List<Student> students;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column
    @ColumnDefault("true")
    private boolean isActive;

    public Campus() {
    }

    public Campus(String campusId, List<Student> students, String name, String city, boolean isActive) {
        this.campusId = campusId;
        this.students = students;
        this.name = name;
        this.city = city;
        this.isActive = isActive;
    }

    public long getId() {
        return id;
    }

    public String getCampus_id() {
        return campusId;
    }

    public void setCampus_id(String campusId) {
        this.campusId = campusId;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
