package com.example.controller.step3_advance;

import com.example.controller.step3_advance.form.InsertEmployeeForm;
import com.example.domain.DisplaySize;
import com.example.domain.Employee;
import com.example.domain.EmployeeSortKey;
import com.example.form.UpdateEmployeeForm;
import com.example.service.step3_advance.FullEmployeeService;
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
 * 従業員情報を操作するコントローラー（上級対応版）.
 */
@Controller("step3FullEmployeeController")
@RequestMapping("/advanceEmployee")
public class FullEmployeeController {

    @Autowired
    private FullEmployeeService employeeService;

    private static final int PAGE_SIZE = 10;

    @ModelAttribute
    public InsertEmployeeForm setUpInsertEmployeeForm() {
        return new InsertEmployeeForm();
    }

    @ModelAttribute
    public UpdateEmployeeForm setUpUpdateForm() {
        return new UpdateEmployeeForm();
    }

    private void prepareAutocomplete(Model model) {
        List<Employee> allEmployees = employeeService.showList();
        model.addAttribute("allEmployees", allEmployees);
        model.addAttribute("employeeNames", allEmployees.stream().map(Employee::getName).collect(Collectors.toList()));
    }

    /**
     * 従業員一覧画面を出力します.
     */
    @GetMapping("/showList")
    public String showList(Integer page, Model model) {
        if (page == null) {
            page = 1;
        }
        
        List<Employee> employeeList = employeeService.showList(page, PAGE_SIZE);
        int totalCount = employeeService.getCount();
        int totalPages = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        model.addAttribute("employeeList", employeeList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        model.addAttribute("pageNumbers", pageNumbers);

        prepareAutocomplete(model);
        return "step3_advance/employee/list_datalist";
    }

    /**
     * 別解：Enumによるプロ仕様の一覧表示.
     * URL例：/advanceEmployee/showListWithEnum?page=1&size=LARGE&sort=SALARY_DESC
     */
    @GetMapping("/showListWithEnum")
    public String showListWithEnum(Integer page, DisplaySize size, EmployeeSortKey sort, Model model) {
        page = (page == null) ? 1 : page;
        size = (size == null) ? DisplaySize.SMALL : size;
        sort = (sort == null) ? EmployeeSortKey.HIRE_DATE_DESC : sort;

        List<Employee> employeeList = employeeService.showListWithEnum(page, size, sort);
        int totalPages = (int) Math.ceil((double) employeeService.getCount() / size.getCount());

        model.addAttribute("employeeList", employeeList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        prepareAutocomplete(model);
        return "step3_advance/employee/list_datalist";
    }

    /**
     * 従業員詳細画面を出力します.
     * (4-3) パンくずリスト修正済みのテンプレートを返却
     */
    @GetMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);
        return "step3_advance/employee/detail";
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
        return "redirect:/advanceEmployee/showList";
    }

    @GetMapping("/search")
    public String search(String name, Model model) {
        List<Employee> employeeList = employeeService.findByName(name);
        if (employeeList.isEmpty()) {
            model.addAttribute("message", "１件もありませんでした");
            return showList(1, model);
        }
        model.addAttribute("employeeList", employeeList);
        prepareAutocomplete(model);
        return "step3_advance/employee/list_datalist";
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "step3_advance/employee/insert";
    }

    @PostMapping("/insert")
    public String insert(@Validated InsertEmployeeForm form, BindingResult result, Model model) {
        MultipartFile imageFile = form.getImage();
        String contentType = imageFile.getContentType();
        if (contentType != null && !contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            result.rejectValue("image", null, "画像はjpgまたはpng形式でアップロードしてください");
        }

        if (result.hasErrors()) {
            return "step3_advance/employee/insert";
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
            e.printStackTrace();
            employee.setImage("no_image.png");
        }

        employeeService.insert(employee);
        return "redirect:/advanceEmployee/showList";
    }
}
