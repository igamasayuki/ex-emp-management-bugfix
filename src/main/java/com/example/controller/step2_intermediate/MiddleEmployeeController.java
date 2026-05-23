package com.example.controller.step2_intermediate;

import com.example.domain.Employee;
import com.example.form.UpdateEmployeeForm;
import com.example.service.step2_intermediate.MiddleEmployeeService;
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
 * 従業員情報を操作するコントローラー（中級対応版）.
 */
@Controller
@RequestMapping("/middleEmployee")
public class MiddleEmployeeController {
	
    @Autowired
    private MiddleEmployeeService employeeService;

    @ModelAttribute
    public UpdateEmployeeForm setUpForm() {
        return new UpdateEmployeeForm();
    }

    /**
     * 従業員一覧画面を出力します.
     */
    @GetMapping("/showList")
    public String showList(Model model) {
        List<Employee> employeeList = employeeService.showList();
        model.addAttribute("employeeList", employeeList);
        return "step2_intermediate/employee/list";
    }

    /**
     * 従業員を名前で検索します.
     * (6-2) 中級対応
     */
    @GetMapping("/search")
    public String search(String name, Model model) {
        List<Employee> employeeList = employeeService.findByName(name);
        if (employeeList.isEmpty()) {
            model.addAttribute("message", "１件もありませんでした");
            employeeList = employeeService.showList();
        }
        model.addAttribute("employeeList", employeeList);
        return "step2_intermediate/employee/list";
    }

    /**
     * 従業員詳細画面を出力します.
     */
    @GetMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);
        return "step2_intermediate/employee/detail";
    }

    /**
     * 従業員詳細を更新します.
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
        return "redirect:/middleEmployee/showList";
    }
}
