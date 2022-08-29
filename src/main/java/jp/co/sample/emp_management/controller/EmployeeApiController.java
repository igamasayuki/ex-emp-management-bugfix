package jp.co.sample.emp_management.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.service.EmployeeService;

@RestController
@RequestMapping("/searchname")
public class EmployeeApiController {
	@Autowired
	private EmployeeService employeeService;

	@PostMapping("search")
	public List<String> search(String name) {
		List<Employee> employeeList = employeeService.searchEmployees(name);
		List<String> nameList = new ArrayList<>();
		for (Employee employee: employeeList) {
			nameList.add(employee.getName());
		}
		return nameList;
	}
}
