package com.gmail.liliyayalovchenko.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EMPLOYEE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Employee {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    private int id;

    @Column(name = "second_name")
    private String secondName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "empl_date")
    private Date emplDate;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private Position position;

    @Column(name = "salary")
    private int salary;

    public Employee(int id, String secondName, String firstName, Date emplDate, String phone, Position position, int salary) {
        this.id = id;
        this.secondName = secondName;
        this.firstName = firstName;
        this.emplDate = emplDate;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
    }

    public Employee(String secondName, String firstName, Date emplDate, String phone, Position position, int salary) {
        this.secondName = secondName;
        this.firstName = firstName;
        this.emplDate = emplDate;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
    }

    public Employee(String secondName, String firstName, String phone, Position position, int salary) {
        this.secondName = secondName;
        this.firstName = firstName;
        this.phone = phone;
        this.position = position;
        this.salary = salary;
    }

    public Employee() {}

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmplDate(Date emplDate) {
        this.emplDate = emplDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstName() {
        return firstName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", secondName='" + secondName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", emplDate=" + emplDate +
                ", phone='" + phone + '\'' +
                ", position=" + position +
                ", salary=" + salary +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee)) return false;

        Employee employee = (Employee) o;

        if (salary != employee.salary) return false;
        if (emplDate != null ? !emplDate.equals(employee.emplDate) : employee.emplDate != null) return false;
        if (firstName != null ? !firstName.equals(employee.firstName) : employee.firstName != null) return false;
        if (phone != null ? !phone.equals(employee.phone) : employee.phone != null) return false;
        if (position != employee.position) return false;
        if (secondName != null ? !secondName.equals(employee.secondName) : employee.secondName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = secondName != null ? secondName.hashCode() : 0;
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (emplDate != null ? emplDate.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + salary;
        return result;
    }
}
