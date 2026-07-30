package com.example.repository.step5_beyond;

import com.example.domain.Administrator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 追加課題(17-1) AdministratorRepository 実 DB テスト（step5）.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BeyondAdministratorRepositoryTest {

    @Autowired
    @Qualifier("beyondAdministratorRepository")
    private BeyondAdministratorRepository administratorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void insertFindAndUpdatePassword() {
        String mail = "step5repo-" + System.nanoTime() + "@test.local";
        Administrator admin = new Administrator();
        admin.setName("テスト管理者");
        admin.setMailAddress(mail);
        admin.setPassword(passwordEncoder.encode("password"));
        administratorRepository.insert(admin);

        Administrator found = administratorRepository.findByMailAddress(mail);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("テスト管理者");
        assertThat(passwordEncoder.matches("password", found.getPassword())).isTrue();

        administratorRepository.updatePassword(mail, passwordEncoder.encode("newpass"));
        Administrator updated = administratorRepository.findByMailAddress(mail);
        assertThat(passwordEncoder.matches("newpass", updated.getPassword())).isTrue();
    }
}
