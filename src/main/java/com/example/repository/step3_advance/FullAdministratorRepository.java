package com.example.repository.step3_advance;

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
 * administratorsテーブルを操作するリポジトリ（上級対応版）.
 */
@Repository
public class FullAdministratorRepository {

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
        SqlParameterSource param = new BeanPropertySqlParameterSource(administrator);
        String sql = "insert into administrators(name,mail_address,password)values(:name,:mailAddress,:password);";
        template.update(sql, param);
    }

    /**
     * メールアドレスから管理者情報を取得します.
     * (5-3) 上級対応：パスワード認証はServiceで行うため、ここではメールアドレスのみで検索
     */
    public Administrator findByMailAddress(String mailAddress) {
        String sql = "select id,name,mail_address,password from administrators where mail_address=:mailAddress";
        SqlParameterSource param = new MapSqlParameterSource().addValue("mailAddress", mailAddress);
        List<Administrator> administratorList = template.query(sql, param, ADMINISTRATOR_ROW_MAPPER);
        if (administratorList.size() == 0) {
            return null;
        }
        return administratorList.get(0);
    }
}
