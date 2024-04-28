package com.studentDB.StudenDBManagementSystem.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpHeaders;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.studentDB.StudenDBManagementSystem.Model.LoginDetails;
import com.studentDB.StudenDBManagementSystem.Model.Student;
import com.studentDB.StudenDBManagementSystem.repo.LoginController;
import com.studentDB.StudenDBManagementSystem.repo.StudentService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class StudentController {

	@Autowired
	private StudentService service;
	
	@Autowired
	private LoginController logServ;
	
	
	// this is for login and sign up 
	
	@GetMapping("/")
	public String Start(Model model) {
		model.addAttribute("credintial",new LoginDetails());
		return "Login";
	}
	
	@GetMapping("/go")
	public String gotosign(Model model) {
		model.addAttribute("loginDetails", new LoginDetails());
		return "Signup";
	}
	
	@RequestMapping(path="/save",method = RequestMethod.GET)
    public String processAdd(@ModelAttribute LoginDetails obj) {
		logServ.save(obj);
        return "redirect:/";
    }
	
	
	// main thing is starts here whether it goes to admin or student page
	
	@RequestMapping(path="/validate",method = RequestMethod.GET)
	public String validate(@ModelAttribute LoginDetails obj,Model model) {
		if(obj.getStudentno().equals("ADMIN") && obj.getPassword().equals("ADMIN")) {
		return "Admin";
		}
		else {
			List<LoginDetails> loginDB=logServ.findAll();
			for(LoginDetails x:loginDB) {
				if(obj.getStudentno().equals(x.getStudentno()) && obj.getPassword().equals(x.getPassword())) {
					model.addAttribute("id", obj.getStudentno());
					return "student";
				}
			}
			return "errorLogin";
		}
	}
	
	@GetMapping("/homestu/{id}")
	public String homestu(@PathVariable String id,Model model) {
		model.addAttribute("id", id);
		return "student";
	}
	
	@GetMapping("/homeadm")
	public String homeadm() {
		return "Admin";
	}
	
	@GetMapping("/viewadmin")
	public String viewAdmin(Model model) {
		model.addAttribute("students", service.findAll());
		return "viewAdmin";
	}
	
	@GetMapping("/addstudent")
	public String addStudent(Model model) {
		model.addAttribute("data", new Student());
		return "addStudent";
	}
	
	@RequestMapping(path = "/savestudent",method = RequestMethod.GET)
	public String saveStudent(@ModelAttribute Student obj) {
		service.save(obj);
		//System.out.print(obj);
        return "redirect:/homeadm";
    }
	
	
	@GetMapping("/studentview/{id}")
	public String studentview(@PathVariable String id,Model model) {
		Student getdetails;
		try {
		getdetails = service.findById(id).orElseThrow(EntityNotFoundException::new);
		}
		catch(Exception e) {
			getdetails=null;
		}
		System.out.print(getdetails);
		
		model.addAttribute("id",id);
		model.addAttribute("getdata", getdetails);
		return "viewStudent";
	}
	
	@GetMapping("/updatestudent/{id}")
	public String updateStudent(@PathVariable String id,Model model) {
	Student getdetails=service.getReferenceById(id);
	   model.addAttribute("id",id);
	   model.addAttribute("getdata", getdetails);
	   //model.addAttribute("newobj", new Student());
	   return "updateStudent";	
	}
	
	@GetMapping("/update/{id}")
	public String update(@ModelAttribute Student newobj,@PathVariable String id,Model model) {
		model.addAttribute("id", id);
		service.save(newobj);
		Student getdetails;
		try {
		getdetails = service.findById(id).orElseThrow(EntityNotFoundException::new);
		}
		catch(Exception e) {
			getdetails=null;
		}
		System.out.print(getdetails);
		
		model.addAttribute("id",id);
		model.addAttribute("getdata", getdetails);
		return "viewStudent";
	}
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable String id,Model model) {
		service.deleteById(id);
		model.addAttribute("students", service.findAll());
		return "viewAdmin";
	}
	
	@GetMapping("/updatedata/{id}")
	public String updatedata(@PathVariable String id,Model model) {
		Student getdetails=service.getReferenceById(id);
		   model.addAttribute("id",id);
		   model.addAttribute("getdata", getdetails);
		   return "updateAdmin";
	}
	@GetMapping("/updatesave/{id}")
	public String updateSave(@ModelAttribute Student newobj,@PathVariable String id,Model model) {
		model.addAttribute("id", id);
		service.save(newobj);
		model.addAttribute("students", service.findAll());
		return "viewAdmin";
	}
	
	
	@GetMapping("/csv")
	public void CSV(HttpServletResponse response) throws Exception {
		
		//set file name and content type
		String filename = "Student-data.csv";
		
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+filename+"\"");
		
		 // Create a CSV writer
	    try (CSVWriter csvWriter = new CSVWriter(response.getWriter())) {
	        // Write CSV header
	        csvWriter.writeNext(new String[]{"studentId", "firstName", "lastName","email","phoneNo"});

	        // Write all student data into this CSV file
	        for (Student student : service.findAll()) {
	            csvWriter.writeNext(new String[]{student.getStudentId(), student.getFirstName(),student.getLastName() ,student.getEmail(),student.getPhoneNo()});
	        }
	    } catch (Exception e) {
	        // Handle exceptions here
	    }
	}
}
