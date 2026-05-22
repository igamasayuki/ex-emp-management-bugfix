package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

/**
 * EmployeeServiceのテスト.
 */
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void showListはRepositoryの一覧を返す() {
        List<Employee> employees = List.of(new Employee(), new Employee());
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> actual = employeeService.showList();

        // Repositoryをモックにすることで、DBなしでServiceの返却値だけを確認できる。
        assertEquals(2, actual.size());
        assertSame(employees, actual);
    }

    @Test
    void showDetailはRepositoryの詳細を返す() {
        Employee employee = new Employee();
        when(employeeRepository.load(1)).thenReturn(employee);

        Employee actual = employeeService.showDetail(1);

        assertSame(employee, actual);
    }
}
