package com.example.domain;

/**
 * 従業員一覧のソート順を管理する列挙型.
 * (3-1) 別解：型安全な動的ソート
 */
public enum EmployeeSortKey {
    HIRE_DATE_DESC("hire_date DESC"),
    HIRE_DATE_ASC("hire_date ASC"),
    SALARY_DESC("salary DESC"),
    NAME_ASC("name ASC");

    private final String sql;

    EmployeeSortKey(String sql) {
        this.sql = sql;
    }

    public String getSql() { return sql; }
}
