package com.example.repository.step5_beyond;

import com.example.domain.BeyondEmployeeSortKey;
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
 * 追加課題用 従業員リポジトリ（8-1, 9-1, 11-1, 29-1）.
 */
@Repository("beyondEmployeeRepository")
public class BeyondEmployeeRepository {

    private static final String SELECT_COLUMNS =
            "id,name,image,gender,hire_date,mail_address,zip_code,address,telephone,salary,characteristics,dependents_count";

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
        template.update(sql, new BeanPropertySqlParameterSource(employee));
    }

    public List<Employee> findAll() {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM employees ORDER BY hire_date DESC";
        return template.query(sql, EMPLOYEE_ROW_MAPPER);
    }

    public Integer count(String name, String mail, String telephone) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM employees WHERE 1=1");
        MapSqlParameterSource param = new MapSqlParameterSource();
        appendSearchConditions(sql, param, name, mail, telephone);
        return template.queryForObject(sql.toString(), param, Integer.class);
    }

    /**
     * if による列名・向き指定（研修生の別解・テスト用）.
     */
    public List<Employee> findPage(String name, String mail, String telephone,
            String orderColumn, String orderDir, int limit, int offset) {
        StringBuilder sql = new StringBuilder("SELECT ").append(SELECT_COLUMNS).append(" FROM employees WHERE 1=1");
        MapSqlParameterSource param = new MapSqlParameterSource();
        appendSearchConditions(sql, param, name, mail, telephone);
        sql.append(" ORDER BY ").append(orderColumn).append(" ").append(orderDir);
        sql.append(" LIMIT :limit OFFSET :offset");
        param.addValue("limit", limit).addValue("offset", offset);
        return template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);
    }

    /**
     * Enum 別解（11-1 講師用完成形のデフォルト）.
     */
    public List<Employee> findPage(String name, String mail, String telephone,
            BeyondEmployeeSortKey sortKey, int limit, int offset) {
        StringBuilder sql = new StringBuilder("SELECT ").append(SELECT_COLUMNS).append(" FROM employees WHERE 1=1");
        MapSqlParameterSource param = new MapSqlParameterSource();
        appendSearchConditions(sql, param, name, mail, telephone);
        sql.append(" ORDER BY ").append(sortKey.getSql());
        sql.append(" LIMIT :limit OFFSET :offset");
        param.addValue("limit", limit).addValue("offset", offset);
        return template.query(sql.toString(), param, EMPLOYEE_ROW_MAPPER);
    }

    public Employee load(Integer id) {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM employees WHERE id=:id";
        SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
        return template.queryForObject(sql, param, EMPLOYEE_ROW_MAPPER);
    }

    public void update(Employee employee) {
        String sql = "UPDATE employees SET name=:name, mail_address=:mailAddress, telephone=:telephone, "
                + "salary=:salary, address=:address, dependents_count=:dependentsCount WHERE id=:id";
        template.update(sql, new BeanPropertySqlParameterSource(employee));
    }

    public void deleteById(Integer id) {
        String sql = "DELETE FROM employees WHERE id=:id";
        template.update(sql, new MapSqlParameterSource().addValue("id", id));
    }

    private void appendSearchConditions(StringBuilder sql, MapSqlParameterSource param,
            String name, String mail, String telephone) {
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE :name");
            param.addValue("name", "%" + name + "%");
        }
        if (mail != null && !mail.isEmpty()) {
            sql.append(" AND mail_address LIKE :mail");
            param.addValue("mail", "%" + mail + "%");
        }
        if (telephone != null && !telephone.isEmpty()) {
            sql.append(" AND telephone LIKE :telephone");
            param.addValue("telephone", "%" + telephone + "%");
        }
    }
}
