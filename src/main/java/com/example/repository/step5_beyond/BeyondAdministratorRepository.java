package com.example.repository.step5_beyond;

import com.example.domain.Administrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 追加課題用 管理者リポジトリ（22-1）.
 */
@Repository("beyondAdministratorRepository")
public class BeyondAdministratorRepository {

    private static final RowMapper<Administrator> ADMINISTRATOR_ROW_MAPPER = (rs, i) -> {
        Administrator administrator = new Administrator();
        administrator.setId(rs.getInt("id"));
        administrator.setName(rs.getString("name"));
        administrator.setMailAddress(rs.getString("mail_address"));
        administrator.setPassword(rs.getString("password"));
        return administrator;
    };

    @Autowired
    private NamedParameterJdbcTemplate template;

    public void insert(Administrator administrator) {
        String sql = "INSERT INTO administrators(name, mail_address, password) VALUES (:name, :mailAddress, :password)";
        template.update(sql, new BeanPropertySqlParameterSource(administrator));
    }

    public Administrator findByMailAddress(String mailAddress) {
        String sql = "SELECT id, name, mail_address, password FROM administrators WHERE mail_address=:mailAddress";
        SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
        List<Administrator> list = template.query(sql, param, ADMINISTRATOR_ROW_MAPPER);
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public void updatePassword(String mailAddress, String encodedPassword) {
        String sql = "UPDATE administrators SET password=:password WHERE mail_address=:mailAddress";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("mailAddress", mailAddress)
                .addValue("password", encodedPassword);
        template.update(sql, param);
    }
}
