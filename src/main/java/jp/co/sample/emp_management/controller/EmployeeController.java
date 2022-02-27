package jp.co.sample.emp_management.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import jp.co.sample.emp_management.domain.Employee;
import jp.co.sample.emp_management.form.InsertEmployeeForm;
import jp.co.sample.emp_management.form.SearchEmployeeForm;
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

	@Autowired
	private MessageSource messageSource;

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 *
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpForm() {
		return new UpdateEmployeeForm();
	}


	@ModelAttribute
	public SearchEmployeeForm setUpSearchEmployeeForm() {
		return new SearchEmployeeForm();
	}

	@ModelAttribute
	public InsertEmployeeForm setUpinsertEmployeeForm() {
		return new InsertEmployeeForm();
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
	 * @param id リクエストパラメータで送られてくる従業員ID
	 * @param model モデル
	 * @return 従業員詳細画面
	 */
	@RequestMapping("/showDetail")
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
	@RequestMapping("/update")
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
	 * 従業員名であいまい検索する
	 *
	 * @param form 従業員名情報
	 * @param model リクエストスコープ
	 * @return 従業員一覧ページ
	 */
	@RequestMapping("/searchEmployee")
	public String searchEmployee(SearchEmployeeForm form, Model model) {
		List<Employee> employeeList = employeeService.findByEmployeeName(form.getName());
		if (employeeList.isEmpty()) {
			model.addAttribute("zeroResults",
					messageSource.getMessage("zeroResults", new String[] {}, Locale.getDefault()));
		} else {
			model.addAttribute("employeeList", employeeList);
		}
		return "employee/list";
	}

	/**
	 * 従業員登録ページへ遷移
	 *
	 * @param form 従業員情報
	 * @param model リクエストスコープ
	 * @return 従業員登録ページ
	 */
	@RequestMapping("/toInsert")
	public String searchEmployee(Model model) {
		return "employee/insert";
	}

	/**
	 * 従業員登録メソッド
	 *
	 * @param form 新規従業員情報
	 * @param result バリデーション情報
	 * @param model リクエストスコープ
	 * @return 従業員一覧ページ
	 */
	@RequestMapping("/insert")
	public String insert(@Validated InsertEmployeeForm form, BindingResult result, Model model) {
		Boolean isEmailExists = employeeService.findByMailAddress(form.getMailAddress());
		if (isEmailExists) {
			result.rejectValue("mailAddress", "dupulicateEmailError");
		}
		if (result.hasErrors() || isEmailExists) {
			return "employee/insert";
		}
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);
		employee.setSalary(Integer.parseInt(form.getSalary()));
		employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
		employee.setImage(saveImage(form));
		employeeService.insert(employee);
		return "redirect:/employee/showList";
	}

	/**
	 * jQuery 従業一覧ページ名前検索のオートコンプリート機能
	 *
	 * @return
	 */
	@ResponseBody
	@RequestMapping("suggest")
	public List<String> suggest() {
		List<Employee> employeeList = employeeService.showList();
		List<String> employeeNameList = employeeList.stream().map(employee -> employee.getName())
				.collect(Collectors.toList());
		return employeeNameList;
	}

	/**
	 * 画像保存処理
	 *
	 * @param form 新規従業員情報
	 * @return 登録画像ファイル名
	 */
	public String saveImage(InsertEmployeeForm form) {
		MultipartFile profileImage = form.getImage();
		// 画像をサーバへ保存
		if (!"".equals(profileImage.getOriginalFilename())) {
			String saveImageName =
					form.getMailAddress() + getExtension(profileImage.getOriginalFilename());
			// 画像保存先のパスオブジェクトを作成
			Path uploadFile = Paths.get("src/main/resources/static/img/" + saveImageName);
			// 画像保存先へのoutputStreamを作成
			try (OutputStream os = Files.newOutputStream(uploadFile, StandardOpenOption.CREATE)) {
				byte[] bytes = profileImage.getBytes();
				os.write(bytes);
				return saveImageName;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/** イメージの拡張子を取得 */
	public String getExtension(String originalImageName) {
		int dot = originalImageName.lastIndexOf(".");
		if (dot > 0) {
			return originalImageName.substring(dot);
		}
		return "";
	}
}
