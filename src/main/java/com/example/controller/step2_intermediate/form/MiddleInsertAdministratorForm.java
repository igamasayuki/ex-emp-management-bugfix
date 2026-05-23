package com.example.controller.step2_intermediate.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 管理者情報登録時に使用するフォーム（中級対応版）.
 */
public class MiddleInsertAdministratorForm {

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

    @NotBlank(message = "確認用パスワードを入力してください")
    private String passwordConfirm;

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

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
