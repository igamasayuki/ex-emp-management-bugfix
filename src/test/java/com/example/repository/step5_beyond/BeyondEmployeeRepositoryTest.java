package com.example.repository.step5_beyond;

import com.example.domain.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 追加課題(17-1) EmployeeRepository 実 DB テスト（step5）.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BeyondEmployeeRepositoryTest {

    @Autowired
    @Qualifier("beyondEmployeeRepository")
    private BeyondEmployeeRepository employeeRepository;

    @Test
    void insertLoadUpdateDelete() {
        Employee employee = sampleEmployee("step5-test-" + System.nanoTime());
        Integer maxId = employeeRepository.getMaxId();
        if (maxId == null) {
            employee.setId(1);
        } else {
            employee.setId(maxId + 1);
        }
        employeeRepository.insert(employee);

        Employee loaded = employeeRepository.load(employee.getId());
        assertThat(loaded.getName()).isEqualTo(employee.getName());

        loaded.setAddress("更新後住所");
        loaded.setDependentsCount(2);
        employeeRepository.update(loaded);

        Employee updated = employeeRepository.load(employee.getId());
        assertThat(updated.getAddress()).isEqualTo("更新後住所");
        assertThat(updated.getDependentsCount()).isEqualTo(2);

        employeeRepository.deleteById(employee.getId());
        List<Employee> page = employeeRepository.findPage(null, null, null, "hire_date", "desc", 10, 0);
        assertThat(page.stream().noneMatch(e -> e.getId().equals(employee.getId()))).isTrue();
    }

    @Test
    void searchAndCount() {
        int count = employeeRepository.count("山田", null, null);
        assertThat(count).isGreaterThanOrEqualTo(0);
        List<Employee> list = employeeRepository.findPage("山田", null, null, "name", "asc", 10, 0);
        assertThat(list).isNotNull();
    }

    private Employee sampleEmployee(String name) {
        Employee e = new Employee();
        e.setName(name);
        e.setImage("no_image.png");
        e.setGender("男性");
        e.setHireDate(Date.valueOf("2020-01-01"));
        e.setMailAddress(name + "@example.com");
        e.setZipCode("1000001");
        e.setAddress("東京都千代田区");
        e.setTelephone("03-0000-0000");
        e.setSalary(300000);
        e.setCharacteristics("テスト");
        e.setDependentsCount(0);
        return e;
    }
}
