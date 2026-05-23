package com.example.service.step2_intermediate;

import com.example.domain.Administrator;
import com.example.repository.step2_intermediate.MiddleAdministratorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理者情報を操作するサービス（中級対応版）.
 */
@Service
@Transactional
public class MiddleAdministratorService {
	
    @Autowired
    private MiddleAdministratorRepository administratorRepository;

    public void insert(Administrator administrator) {
        administratorRepository.insert(administrator);
    }

    public Administrator findByMailAddress(String mailAddress) {
        return administratorRepository.findByMailAddress(mailAddress);
    }

    public Administrator login(String mailAddress, String password) {
        // (5-1) SQL Injection 対策済みのメソッドを呼び出し
        return administratorRepository.findByMailAddressAndPassword(mailAddress, password);
    }
}
