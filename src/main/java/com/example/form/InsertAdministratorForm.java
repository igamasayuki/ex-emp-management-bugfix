package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 管理者情報登録時に使用するフォーム.
 *
 * @author igamasayuki
 */
public class InsertAdministratorForm {
    /**
     * 名前
     */
    @NotBlank(message = "名前は必須です")
    private String name;
    /**
     * メールアドレス
     */
    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String mailAddress;
    /**
     * パスワード
     */
    @NotBlank(message = "パスワードは必須です")
    private String password;

    /**
     * 確認用パスワード
     */
    @NotBlank(message = "確認用パスワードは必須です")
    private String confirmation;

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

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    @Override
    public String toString() {
        return "InsertAdministratorForm{" +
                "name='" + name + '\'' +
                ", mailAddress='" + mailAddress + '\'' +
                ", password='" + password + '\'' +
                ", confirmation='" + confirmation + '\'' +
                '}';
    }
}
