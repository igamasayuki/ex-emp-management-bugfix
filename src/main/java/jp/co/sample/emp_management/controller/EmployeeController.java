package jp.co.sample.emp_management.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.UpdateEmployeeForm;
import jp.co.sample.emp_management.service.EmployeeService;

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
	@RequestMapping("/showList")
	public String showList(Model model) {
		List<Employee> employeeList = employeeService.showList();
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
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		return "employee/detail";
	}

	/**
	 * 
	 * @param searchName 検索キーワードを取得
	 * @param model      検索結果がない場合にエラーメッセージを格納する
	 * @return 従業員詳細画面
	 */
	@PostMapping("/search")
	public String search(String searchName, Model model) {
		List<Employee> employeeList = employeeService.findByName(searchName);
		if (searchName.isEmpty()) {
			return showList(model);
		}
		if (employeeList.size() == 0) {
			model.addAttribute("notFoundMessage", "1件もありませんでした");
			return showList(model);
		}
		model.addAttribute("employeeList", employeeList);
		return "employee/list";
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
	@RequestMapping("/update")
	public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			System.out.println(form);
			return showDetail(form.getId(), model);
		}
		Employee employee = new Employee();
		employee.setId(form.getIntId());
		employee.setDependentsCount(form.getIntDependentsCount());
		employeeService.update(employee);
		return "redirect:/employee/showList";
	}

	@GetMapping("/toCreateEmployee")
	public String toInsert() {
		return "employee/create-employee";
	}

	@PostMapping("/create")
	public String create(InsertEmployeeForm form) {
		Integer id = employeeService.getMaxIdEmployee().getId() + 1;

		// MultipartFileでフォームから画像ファイルを受信
		MultipartFile file = form.getImage();
		if (file.getSize() >= 20000) {
			return toInsert();
		}
		// デフォルトの画像ファイルの名前を取得
		String defaultFileName = file.getOriginalFilename();

		String extension = defaultFileName.substring(defaultFileName.lastIndexOf("."));
		String fileName = "e" + id + extension;

		// javaIOでフィルパスと画像の名前を指定
		File filepath = new File("src/main/resources/static/img/" + fileName);
		try {
			// バイト配列に格納
			byte[] bytes = file.getBytes();
			// javaIOのFileOutputStreamクラス・writeメソッドを使用して画像を保存
			FileOutputStream stream = new FileOutputStream(filepath.toString());
//			stream.write(bytes);
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

//		Employee employee = new Employee();
//		BeanUtils.copyProperties(form, employee);
//		employee.setImage(fileName);
//		employeeService.insert(employee);
		return toInsert();
	}

}
