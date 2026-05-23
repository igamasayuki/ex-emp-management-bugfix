package com.example.domain;

/**
 * システム全体で使用するメッセージを管理する列挙型.
 * (6-1) 別解：プロのメッセージ管理
 */
public enum SystemMessage {
    REGISTER_SUCCESS("infoMessage", "管理者の登録が完了しました。ログインしてください。"),
    MAIL_DUPLICATE("errorMessage", "そのメールアドレスはすでに登録されています"),
    PASSWORD_MISMATCH("errorMessage", "パスワードが一致しません"),
    LOGIN_FAILED("errorMessage", "メールアドレスまたはパスワードが不正です。"),
    SEARCH_NOT_FOUND("message", "１件もありませんでした");

    private final String key;
    private final String text;

    SystemMessage(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public String getKey() { return key; }
    public String getText() { return text; }
}
