package com.example.controller;

import com.example.domain.Employee;
import com.example.form.InsertEmployeeForm;
import com.example.form.UpdateEmployeeForm;
import com.example.service.EmployeeService;
import org.springframework.beans.BeanUtils;
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
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 従業員情報を操作するコントローラー（完成形）.
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    private static final int PAGE_SIZE = 10;

    @Autowired
    private EmployeeService employeeService;

    @ModelAttribute
    public UpdateEmployeeForm setUpUpdateForm() {
        return new UpdateEmployeeForm();
    }

    @ModelAttribute
    public InsertEmployeeForm setUpInsertEmployeeForm() {
        return new InsertEmployeeForm();
    }

    private void prepareAutocomplete(Model model) {
        List<Employee> allEmployees = employeeService.showList();
        model.addAttribute("allEmployees", allEmployees);
        model.addAttribute("employeeNames", allEmployees.stream().map(Employee::getName).collect(Collectors.toList()));
    }

    @GetMapping("/showList")
    public String showList(Integer page, Model model) {
        page = (page == null) ? 1 : page;

        List<Employee> employeeList = employeeService.showList(page, PAGE_SIZE);
        int totalCount = employeeService.getCount();
        int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / PAGE_SIZE));

        model.addAttribute("employeeList", employeeList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        prepareAutocomplete(model);
        return "employee/list";
    }

    @GetMapping("/search")
    public String search(String name, Model model) {
        List<Employee> employeeList = employeeService.findByName(name);
        if (employeeList.isEmpty()) {
            model.addAttribute("message", "１件もありませんでした");
            return showList(1, model);
        }
        model.addAttribute("employeeList", employeeList);
        model.addAttribute("searchMode", true);
        prepareAutocomplete(model);
        return "employee/list";
    }

    @GetMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);
        UpdateEmployeeForm updateForm = new UpdateEmployeeForm();
        updateForm.setId(String.valueOf(employee.getId()));
        updateForm.setDependentsCount(String.valueOf(employee.getDependentsCount()));
        model.addAttribute("updateEmployeeForm", updateForm);
        return "employee/detail";
    }

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

    @GetMapping("/toInsert")
    public String toInsert() {
        return "employee/insert";
    }

    @PostMapping("/insert")
    public String insert(@Validated InsertEmployeeForm form, BindingResult result, Model model) {
        MultipartFile imageFile = form.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            String contentType = imageFile.getContentType();
            if (contentType != null && !contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                result.rejectValue("image", null, "画像はjpgまたはpng形式でアップロードしてください");
            }
        }

        if (result.hasErrors()) {
            return "employee/insert";
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(form, employee);
        employee.setHireDate(Date.valueOf(form.getHireDate()));
        employee.setDependentsCount(0);

        String fileName = imageFile.getOriginalFilename();
        try {
            byte[] bytes = imageFile.getBytes();
            Path path = Paths.get("src/main/resources/static/img/" + fileName);
            Files.write(path, bytes);
            employee.setImage(fileName);
        } catch (IOException e) {
            employee.setImage("no_image.png");
        }

        employeeService.insert(employee);
        return "redirect:/employee/showList";
    }
}
