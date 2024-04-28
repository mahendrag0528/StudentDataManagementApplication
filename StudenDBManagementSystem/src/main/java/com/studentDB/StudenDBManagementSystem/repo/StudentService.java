package com.studentDB.StudenDBManagementSystem.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentDB.StudenDBManagementSystem.Model.LoginDetails;
import com.studentDB.StudenDBManagementSystem.Model.Student;

@Repository
public interface StudentService extends JpaRepository<Student,String>{

	public Optional<Student> findById(String id); 
}
