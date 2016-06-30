package com.gmail.liliyayalovchenko.dao;

import com.gmail.liliyayalovchenko.domain.Employee;

import java.util.List;

public interface EmployeeDAO {

    void save(Employee employee);

    Employee getById(Long id);

    List<Employee> findAll();

    Employee findByName(String firstName, String secondName);

    void removeEmployee(String firstName, String secondName);
}
