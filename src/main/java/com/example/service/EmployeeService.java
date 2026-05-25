package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 従業員情報を操作するサービス.
 */
@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * 従業員を登録します（6-3: synchronized によるID採番）.
     */
    public synchronized void insert(Employee employee) {
        Integer maxId = employeeRepository.getMaxId();
        if (maxId == null) {
            employee.setId(1);
        } else {
            employee.setId(maxId + 1);
        }
        employeeRepository.insert(employee);
    }

    public List<Employee> showList() {
        return employeeRepository.findAll();
    }

    public Integer getCount() {
        return employeeRepository.count();
    }

    public List<Employee> showList(int page, int size) {
        int offset = (page - 1) * size;
        return employeeRepository.findAll(size, offset);
    }

    public List<Employee> findByName(String name) {
        if (name == null || name.isEmpty()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByName(name);
    }

    public Employee showDetail(Integer id) {
        return employeeRepository.load(id);
    }

    public void update(Employee employee) {
        employeeRepository.update(employee);
    }
}
