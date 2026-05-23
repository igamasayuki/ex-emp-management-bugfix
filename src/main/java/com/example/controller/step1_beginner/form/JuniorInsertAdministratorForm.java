package com.example.controller.step1_beginner.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 管理者情報登録時に使用するフォーム（初級 1-2 対応版）.
 * 既存の InsertAdministratorForm を汚さないための比較用クラス。
 */
public class JuniorInsertAdministratorForm {

    @NotBlank(message = "氏名を入力してください")
    @Size(max = 50, message = "氏名は50文字以内で入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が不正です")
    @Size(max = 254, message = "メールアドレスは254文字以内で入力してください")
    private String mailAddress;

    @NotBlank(message = "パスワードを入力してください")
    @Size(min = 8, max = 16, message = "パスワードは8文字以上16文字以内で入力してください")
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
