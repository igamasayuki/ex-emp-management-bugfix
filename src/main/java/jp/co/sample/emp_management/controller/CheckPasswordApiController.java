package jp.co.sample.emp_management.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CheckPasswordApiController {

	@PostMapping("/check")
		public Map<String, String> check(String password, String confirmationPassword) {
		
			Map<String, String> map = new HashMap<>();
			
			//パスワード一致チェック
			String passwordMissMatch = null;
			if(confirmationPassword != null && confirmationPassword != "" && password.equals(confirmationPassword)) {
				passwordMissMatch = "確認用パスワード入力OK！";
			} else {
				passwordMissMatch = "パスワードが一致していません";
			}
			
			map.put("passwordMissMatch", passwordMissMatch);
			
			System.out.println(password + ":" + confirmationPassword);
			
			return map;
	}
}
