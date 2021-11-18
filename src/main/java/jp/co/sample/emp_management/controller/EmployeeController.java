package jp.co.sample.emp_management.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
	public InsertEmployeeForm setUpInsertEmployeeForm() {
		return new InsertEmployeeForm();
	}

	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * 
	 * @return フォーム
	 */
	@ModelAttribute
	public UpdateEmployeeForm setUpUpdateEmployeeForm() {
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
	// ユースケース：従業員から名前の「含む検索」を行う
	/////////////////////////////////////////////////////
	/**
	 * 従業員一覧画面に、検索結果を出力します.
	 * 
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/searchByName")
	public String searchByName(String name, Model model) {
		List<Employee> employeeList = employeeService.searchByName(name);

		if (employeeList.size() == 0) {
			model.addAttribute("message", "１件もありませんでした");
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
	@RequestMapping("/showDetail")
	public String showDetail(String id, Model model) {
		Employee employee = employeeService.showDetail(Integer.parseInt(id));
		model.addAttribute("employee", employee);
		// 写真データを受け渡し
		//	model.addAttribute("userPhoto", photoView(employee));

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

	/////////////////////////////////////////////////////
	// ユースケース：従業員を登録する
	/////////////////////////////////////////////////////
	/**
	 * 従業員登録画面を出力します.
	 * 
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/toInsert")
	public String toInsert() {
		return "employee/insert";
	}

	/**
	 * 従業員を登録します。
	 * 
	 * @param form  従業員情報用フォーム
	 * @param model モデル
	 * @return 従業員一覧画面
	 */
	@RequestMapping("/insert")
	public synchronized String Insert(MultipartFile image, InsertEmployeeForm form,
			RedirectAttributes redirectAttributes) throws Exception {
		
		Employee employee = new Employee();
		BeanUtils.copyProperties(form, employee);
		employee.setHireDate(Date.valueOf(form.getHireDate()));
		employee.setSalary(Integer.parseInt(form.getSalary()));
		employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));

		// 画像処理
		if (!image.isEmpty()) {
			try {
				// ファイル名をDBのIDにリネイム
				File oldFileName = new File(image.getOriginalFilename());
				File newFileName = new File("e" + employeeService.getNextId() + ".png");
				oldFileName.renameTo(newFileName);

				// 保存先を定義
				String uploadPath = "src/main/resources/static/img/";
				// 画像をバイトコードに変換
				byte[] bytes = image.getBytes();

				// 指定ファイルへ読み込みファイルを書き込み
				BufferedOutputStream stream = new BufferedOutputStream(
						new FileOutputStream(new File(uploadPath + newFileName)));
				stream.write(bytes);
				stream.close();

				// データベースに登録するように、ファイル名をドメインにしまう
				System.out.println(newFileName.toString());
				employee.setImage(newFileName.toString());

			} catch (Exception e) {
				System.err.println(e);
			}
		}

		System.out.println(employee.getImage());

		employeeService.insert(employee);

		redirectAttributes.addFlashAttribute("message", "従業員の登録が完了しました。");
		return "redirect:/employee/showList";
	}

//	public String photoView(Employee employee) {
//		// 画像を検索してbyteとしてViewへ受け渡す
//		String uploadPath = "src/main/resources/static/img/" + employee.getImage();
//		// 画像データストリームを取得する
//		try (FileInputStream fis = new FileInputStream(uploadPath);) {
//			StringBuffer data = new StringBuffer();
//			ByteArrayOutputStream os = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			// バイト配列に変換
//			while (true) {
//				int len = fis.read(buffer);
//				if (len < 0) {
//					break;
//				}
//				os.write(buffer, 0, len);
//			}
//			// 画像データをbaseにエンコード
//			String base64 = new String(org.apache.tomcat.util.codec.binary.Base64.encodeBase64(os.toByteArray()),
//					"ASCII");
//			// 画像タイプはJPEG
//			// Viewへの受け渡し。append("data:~~)としているとtymleafでの表示が楽になる
//			data.append("data:image/jpeg;base64,");
//			data.append(base64);
//
//			return data.toString();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

//	// 画像をViewに渡す
//	@RequestMapping("/top")
//	public String top(@AuthenticationPrincipal LoginAccount account, Model model) {
//		// employeNumberを利用してemployee nameを検索。そしてviewへ受け渡す
//		EmployeeEntity user = repository.findByEmployeeNumber(account.getUsername());
//		model.addAttribute("userName", user.getEmployeeName());
//
//		// 写真データを受け渡し
//		model.addAttribute("userPhoto", photoView(employee));
//
//		return "top";
//	}

}
