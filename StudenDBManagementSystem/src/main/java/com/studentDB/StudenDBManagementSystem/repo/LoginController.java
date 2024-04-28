package com.studentDB.StudenDBManagementSystem.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentDB.StudenDBManagementSystem.Model.LoginDetails;
import com.studentDB.StudenDBManagementSystem.Model.Student;

@Repository
public interface LoginController extends JpaRepository<LoginDetails, String>{

	
}
