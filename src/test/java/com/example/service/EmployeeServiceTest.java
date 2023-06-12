package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Employee;
import com.example.repository.EmployeeRepository;

@SpringBootTest
class EmployeeServiceTest {

	@InjectMocks
	private EmployeeService employeeService;

	@Mock
	private EmployeeRepository employeeRepository;

	@SuppressWarnings("null")
	@Test
	void testshowList() {

		List<Employee> actualEmployeeList = null;
		Employee employee1 = (new Employee(7, "渡辺三郎", "e1.png", "男性", new Date(2018 / 10 / 29),
				"saburo.watanabe@sample.com", "666-6666", "福島県福島市1-1-1", "090-6666-6666", 130000,
				"渡辺三郎さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。",
				1));
		Employee employee2 = (new Employee(22, "加藤十子", "e2.png", "女性", new Date(2002 / 8 / 23), "juko.kato@sample.com",
				"111-1111", "静岡県静岡市1-1-1", "070-1111-1111", 220000,
				"加藤十子さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。",
				1));
		actualEmployeeList.add(employee1);
		actualEmployeeList.add(employee2);
		when(employeeRepository.findAll()).thenReturn(actualEmployeeList);

		assertEquals(2, actualEmployeeList.size(), "従業員の人数が一致しませんでした");

		assertEquals("渡辺三郎", actualEmployeeList.get(0).getName(), "入社日順に並んでいません");
		assertEquals("加藤十子", actualEmployeeList.get(1).getName(), "入社日順に並んでいません");
	}

	@Test
	void testshowDetail() {
		when(employeeRepository.load(0))
		.thenReturn(new Employee(7,"渡辺三郎",  "e1.png","男性", new Date(2018/10/29), "saburo.watanabe@sample.com"	,"666-6666", "福島県福島市1-1-1"		, "090-6666-6666", 130000, "渡辺三郎さんは明るく素直な性格です。リーダーシップを発揮します。新卒社員研修の時はグループ開発の時にリーダーを買ってでました。積極性も人間性も抜群です。周りに対する不満も聞いたことがありません。", 1));
		Employee firstEmployee = employeeRepository.load(0);

		assertEquals("渡辺三郎", firstEmployee.getName(), "名前が一致しませんでした");
		assertEquals("男性", firstEmployee.getGender(), "性別が一致しませんでした");
		assertEquals(new Date(2018/10/29), firstEmployee.getHireDate(), "入社日が一致しませんでした");
		assertEquals("saburo.watanabe@sample.com", firstEmployee.getMailAddress(), "メールが一致しませんでした");
		assertEquals("666-6666", firstEmployee.getZipCode(), "郵便番号が一致しませんでした");
		assertEquals("福島県福島市1-1-1", firstEmployee.getAddress(), "住所が一致しませんでした");
		assertEquals("090-6666-6666", firstEmployee.getTelephone(), "電話番号が一致しませんでした");
		assertEquals(130000, firstEmployee.getSalary(), "給料が一致しませんでした");
		assertEquals(1, firstEmployee.getDependentsCount(), "扶養人数が一致しませんでした");
	}

}
