package com.example.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 従業員登録フォーム.
 *
 * @author rui.inoue
 */
public class InsertEmployeeForm {
    /** 名前 */
    @NotBlank(message = "名前が未入力です")
    private String name;
    /** 画像 */
    private MultipartFile image;
    /** 性別 */
    @NotNull(message = "性別を選んでください")
    private Integer gender;
    /** 入社日 */
    @NotBlank(message = "入社日を入力してください")
    private String hireDate;
    /** メールアドレス */
    @NotBlank(message = "メールアドレスを入力してください")
    @Email(message = "メールアドレスの形式が不正です")
    private String mailAddress;
    /** 郵便番号 */
    @NotBlank(message = "郵便番号を入力してください")
    private String zipCode;
    /** 住所 */
    @NotBlank(message = "住所が未入力です")
    private String address;
    /** 電話番号 */
    @NotBlank(message = "電話番号が未入力です")
    @Pattern(regexp = "^(070|080|090)-\\d{4}-\\d{4}$", message = "電話番号の入力形式が不正です")
    private String telephone;
    /** 給料 */
    @NotNull(message = "給料が未入力です")
    private Integer salary;
    /** 特徴 */
    private String characteristics;
    /** 扶養人数 */
    @NotNull(message = "扶養人数が未入力です")
    private Integer dependentsCount;

    @Override
    public String toString() {
        return "InsertEmployeeForm{" +
                "name='" + name + '\'' +
                ", image=" + image +
                ", gender='" + gender + '\'' +
                ", hireDate=" + hireDate +
                ", mailAddress='" + mailAddress + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", salary=" + salary +
                ", characteristics='" + characteristics + '\'' +
                ", dependentsCount=" + dependentsCount +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

    public Integer getDependentsCount() {
        return dependentsCount;
    }

    public void setDependentsCount(Integer dependentsCount) {
        this.dependentsCount = dependentsCount;
    }
}
