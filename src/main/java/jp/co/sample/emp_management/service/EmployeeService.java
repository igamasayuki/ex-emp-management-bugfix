package jp.co.sample.emp_management.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.repository.EmployeeRepository;

/**
 * 従業員情報を操作するサービス.
 *
 * @author igamasayuki
 *
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

	/**
	 * 名前で従業員情報をあいまい検索
	 *
	 * @param name 検索情報
	 * @return 検索結果
	 */
	public List<Employee> findByEmployeeName(String name) {
		return employeeRepository.findByEmployeeName(name);
	}

	/**
	 * 従業員新規登録
	 * 
	 * @param employee
	 */
	public synchronized void insert(Employee employee) {
		int maxId = employeeRepository.findMaxId();
		employee.setId(maxId + 1);
		employeeRepository.insert(employee);
	}

	public Boolean findByMailAddress(String mailAddress) {
		return employeeRepository.findByMailAddress(mailAddress);
	}
}
