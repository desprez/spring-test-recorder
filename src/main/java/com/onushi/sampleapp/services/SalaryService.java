package com.onushi.sampleapp.services;

import com.onushi.sampleapp.model.Employee;
import org.springframework.stereotype.Service;

@Service
public class SalaryService {
    private final EmployeeRepository employeeRepository;

    public SalaryService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public double computeEmployeeSalary(int employeeId) {
        Employee employee = employeeRepository.loadEmployee(employeeId);
        return employee.getSalaryParam1() + 2 * employee.getSalaryParam2();
    }
}
