package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AdministratorServiceのテスト.
 */
@ExtendWith(MockitoExtension.class)
class AdministratorServiceTest {

    @InjectMocks
    private AdministratorService administratorService;

    @Mock
    private AdministratorRepository administratorRepository;

    @Test
    void insertはRepositoryのinsertを呼び出す() {
        Administrator administrator = new Administrator();

        administratorService.insert(administrator);

        // Serviceの責務はRepositoryへ処理を委譲することなので、呼び出しをverifyで確認する。
        verify(administratorRepository).insert(administrator);
    }

    @Test
    void loginはRepositoryの検索結果を返す() {
        Administrator administrator = new Administrator();
        when(administratorRepository.findByMailAddressAndPassword("user@example.com", "password"))
                .thenReturn(administrator);

        Administrator actual = administratorService.login("user@example.com", "password");

        assertSame(administrator, actual);
    }
}
