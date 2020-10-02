package com.jd.spring.cloud.contract.get.controller;

import com.jd.spring.cloud.contract.get.model.Employee;
import com.jd.spring.cloud.contract.get.repository.DBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.springframework.http.HttpMethod.GET;

@RestController
public class GetEmployeeController {

	@Autowired
	private DBRepository dbRepository;

	@RequestMapping(value="/employee/{aadharNo}", method = RequestMethod.GET)
	public ResponseEntity<Employee> getEmployee(@PathVariable String aadharNo)
	{

		Random random = new Random();
		if (random.nextBoolean()){
			return new ResponseEntity<Employee>(dbRepository.getEmployee(aadharNo), HttpStatus.OK);
		}

		Employee emp = new Employee();
		emp.setAadharNo(aadharNo);
		emp.setStatus(false);
		emp.setMessage("Employee not found");

		return new ResponseEntity<Employee>(emp, HttpStatus.NOT_FOUND);

	}
}
