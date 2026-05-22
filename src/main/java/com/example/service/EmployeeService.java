package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 従業員情報を操作するサービス.
 *
 * @author igamasayuki
 */
@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
	
    /**
     * 従業員情報を全件取得します.
     *
     * @return 従業員情報一覧
     */
    public List<Employee> showList() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList;
    }

    /**
     * 従業員名で曖昧検索します.
     *
     * @param name 検索文字列
     * @return 検索結果
     */
    public List<Employee> searchByName(String name) {
        if (name == null || name.isBlank()) {
            return employeeRepository.findAll();
        }
        return employeeRepository.findByNameContaining(name);
    }

    /**
     * 従業員情報を取得します.
     *
     * @param id ID
     * @return 従業員情報
     * @throws org.springframework.dao.DataAccessException 検索されない場合は例外が発生します
     */
    public Employee showDetail(Integer id) {
        Employee employee = employeeRepository.load(id);
        return employee;
    }

    /**
     * 従業員情報を更新します.
     *
     * @param employee 更新した従業員情報
     */
    public void update(Employee employee) {
        employeeRepository.update(employee);
    }
}
