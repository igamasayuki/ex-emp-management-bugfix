package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Administrator;
import com.example.domain.Employee;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;

import jakarta.servlet.http.HttpSession;

/**
 * 従業員情報を操作するコントローラー.
 * 
 * @author nagahashirisa
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
	public String showList(Model model,HttpSession session) {
		List<Employee> employeeList = employeeService.showList();
		model.addAttribute("employeeList", employeeList);
		//管理者を情報を受け取る
		Administrator administrator = (Administrator) session.getAttribute("administrator");
		//キー名がadministratorName,受け取った管理者の名前をキーに入れる
		model.addAttribute("administratorName", administrator.getName());
		return "employee/list";
	}
	
	/////////////////////////////////////////////////////
	// ユースケース：曖昧検索で従業員を表示する
	/////////////////////////////////////////////////////
	/**
	 * 曖昧検索で従業員を出力する
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@GetMapping("/employee/list")
	public String findAmbiguousList(@RequestParam("name") String name,Model model,HttpSession session) {
		//もしnameが空文字列の場合、名前の入力が必要であることを示すメッセージをモデルに追加する。
		//employeeMapをセッションから取得してモデルに追加する
		if(name == "") {
			model.addAttribute("noEmployee", "名前を入力してください。");
			model.addAttribute("employeeMap", session.getAttribute("employeeMap"));
		}
		
		//employeeServiceを使用して、与えられた名前に基づいて従業員のリストを取得する。
		List<Employee> employees = employeeService.findAmbiguousList(name);
		
		//従業員を名前でグループ化するための空のマップを作成する。
		Map<String,List<Employee>> employeeMap = new HashMap<>();
		
		//取得した従業員のリストを反復処理する。
		//各従業員の名前をキーとして、マップ内でその名前に関連する従業員のリストを作成する。
		for(Employee employee : employees) {
			//マップ内に指定された名前のキーが存在しない場合、新しい従業員リストを作成し、マップに追加する。
			if(!employeeMap.containsKey(employee.getName())) {
				List<Employee> employeeSearchList = new ArrayList<>();
				employeeSearchList.add(employee);
				employeeMap.put(employee.getName(), employeeSearchList);
			} else {
				//名前のキーが既に存在する場合、そのキーに関連するリストに従業員を追加する
				employeeMap.get(employee.getName()).add(employee);
			}
		}
		//マップのサイズが0である場合、
		if(employeeMap.size() == 0) {
			//検索結果が見つからなかったことを示すため、セッションから取得したemployeeMapをモデルに追加
			model.addAttribute("employeeMap",session.getAttribute("employeeMap"));
			//"employee/list"ビューを返す。
			return "employee/list";
		} else {
			//マップのサイズが0でない場合、検索結果が存在するため、新しいemployeeMapをモデルに追加
			model.addAttribute("employeeMap", employeeMap);
			//"employee/list"ビューを返す。
			return "employee/list";
		}
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
		Administrator administrator = (Administrator) session.getAttribute("administrator");
		if (administrator == null) {
			return "redirect:/login";
		}model.addAttribute("administratorName", administrator.getName());
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
}
