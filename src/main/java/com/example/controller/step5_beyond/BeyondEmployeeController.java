package com.example.controller.step5_beyond;

import com.example.controller.step5_beyond.form.BeyondInsertEmployeeForm;
import com.example.controller.step5_beyond.form.BeyondUpdateEmployeeForm;
import com.example.domain.Employee;
import com.example.service.step5_beyond.BeyondEmployeeService;
import com.example.service.step5_beyond.BeyondEmployeeSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 追加課題(8-1〜29-1, 13-3 UI) 従業員コントローラー.
 */
@Controller("beyondEmployeeController")
@RequestMapping("/beyondEmployee")
public class BeyondEmployeeController {

    private static final Logger log = LoggerFactory.getLogger(BeyondEmployeeController.class);
    private static final int PAGE_SIZE = 10;

    @Autowired
    private BeyondEmployeeService employeeService;

    @ModelAttribute("beyondUpdateEmployeeForm")
    public BeyondUpdateEmployeeForm setUpUpdateForm() {
        return new BeyondUpdateEmployeeForm();
    }

    @ModelAttribute("beyondInsertEmployeeForm")
    public BeyondInsertEmployeeForm setUpInsertEmployeeForm() {
        return new BeyondInsertEmployeeForm();
    }

    private boolean hasSearchCriteria(String name, String mail, String telephone) {
        return (name != null && !name.isEmpty())
                || (mail != null && !mail.isEmpty())
                || (telephone != null && !telephone.isEmpty());
    }

    private void prepareAutocomplete(Model model) {
        List<Employee> allEmployees = employeeService.showList();
        model.addAttribute("allEmployees", allEmployees);
        model.addAttribute("employeeNames",
                allEmployees.stream().map(Employee::getName).collect(Collectors.toList()));
    }

    private void prepareListModel(Model model, int page, int totalPages, String sort, String order,
            String name, String mail, String telephone) {
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        if (sort == null) {
            sort = "hireDate";
        }
        if (order == null) {
            order = "desc";
        }
        if (name == null) {
            name = "";
        }
        if (mail == null) {
            mail = "";
        }
        if (telephone == null) {
            telephone = "";
        }
        model.addAttribute("sort", sort);
        model.addAttribute("order", order);
        model.addAttribute("searchName", name);
        model.addAttribute("searchMail", mail);
        model.addAttribute("searchTelephone", telephone);

        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = 1; i <= totalPages; i++) {
            pageNumbers.add(i);
        }
        model.addAttribute("pageNumbers", pageNumbers);
    }

    @GetMapping("/showList")
    public String showList(Integer page, String sort, String order, String name, String mail, String telephone,
            Model model) {
        if (page == null || page < 1) {
            page = 1;
        }
        if (sort == null) {
            sort = "hireDate";
        }
        if (order == null) {
            order = "desc";
        }

        if (hasSearchCriteria(name, mail, telephone) && employeeService.getCount(name, mail, telephone) == 0) {
            model.addAttribute("message", "１件もありませんでした");
            name = null;
            mail = null;
            telephone = null;
        }

        int totalCount = employeeService.getCount(name, mail, telephone);
        int totalPages = Math.max(1, (int) Math.ceil((double) totalCount / PAGE_SIZE));
        if (page > totalPages) {
            page = totalPages;
        }

        List<Employee> employeeList =
                employeeService.showList(page, PAGE_SIZE, name, mail, telephone, sort, order);

        model.addAttribute("employeeList", employeeList);
        prepareListModel(model, page, totalPages, sort, order, name, mail, telephone);
        prepareAutocomplete(model);
        return "step5_beyond/employee/list";
    }

    @GetMapping("/showDetail")
    public String showDetail(String id, Model model) {
        Employee employee = employeeService.showDetail(Integer.parseInt(id));
        model.addAttribute("employee", employee);

        BeyondUpdateEmployeeForm updateForm = new BeyondUpdateEmployeeForm();
        updateForm.setId(String.valueOf(employee.getId()));
        updateForm.setName(employee.getName());
        updateForm.setMailAddress(employee.getMailAddress());
        updateForm.setTelephone(employee.getTelephone());
        updateForm.setSalary(employee.getSalary());
        updateForm.setAddress(employee.getAddress());
        updateForm.setDependentsCount(String.valueOf(employee.getDependentsCount()));
        model.addAttribute("beyondUpdateEmployeeForm", updateForm);
        return "step5_beyond/employee/detail";
    }

    @PostMapping("/update")
    public String update(@Validated @ModelAttribute("beyondUpdateEmployeeForm") BeyondUpdateEmployeeForm form,
            BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("employee", employeeService.showDetail(form.getIntId()));
            return "step5_beyond/employee/detail";
        }
        try {
            Employee employee = new Employee();
            BeanUtils.copyProperties(form, employee);
            employee.setId(form.getIntId());
            employee.setDependentsCount(form.getIntDependentsCount());
            employeeService.update(employee);
            redirectAttributes.addFlashAttribute("successMessage", "employee.update.success");
            return "redirect:/beyondEmployee/showDetail?id=" + form.getId();
        } catch (Exception e) {
            log.error("従業員更新に失敗しました id={}", form.getId(), e);
            model.addAttribute("employee", employeeService.showDetail(form.getIntId()));
            model.addAttribute("beyondUpdateEmployeeForm", form);
            model.addAttribute("errorMessage", "更新に失敗しました");
            return "step5_beyond/employee/detail";
        }
    }

    @PostMapping("/delete")
    public String delete(String id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.delete(Integer.parseInt(id));
            redirectAttributes.addFlashAttribute("successMessage", "employee.delete.success");
        } catch (Exception e) {
            log.error("従業員削除に失敗しました id={}", id, e);
            redirectAttributes.addFlashAttribute("errorMessage", "削除に失敗しました");
        }
        return "redirect:/beyondEmployee/showList";
    }

    @GetMapping("/toInsert")
    public String toInsert() {
        return "step5_beyond/employee/insert";
    }

    @PostMapping("/insert")
    public String insert(@Validated @ModelAttribute("beyondInsertEmployeeForm") BeyondInsertEmployeeForm form,
            BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        MultipartFile imageFile = form.getImage();
        if (imageFile != null && !imageFile.isEmpty()) {
            String contentType = imageFile.getContentType();
            if (contentType != null && !contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                result.rejectValue("image", null, "画像はjpgまたはpng形式でアップロードしてください");
            }
        }
        if (result.hasErrors()) {
            return "step5_beyond/employee/insert";
        }

        Employee employee = new Employee();
        BeanUtils.copyProperties(form, employee);
        employee.setHireDate(Date.valueOf(form.getHireDate()));
        employee.setDependentsCount(0);

        String fileName = imageFile.getOriginalFilename();
        try {
            Path path = Paths.get("src/main/resources/static/img/" + fileName);
            Files.write(path, imageFile.getBytes());
            employee.setImage(fileName);
        } catch (IOException e) {
            log.error("画像保存に失敗しました", e);
            employee.setImage("no_image.png");
        }

        employeeService.insert(employee);
        redirectAttributes.addFlashAttribute("successMessage", "employee.insert.success");
        return "redirect:/beyondEmployee/showList";
    }
}
