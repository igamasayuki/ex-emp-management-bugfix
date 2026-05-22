package com.example.controller;

import com.example.domain.Employee;
import com.example.form.InsertEmployeeForm;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Date;
import java.util.List;

/**
 * 従業員情報を操作するコントローラー.
 *
 * @author igamasayuki
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

    @ModelAttribute
    public InsertEmployeeForm setUpInsertEmployeeForm() {
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
    @GetMapping("/showList")
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
    @GetMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);
        return "employee/detail";
    }

    /**
     * 従業員登録画面を表示します.
     */
    @GetMapping("/toInsert")
    public String toInsert() {
        return "employee/insert";
    }

    /**
     * 従業員情報を登録します.
     */
    @PostMapping("/insert")
    public String insert(InsertEmployeeForm form, Model model) throws IOException {
        MultipartFile imageFile = form.getImageFile();
        String imageName = "e1.png";
        if (imageFile != null && !imageFile.isEmpty()) {
            String originalName = imageFile.getOriginalFilename();
            String lowerName = originalName == null ? "" : originalName.toLowerCase();
            // 画像はjpg/pngだけ許可し、その他のファイルを保存しない。
            if (!lowerName.endsWith(".jpg") && !lowerName.endsWith(".jpeg") && !lowerName.endsWith(".png")) {
                model.addAttribute("errorMessage", "画像はjpgまたはpngを選択してください。");
                return "employee/insert";
            }
            imageName = System.currentTimeMillis() + "_" + originalName;
            Path imagePath = Path.of("src/main/resources/static/img", imageName);
            Files.copy(imageFile.getInputStream(), imagePath);
        }

        Employee employee = new Employee();
        employee.setName(form.getName());
        employee.setImage(imageName);
        employee.setGender(form.getGender());
        employee.setHireDate(Date.valueOf(form.getHireDate()));
        employee.setMailAddress(form.getMailAddress());
        employee.setZipCode(form.getZipCode());
        employee.setAddress(form.getAddress());
        employee.setTelephone(form.getTelephone());
        employee.setSalary(Integer.parseInt(form.getSalary()));
        employee.setCharacteristics(form.getCharacteristics());
        employee.setDependentsCount(Integer.parseInt(form.getDependentsCount()));
        employeeService.insert(employee);
        return "redirect:/employee/showList";
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
