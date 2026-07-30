package com.example.service.step5_beyond;

import com.example.domain.BeyondEmployeeSortKey;
import com.example.domain.Employee;
import com.example.repository.step5_beyond.BeyondEmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 追加課題用 従業員サービス.
 */
@Service("beyondEmployeeService")
@Transactional
public class BeyondEmployeeService {

    private static final Logger log = LoggerFactory.getLogger(BeyondEmployeeService.class);
    private static final Path IMG_DIR = Paths.get("src/main/resources/static/img");

    @Autowired
    private BeyondEmployeeRepository employeeRepository;

    public synchronized void insert(Employee employee) {
        Integer maxId = employeeRepository.getMaxId();
        if (maxId == null) {
            employee.setId(1);
        } else {
            employee.setId(maxId + 1);
        }
        employeeRepository.insert(employee);
        log.info("従業員を登録しました id={}", employee.getId());
    }

    public List<Employee> showList() {
        return employeeRepository.findAll();
    }

    public int getCount(String name, String mail, String telephone) {
        return employeeRepository.count(name, mail, telephone);
    }

    public List<Employee> showList(int page, int size, String name, String mail, String telephone,
            String sort, String order) {
        int offset = (page - 1) * size;
        BeyondEmployeeSortKey sortKey = BeyondEmployeeSortKey.fromUrl(sort, order);
        return employeeRepository.findPage(name, mail, telephone, sortKey, size, offset);
    }

    public Employee showDetail(Integer id) {
        return employeeRepository.load(id);
    }

    public void update(Employee employee) {
        employeeRepository.update(employee);
        log.info("従業員を更新しました id={}", employee.getId());
    }

    public void delete(Integer id) {
        Employee employee = employeeRepository.load(id);
        employeeRepository.deleteById(id);
        deleteImageIfPresent(employee.getImage());
        log.info("従業員を削除しました id={}", id);
    }

    private void deleteImageIfPresent(String imageFileName) {
        if (imageFileName == null || "no_image.png".equals(imageFileName)) {
            return;
        }
        try {
            Files.deleteIfExists(IMG_DIR.resolve(imageFileName));
        } catch (IOException e) {
            log.error("画像ファイルの削除に失敗しました file={}", imageFileName, e);
        }
    }
}
