package com.example.controller;

import com.example.domain.Employee;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * employee関連の処理のweb api用のコントトーラ.
 *
 * @author rui.inoue
 */
@RestController
@RequestMapping("/employee-api")
public class ApiEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 名前による従業員リストを渡す.
     *
     * @param employeeName 検索したい従業員名
     * @return 該当の従業員リスト
     */
    @PostMapping("/nameList")
    public Map<String, List<Employee>> nameList(String employeeName){
        Map<String, List<Employee>> map = new HashMap<>();
        List<Employee> employeeList = employeeService.showListByName(employeeName);
        map.put("employeeList", employeeList);
        return map;
    }
}
