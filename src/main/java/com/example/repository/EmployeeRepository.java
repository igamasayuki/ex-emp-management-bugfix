package com.example.repository;

import com.example.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * employeesテーブルを操作するリポジトリ.
 */
@Repository
public class EmployeeRepository {

    private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (rs, i) -> {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setName(rs.getString("name"));
        employee.setImage(rs.getString("image"));
        employee.setGender(rs.getString("gender"));
        employee.setHireDate(rs.getDate("hire_date"));
        employee.setMailAddress(rs.getString("mail_address"));
        employee.setZipCode(rs.getString("zip_code"));
        employee.setAddress(rs.getString("address"));
        employee.setTelephone(rs.getString("telephone"));
        employee.setSalary(rs.getInt("salary"));
        employee.setCharacteristics(rs.getString("characteristics"));
        employee.setDependentsCount(rs.getInt("dependents_count"));
        return employee;
    };

    @Autowired
    private NamedParameterJdbcTemplate template;

    public Integer getMaxId() {
        String sql = "SELECT MAX(id) FROM employees";
        return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    public void insert(Employee employee) {
        String sql = "INSERT INTO employees(id, name, image, gender, hire_date, mail_address, zip_code, address, telephone, salary, characteristics, dependents_count) "
                + "VALUES(:id, :name, :image, :gender, :hireDate, :mailAddress, :zipCode, :address, :telephone, :salary, :characteristics, :dependentsCount)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        template.update(sql, param);
    }

    /**
     * 従業員一覧を入社日降順で取得します（3-1）.
     */
    public List<Employee> findAll() {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date DESC";
        return template.query(sql, EMPLOYEE_ROW_MAPPER);
    }

    public Integer count() {
        String sql = "SELECT COUNT(*) FROM employees";
        return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    public List<Employee> findAll(int limit, int offset) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date DESC LIMIT :limit OFFSET :offset";
        SqlParameterSource param = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }

    /**
     * 名前で曖昧検索します（6-2）.
     */
    public List<Employee> findByName(String name) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE name LIKE :name ORDER BY hire_date DESC";
        SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }

    public Employee load(Integer id) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees WHERE id=:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);
    }

    public void update(Employee employee) {
        String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
        SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        template.update(updateSql, param);
    }
}
