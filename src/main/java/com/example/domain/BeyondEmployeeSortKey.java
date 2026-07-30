package com.example.domain;

/**
 * 追加課題(11-1) ソートの Enum 別解（3-1 別解 {@link EmployeeSortKey} の発展）.
 * <p>
 * URL の sort / order を安全な ORDER BY 句に変換する。定義外は {@link #HIRE_DATE_DESC}。
 */
public enum BeyondEmployeeSortKey {

    HIRE_DATE_DESC("hire_date DESC", "hireDate", "desc"),
    HIRE_DATE_ASC("hire_date ASC", "hireDate", "asc"),
    SALARY_DESC("salary DESC", "salary", "desc"),
    SALARY_ASC("salary ASC", "salary", "asc"),
    NAME_DESC("name DESC", "name", "desc"),
    NAME_ASC("name ASC", "name", "asc");

    private final String sql;
    private final String urlSort;
    private final String urlOrder;

    BeyondEmployeeSortKey(String sql, String urlSort, String urlOrder) {
        this.sql = sql;
        this.urlSort = urlSort;
        this.urlOrder = urlOrder;
    }

    /**
     * ORDER BY に連結する安全な断片（例: hire_date DESC）.
     */
    public String getSql() {
        return sql;
    }

    /**
     * URL の sort / order から許可された組み合わせを返す.
     */
    public static BeyondEmployeeSortKey fromUrl(String sort, String order) {
        String urlSort = "hireDate";
        if ("salary".equals(sort)) {
            urlSort = "salary";
        } else if ("name".equals(sort)) {
            urlSort = "name";
        } else if ("hireDate".equals(sort)) {
            urlSort = "hireDate";
        }

        String urlOrder = "desc";
        if ("asc".equalsIgnoreCase(order)) {
            urlOrder = "asc";
        }

        for (BeyondEmployeeSortKey key : values()) {
            if (key.urlSort.equals(urlSort) && key.urlOrder.equals(urlOrder)) {
                return key;
            }
        }
        return HIRE_DATE_DESC;
    }
}
