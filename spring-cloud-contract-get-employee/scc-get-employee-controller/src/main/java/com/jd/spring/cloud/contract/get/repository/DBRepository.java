package com.jd.spring.cloud.contract.get.repository;

import com.jd.spring.cloud.contract.get.model.Employee;

import java.util.Random;

public class DBRepository {

    public Employee getEmployee(String aadharNo)
    {
        Employee emp = new Employee();

        String id= String.valueOf(Math.abs(new Random().nextInt()));
        emp.setId(id);
        emp.setAadharNo(aadharNo);
        emp.setFirstName("FirstName");
        emp.setLastName("LastName");
        emp.setStatus(true);
        emp.setMessage("Employee already present");

        return emp;
    }
}