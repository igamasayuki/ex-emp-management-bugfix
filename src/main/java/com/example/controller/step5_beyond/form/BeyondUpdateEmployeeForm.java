package com.example.controller.step5_beyond.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 追加課題(8-1) 従業員更新フォーム.
 */
public class BeyondUpdateEmployeeForm {

    @NotBlank(message = "名前を入力してください")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が不正です")
    private String mailAddress;

    @NotBlank(message = "電話番号を入力してください")
    private String telephone;

    @NotNull(message = "給料を入力してください")
    private Integer salary;

    @NotBlank(message = "住所を入力してください")
    private String address;

    private String id;

    @Pattern(regexp = "^[0-9]+$", message = "扶養人数は数値で入力してください")
    private String dependentsCount;

    public Integer getIntId() {
        return Integer.parseInt(id);
    }

    public Integer getIntDependentsCount() {
        return Integer.parseInt(dependentsCount);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDependentsCount() {
        return dependentsCount;
    }

    public void setDependentsCount(String dependentsCount) {
        this.dependentsCount = dependentsCount;
    }
}
