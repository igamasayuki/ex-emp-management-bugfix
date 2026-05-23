package com.example.service.step2_intermediate;

import com.example.domain.Employee;
import com.example.repository.step2_intermediate.MiddleEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 従業員情報を操作するサービス（中級対応版）.
 */
@Service
@Transactional
public class MiddleEmployeeService {

    @Autowired
    private MiddleEmployeeRepository employeeRepository;
	
    public List<Employee> showList() {
        return employeeRepository.findAll();
    }

    /**
     * 名前で曖昧検索します.
     * (6-2) 中級対応
     */
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
