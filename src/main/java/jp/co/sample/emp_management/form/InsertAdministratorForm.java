package jp.co.sample.emp_management.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 管理者情報登録時に使用するフォーム.
 *
 * @author igamasayuki
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertAdministratorForm {
	/** 名前 */
	@NotBlank
	@Size(min = 1, max = 128)
	private String name;
	/** メールアドレス */
	@NotBlank
	@Email
	@Size(min = 1, max = 128)
	private String mailAddress;
	/** パスワード */
	@NotBlank
	@Size(min = 8, max = 16)
	private String password;

	@NotBlank
	@Size(min = 8, max = 16)
	private String passwordConfirmation;

}
