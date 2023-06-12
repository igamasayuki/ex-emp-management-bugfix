package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;

@SpringBootTest
public class EmployeeServiceTest {

	@InjectMocks
	private EmployeeService employeeService;

	@Mock
	private EmployeeRepository employeeRepository;

	@Test
	void testShowList() {
		List<Employee> employees = new ArrayList<>();
		Employee firstEmployee = new Employee(22, "加藤十子", "e2.png", "女性", new Date(), "juko.kato@sample.com",
				"111-1111", "静岡県静岡市1-1-1", "070-1111-1111", 220000,
				"加藤十子さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。", 1);
		Employee lastEmployee = new Employee(7, "渡辺三郎", "e1.png", "男性", new Date(), "saburo.watanabe@sample.com",
				"666-6666", "福島県福島市1-1-1", "090-6666-6666", 130000,
				"渡辺三郎さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。", 1);

		employees.add(firstEmployee);
		employees.add(lastEmployee);

		when(employeeRepository.findAll()).thenReturn(employees);

		List<Employee> actualEmployees = employeeService.showList();
		List<Employee> expectedEmployees = employees;

		assertEquals(expectedEmployees.get(0).getId(), actualEmployees.get(0).getId());
		assertEquals(expectedEmployees.get(0).getImage(), actualEmployees.get(0).getImage());
		assertEquals(expectedEmployees.get(0).getMailAddress(), actualEmployees.get(0).getMailAddress());
		assertEquals(expectedEmployees.get(0).getAddress(), actualEmployees.get(0).getAddress());
		assertEquals(expectedEmployees.get(0).getSalary(), actualEmployees.get(0).getSalary());
		assertEquals(expectedEmployees.get(1).getName(), actualEmployees.get(1).getName());
		assertEquals(expectedEmployees.get(1).getGender(), actualEmployees.get(1).getGender());
		assertEquals(expectedEmployees.get(1).getZipCode(), actualEmployees.get(1).getZipCode());
		assertEquals(expectedEmployees.get(1).getTelephone(), actualEmployees.get(1).getTelephone());
		assertEquals(expectedEmployees.get(1).getCharacteristics(), actualEmployees.get(1).getCharacteristics());

	}

	@Test
	void testShowDetail() {

		Employee employee13 = new Employee(13, "田中六郎", "e1.png", "男性", new Date(), "rokuro.tanaka@sample.com",
				"222-2222", "東京都新宿区1-1-1", "080-2222-2222", 210000,
				"田中六郎さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。", 0);
		Employee employee18 = new Employee(18, "山本八子", "e2.png", "女性", new Date(), "hachiko.yamamoto@sample.com",
				"777-7777", "福井県福井市1-1-1", "080-7777-7777", 300000,
				"山本八子さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。", 2);

		when(employeeRepository.load(13)).thenReturn(employee13);
		when(employeeRepository.load(18)).thenReturn(employee18);

		Employee actualEmployee13 = employeeService.showDetail(13);
		Employee actualEmployee18 = employeeService.showDetail(18);

		Employee expectedEmployee13 = employee13;
		Employee expectedEmployee18 = employee18;

		assertEquals(expectedEmployee13.getId(), actualEmployee13.getId());
		assertEquals(expectedEmployee13.getImage(), actualEmployee13.getImage());
		assertEquals(expectedEmployee13.getMailAddress(), actualEmployee13.getMailAddress());
		assertEquals(expectedEmployee13.getAddress(), actualEmployee13.getAddress());
		assertEquals(expectedEmployee13.getSalary(), actualEmployee13.getSalary());
		assertEquals(expectedEmployee18.getName(), actualEmployee18.getName());
		assertEquals(expectedEmployee18.getGender(), actualEmployee18.getGender());
		assertEquals(expectedEmployee18.getZipCode(), actualEmployee18.getZipCode());
		assertEquals(expectedEmployee18.getTelephone(), actualEmployee18.getTelephone());
		assertEquals(expectedEmployee18.getCharacteristics(), actualEmployee18.getCharacteristics());
	}

}
