package com.example.service.step5_beyond;

import com.example.domain.Administrator;
import com.example.repository.step5_beyond.BeyondAdministratorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 追加課題用 管理者サービス.
 */
@Service("beyondAdministratorService")
@Transactional
public class BeyondAdministratorService {

    private static final Logger log = LoggerFactory.getLogger(BeyondAdministratorService.class);

    @Autowired
    private BeyondAdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void insert(Administrator administrator) {
        administrator.setPassword(passwordEncoder.encode(administrator.getPassword()));
        administratorRepository.insert(administrator);
        log.info("管理者を登録しました mail={}", administrator.getMailAddress());
    }

    public Administrator findByMailAddress(String mailAddress) {
        return administratorRepository.findByMailAddress(mailAddress);
    }

    public Administrator login(String mailAddress, String password) {
        Administrator administrator = administratorRepository.findByMailAddress(mailAddress);
        if (administrator == null) {
            return null;
        }
        if (passwordEncoder.matches(password, administrator.getPassword())) {
            log.info("ログイン成功 mail={}", mailAddress);
            return administrator;
        }
        return null;
    }

    public void changePassword(String mailAddress, String currentRaw, String newRaw) {
        Administrator admin = administratorRepository.findByMailAddress(mailAddress);
        if (admin == null || !passwordEncoder.matches(currentRaw, admin.getPassword())) {
            throw new IllegalArgumentException("現在のパスワードが正しくありません");
        }
        administratorRepository.updatePassword(mailAddress, passwordEncoder.encode(newRaw));
        log.info("パスワードを変更しました mail={}", mailAddress);
    }
}
