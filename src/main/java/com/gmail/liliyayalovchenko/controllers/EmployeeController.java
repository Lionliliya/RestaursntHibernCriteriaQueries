package com.gmail.liliyayalovchenko.controllers;

import com.gmail.liliyayalovchenko.dao.EmployeeDAO;
import com.gmail.liliyayalovchenko.domain.Employee;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class EmployeeController {

    private EmployeeDAO employeeDAO;
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Transactional
    public void createEmployee(Employee employee) {
        LOGGER.info("Start persisting employee.");
        try {
            employeeDAO.save(employee);
            LOGGER.info("Employee is persist");
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get employee from database " + ex);
        }
    }

    @Transactional
    public List<Employee> getAllEmployees() {
        LOGGER.info("Try to get all employees");
        try {
            List<Employee> all = employeeDAO.findAll();
            LOGGER.info("All employees are got.");
            return all;
        } catch (HibernateException ex) {
            LOGGER.error("Cannot get all employees from database " + ex);
        }
        return null;
    }

    @Transactional
    public void printAllEmployeesName() {
        LOGGER.info("Try to get all employees");
        try {
            List<Employee> all = employeeDAO.findAll();
            LOGGER.info("All employees are got.");
            for (Employee employee : all) {
                System.out.println(employee.getSecondName() + " " + employee.getFirstName());
            }

        } catch (HibernateException ex) {
            LOGGER.error("Cannot get all employees from database " + ex);
        }

    }

    @Transactional
    public void printAllEmployee() {
        employeeDAO.findAll().forEach(System.out::println);
    }

    @Transactional
    public Employee getEmployeeByName(String firstName, String secondName) {
        Employee byName = null;
        try {
            byName = employeeDAO.findByName(firstName, secondName);

        } catch (HibernateException ex) {
            LOGGER.error("Cannot get employee by name from database " + ex);

        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input. " + ex);

        }

        return byName;
    }

    @Transactional
    public void printEmployeeByName(String firstName, String secondName) {
        Employee byName = null;
        try {
            byName = employeeDAO.findByName(firstName, secondName);

        } catch (HibernateException ex) {
            LOGGER.error("Cannot get employee by name from database " + ex);

        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input. " + ex);

        }
        System.out.println(byName);
    }



    @Transactional
    public void deleteEmployee(String firstName, String secondName) {
        try {
            employeeDAO.removeEmployee(firstName, secondName);
        } catch (HibernateException ex) {
            LOGGER.error("Cannot remove employee from database " + ex);
        } catch (RuntimeException ex) {
            LOGGER.error("Wrong input! " + ex);
        }
    }

    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }
}
