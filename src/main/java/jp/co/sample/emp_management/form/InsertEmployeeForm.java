package jp.co.sample.emp_management.form;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertEmployeeForm implements Serializable {
	/** 従業員名 */
	@Size(min = 1, max = 128)
	private String name;
	/** 画像 */
	private MultipartFile image;
	/** 性別 */
	@Pattern(regexp = "(男性|女性)")
	private String gender;
	/** 入社日 */
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date hireDate;
	/** メールアドレス */
	@NotBlank
	@Email
	private String mailAddress;
	/** 郵便番号 */
	@NotBlank
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}$")
	private String zipCode;
	/** 住所 */
	@NotBlank
	private String address;
	/** 電話番号 */
	@NotBlank
	@Pattern(regexp = "^0[0-9]{1,4}-[0-9]{4}-[0-9]{4}$")
	private String telephone;
	/** 給料 */
	@NotBlank
	@Pattern(regexp = "^(0|[1-9][0-9]{0,5})$")
	private String salary;
	/** 特性 */
	@Size(min = 1, max = 300)
	private String characteristics;
	/** 扶養人数 */
	@Pattern(regexp = "^[0-9]$")
	private String dependentsCount;

}
