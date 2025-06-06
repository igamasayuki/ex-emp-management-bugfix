package com.example.service;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmployeeServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void showList() {
        List<Employee> dummyList = new ArrayList<>();
        Employee employee1 = new Employee(
                1,
                "田中 太郎",
                "e1.png",
                "男性",
                Date.from((LocalDate.of(2015, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "tanaka@example.com",
                "123-4567",
                "東京都千代田区1-1-1",
                "03-1234-5678",
                4000000,
                "責任感が強い",
                2
        );

        Employee employee2 = new Employee(
                2,
                "鈴木 花子",
                "e2.png",
                "女性",
                Date.from((LocalDate.of(2015, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "suzuki@example.com",
                "234-5678",
                "大阪府大阪市2-2-2",
                "06-2345-6789",
                3800000,
                "コミュニケーション能力が高い",
                1
        );
        Employee employee3 = new Employee(
                3,
                "佐藤 次郎",
                "e1.png",
                "男性",
                Date.from((LocalDate.of(2015, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "sato@example.com",
                "345-6789",
                "北海道札幌市3-3-3",
                "011-3456-7890",
                3500000,
                "分析力に優れる",
                0
        );

        dummyList.add(employee1);
        dummyList.add(employee2);
        dummyList.add(employee3);
        when(employeeRepository.findAll()).thenReturn(dummyList);

        List<Employee> result = employeeService.showList();

        assertEquals(dummyList, result);

        verify(employeeRepository, times(1)).findAll();

    }

    @Test
    void showDetail() {
        Employee employeeFromRepository = new Employee(
                1,
                "田中 太郎",
                "e1.png",
                "男性",
                Date.from((LocalDate.of(2015, 1, 1)).atStartOfDay(ZoneId.systemDefault()).toInstant()),
                "tanaka@example.com",
                "123-4567",
                "東京都千代田区1-1-1",
                "03-1234-5678",
                4000000,
                "責任感が強い",
                2
        );
        when(employeeRepository.load(1)).thenReturn(employeeFromRepository);
        Employee employeeFromService = employeeService.showDetail(1);
        assertEquals(employeeFromService, employeeFromRepository);
        verify(employeeRepository, times(1)).load(1);
    }
}