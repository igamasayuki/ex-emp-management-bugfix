package com.example.service.step3_advance;

import com.example.domain.Employee;
import com.example.repository.step3_advance.FullEmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 従業員情報を操作するサービス（上級対応版）.
 */
@Service
@Transactional
public class FullEmployeeService {

    @Autowired
    private FullEmployeeRepository employeeRepository;

    /**
     * 従業員情報を登録します.
     * (6-3) 上級対応：synchronized による排他制御とID自前採番
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

    /**
     * 全従業員数を取得します.
     */
    public Integer getCount() {
        return employeeRepository.count();
    }

    /**
     * 指定ページの一覧を取得します.
     */
    public List<Employee> showList(int page, int size) {
        int offset = (page - 1) * size;
        return employeeRepository.findAll(size, offset);
    }

    /**
     * 別解：Enumによる安全な一覧取得.
     */
    public List<Employee> showListWithEnum(int page, com.example.domain.DisplaySize displaySize, com.example.domain.EmployeeSortKey sortKey) {
        int offset = (page - 1) * displaySize.getCount();
        return employeeRepository.findAllWithEnum(sortKey, displaySize.getCount(), offset);
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

    /**
     * 従業員情報を更新します.
     */
    public void update(Employee employee) {
        employeeRepository.update(employee);
    }
}
