package com.example.controller;

import com.example.domain.Employee;
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

        // 初期表示時、フォームオブジェクトに値をセットしてモデルに追加
        UpdateEmployeeForm form = new UpdateEmployeeForm();
        form.setId(id);
        form.setDependentsCount(String.valueOf(employee.getDependentsCount()));
        model.addAttribute("updateEmployeeForm", form);

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
            // エラー時、表示に必要な既存の従業員情報を再取得してモデルにセット
            Employee employee = employeeService.showDetail(form.getIntId());
            model.addAttribute("employee", employee);
            return "employee/detail";
        }

        Employee employee = new Employee();
        employee.setId(form.getIntId());
        employee.setDependentsCount(form.getIntDependentsCount());
        employeeService.update(employee);
        return "redirect:/employee/showList";
    }
}
