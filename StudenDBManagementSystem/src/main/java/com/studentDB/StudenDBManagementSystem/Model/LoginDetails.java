package com.studentDB.StudenDBManagementSystem.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="loginDetails")
public class LoginDetails {
	
	@Id
	@Column(nullable = false,name="Studentno")
	private String studentno;
	@Column(nullable = false,name="Password")
	private String password;
	public String getStudentno() {
		return studentno;
	}
	public void setStudentno(String studentno) {
		this.studentno = studentno;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginDetails [studentno=" + studentno + ", password=" + password + "]";
	}
	
}
