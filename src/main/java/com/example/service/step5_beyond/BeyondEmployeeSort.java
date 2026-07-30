package com.example.service.step5_beyond;

/**
 * 追加課題(11-1) ソート列のホワイトリスト（if 文の別解）.
 * <p>
 * 講師用完成形の一覧は {@link com.example.domain.BeyondEmployeeSortKey}（Enum 別解）を使用。
 */
public final class BeyondEmployeeSort {

    private BeyondEmployeeSort() {
    }

    public static String toOrderColumn(String sort) {
        if (sort == null) {
            return "hire_date";
        }
        if ("salary".equals(sort)) {
            return "salary";
        }
        if ("name".equals(sort)) {
            return "name";
        }
        return "hire_date";
    }

    public static String toOrderDir(String order) {
        if ("asc".equalsIgnoreCase(order)) {
            return "asc";
        }
        return "desc";
    }

    public static String toggleOrder(String sort, String currentSort, String currentOrder) {
        if (sort != null && sort.equals(currentSort) && "asc".equalsIgnoreCase(currentOrder)) {
            return "desc";
        }
        return "asc";
    }
}
