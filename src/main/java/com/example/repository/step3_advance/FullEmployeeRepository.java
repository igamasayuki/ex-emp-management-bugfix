package com.example.repository.step3_advance;

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
 * 従業員情報を操作するリポジトリ（上級対応版）.
 */
@Repository
public class FullEmployeeRepository {

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

    /**
     * 従業員IDの最大値を取得します.
     */
    public Integer getMaxId() {
        String sql = "SELECT MAX(id) FROM employees";
        SqlParameterSource param = new MapSqlParameterSource();
        return template.queryForObject(sql, param, Integer.class);
    }

    /**
     * 従業員情報を登録します.
     */
    public void insert(Employee employee) {
        String sql = "INSERT INTO employees(id, name, image, gender, hire_date, mail_address, zip_code, address, telephone, salary, characteristics, dependents_count) " +
                     "VALUES(:id, :name, :image, :gender, :hireDate, :mailAddress, :zipCode, :address, :telephone, :salary, :characteristics, :dependentsCount)";
        SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        template.update(sql, param);
    }

    public List<Employee> findAll() {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date DESC";
        return template.query(sql, EMPLOYEE_ROW_MAPPER);
    }

    /**
     * 従業員数を取得します.
     */
    public Integer count() {
        String sql = "SELECT COUNT(*) FROM employees";
        return template.queryForObject(sql, new MapSqlParameterSource(), Integer.class);
    }

    /**
     * 指定した範囲の従業員一覧を取得します.
     */
    public List<Employee> findAll(int limit, int offset) {
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY hire_date DESC LIMIT :limit OFFSET :offset";
        SqlParameterSource param = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }

    /**
     * 別解：Enumによる型安全なソートとページング指定.
     */
    public List<Employee> findAllWithEnum(com.example.domain.EmployeeSortKey sortKey, int limit, int offset) {
        // sortKey.getSql() を使うことで、外部からのSQLインジェクションを物理的に遮断
        String sql = "SELECT id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count FROM employees ORDER BY " + sortKey.getSql() + " LIMIT :limit OFFSET :offset";
        SqlParameterSource param = new MapSqlParameterSource().addValue("limit", limit).addValue("offset", offset);
        return template.query(sql, param, EMPLOYEE_ROW_MAPPER);
    }

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

    /**
     * 従業員情報を更新します.
     */
    public void update(Employee employee) {
        String updateSql = "UPDATE employees SET dependents_count=:dependentsCount WHERE id=:id";
        SqlParameterSource param = new BeanPropertySqlParameterSource(employee);
        template.update(updateSql, param);
    }
}
