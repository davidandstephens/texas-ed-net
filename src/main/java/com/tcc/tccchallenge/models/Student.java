package com.tcc.tccchallenge.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="students")
public class Student {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gradeLevel;

    @Column(nullable = false, columnDefinition="date")
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @JsonFormat(pattern="MM/dd/yyyy")
    private Date entryDate;

    @Column(nullable = false)
    private String studentId;

    @ManyToOne @JoinColumn (name = "campus_id")
    private Campus campus;

    @Column(nullable = false)
    private long schoolYear;

    public Student() {
    }

    public Student(String name, String gradeLevel, Date entryDate, String studentId, Campus campus, long schoolYear) {
        this.name = name;
        this.gradeLevel = gradeLevel;
        this.entryDate = entryDate;
        this.studentId = studentId;
        this.campus = campus;
        this.schoolYear = schoolYear;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Campus getCampus() {
        return campus;
    }

    public void setCampus(Campus campus) {
        this.campus = campus;
    }

    public long getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(long schoolYear) {
        this.schoolYear = schoolYear;
    }
}
