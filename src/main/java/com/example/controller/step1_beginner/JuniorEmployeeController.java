package com.example.controller.step1_beginner;

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

import java.util.Comparator;
import java.util.List;

/**
 * 従業員情報を操作するコントローラー（初級対応版）.
 * 既存の EmployeeController を汚さないための比較用クラス。
 */
@Controller
@RequestMapping("/junior/employee")
public class JuniorEmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute
    public UpdateEmployeeForm setUpForm() {
        return new UpdateEmployeeForm();
    }

    /**
     * 従業員一覧画面を出力します.
     * (3-1) 初級 従業員一覧の並び順：手元比較用としてController側で「入社日の降順」に並び替えます。
     * ※本来の正解は EmployeeRepository の SQL (ORDER BY hire_date DESC) で行うべきです。
     */
    @GetMapping("/showList")
    public String showList(Model model) {
        List<Employee> employeeList = employeeService.showList();
        
        // (3-1) 手元環境での独立性維持のため、Java側でソート（入社日の降順）
        employeeList.sort(Comparator.comparing(Employee::getHireDate).reversed());
        
        model.addAttribute("employeeList", employeeList);
        return "step1_beginner/employee/list";
    }

    /**
     * 従業員詳細画面を出力します.
     */
    @GetMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);
        return "step1_beginner/employee/detail";
    }

    /**
     * 従業員詳細を更新します.
     */
    @PostMapping("/update")
    public String update(@Validated UpdateEmployeeForm form, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return showDetail(String.valueOf(form.getId()), model);
        }
        Employee employee = new Employee();
        employee.setId(form.getIntId());
        employee.setDependentsCount(form.getIntDependentsCount());
        employeeService.update(employee);
        
        // 更新後は初級用の一覧へリダイレクト
        return "redirect:/junior/employee/showList";
    }
}
