package com.example.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.Base64;
import java.util.List;

import com.example.common.Gender;
import com.example.domain.Administrator;
import com.example.domain.LoginAdministrator;
import com.example.form.InsertEmployeeForm;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Employee;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author igamasayuki
 *
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private HttpSession session;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員一覧を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@GetMapping("/showList")
	public String showList(Model model, String name) {
		if(name == null){
			name = "";
		}
		List<Employee> employeeList = employeeService.showListByName(name);
		if(employeeList.isEmpty()){
			model.addAttribute("notFound", "１件もありませんでした");
			employeeList = employeeService.showList();
		}
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を表示する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細画面を出力します.
	 * 
	 * @param id    リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@GetMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/////////////////////////////////////////////////////
	// ユースケース：従業員詳細を更新する
	/////////////////////////////////////////////////////
	/**
	 * 従業員詳細(ここでは扶養人数のみ)を更新します.
	 * 
	 * @param form 従業員情報用フォーム
	 * @return 従業員一覧画面へリダクレクト
	 */
	@PostMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	/**
	 * 従業員登録画面の表示
	 *
	 * @param form 従業員情報の入力
	 * @param model 性別情報の格納
	 * @return 従業員登録画面
	 */
	@GetMapping("/insert")
	public String insert(InsertEmployeeForm form, Model model){
		model.addAttribute("genders", Gender.getMap());
		return "/employee/insert";
	}

	@PostMapping("save")
	public synchronized String save(@Validated InsertEmployeeForm insertEmployeeForm, BindingResult result, String address2, Model model){
		if(!insertEmployeeForm.getImage().getContentType().contains("png") && !insertEmployeeForm.getImage().getContentType().contains("jpg")){
			result.rejectValue("image", "", "画像はjpg形式かpng形式のみです");
		}

		if(result.hasErrors()){
			return insert(insertEmployeeForm, model);
		}

		Employee employee = new Employee();
		BeanUtils.copyProperties(insertEmployeeForm, employee);
		employee.setGender(Gender.of(insertEmployeeForm.getGender()).getValue());
		employee.setHireDate(Date.valueOf(insertEmployeeForm.getHireDate()));
		employee.setAddress(employee.getAddress() + address2);

		try {
			String base64Image = Base64.getEncoder().encodeToString(insertEmployeeForm.getImage().getBytes());
			if("image/png".equals(insertEmployeeForm.getImage().getContentType())){
				employee.setImage("data:image/png;base64," + base64Image);
			} else if ("image/jpg".equals(insertEmployeeForm.getImage().getContentType())) {
				employee.setImage("data:image/jpg;base64," + base64Image);
			}
		}catch (IOException e){
			e.printStackTrace();
		}

		employeeService.insert(employee);
		return "redirect:/employee/showList";
	}
}
