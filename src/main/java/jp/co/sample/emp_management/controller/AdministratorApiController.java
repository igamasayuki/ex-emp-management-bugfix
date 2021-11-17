package jp.co.sample.emp_management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AdministratorApiController {

	@ResponseBody
	@RequestMapping(value = "/checkConPass", method = RequestMethod.POST)
	public Map<String, String> checkConPass(String password, String confirmPassword) {
		
		Map<String, String> map = new HashMap<>();
		String duplicateMessage = null;
		if (confirmPassword.isBlank()) {
			duplicateMessage = "確認用パスワードが空欄です";
		}else if (confirmPassword.equals(password)) {
			duplicateMessage = "確認用パスワード入力OK!";
		} else {
			duplicateMessage = "確認用パスワードとパスワードが一致していません";
		}
		map.put("duplicateMessage", duplicateMessage);
		return map;
	}

}
