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
    public String showList(Integer page, Model model) {
        final int pageSize = 10;
        int currentPage = page == null || page < 1 ? 1 : page;
        List<Employee> employeeList = employeeService.showList();
        int totalPages = (int) Math.ceil((double) employeeList.size() / pageSize);
        int fromIndex = Math.min((currentPage - 1) * pageSize, employeeList.size());
        int toIndex = Math.min(fromIndex + pageSize, employeeList.size());
        // 全件取得後に画面用の10件へ切り出す。まずページングの考え方を学ぶための素直な実装。
        model.addAttribute("employeeList", employeeList.subList(fromIndex, toIndex));
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
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
}
