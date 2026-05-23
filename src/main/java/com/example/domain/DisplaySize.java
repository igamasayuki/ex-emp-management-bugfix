package com.example.domain;

/**
 * 1ページあたりの表示件数を管理する列挙型.
 * (6-5) 別解：不正な件数指定を許さない安全なページング
 */
public enum DisplaySize {
    SMALL(10),
    MEDIUM(20),
    LARGE(50);

    private final int count;

    DisplaySize(int count) {
        this.count = count;
    }

    public int getCount() { return count; }
}
