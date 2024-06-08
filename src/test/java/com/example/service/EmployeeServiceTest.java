package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * EmployeeServiceのテストクラス.
 *
 * @author rui.inoue
 */
@SpringBootTest
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void testShowList(){
        when(employeeRepository.findAll())
                .thenReturn(List.of(
                        new Employee(1, "name1", "image1", "gender1", Date.valueOf("1000-01-01"), "mail1", "zipcode1", "address1", "tel1", 1, "char1", 1)
                        ,new Employee(2, "name2", "image2", "gender2", Date.valueOf("1000-01-02"), "mail2", "zipcode2", "address2", "tel2", 2, "char2", 2)
                ));

        List<Employee> employeeList = employeeService.showList();

        verify(employeeRepository).findAll();
    }

    @Test
    void testShowDetail(){
        when(employeeRepository.load(1))
                .thenReturn(new Employee(1, "name1", "image1", "gender1", Date.valueOf("1000-01-01"), "mail1", "zipcode1", "address1", "tel1", 1, "char1", 1));

        Employee employee = employeeService.showDetail(1);

        verify(employeeRepository).load(1);
    }
}