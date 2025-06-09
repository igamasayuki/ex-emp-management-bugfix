package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdministratorServiceTest {

    @Mock
    private AdministratorRepository administratorRepository;

    @InjectMocks
    private AdministratorService administratorService;

    @Test
    void insert() {
        Administrator mockAdministrator = new Administrator();
        Integer mockID = 999;
        String mockName = "MockAdministrator";
        String mockMailAddress = "mock@mock.com";
        String mockPassword = "mockPassword";
        mockAdministrator.setId(mockID);
        mockAdministrator.setName(mockName);
        mockAdministrator.setMailAddress(mockMailAddress);
        mockAdministrator.setPassword(mockPassword);

        administratorService.insert(mockAdministrator);
        verify(administratorRepository,
                times(1)).insert(mockAdministrator);
    }

    /**
     * ログイン成功.
     */
    @Test
    void login_success() {
        Administrator mockAdministrator = new Administrator();
        Integer mockID = 999;
        String mockName = "MockAdministrator";
        String mockMailAddress = "mock@mock.com";
        String mockPassword = "mockPassword";
        mockAdministrator.setId(mockID);
        mockAdministrator.setName(mockName);
        mockAdministrator.setMailAddress(mockMailAddress);
        mockAdministrator.setPassword(mockPassword);

        when(administratorRepository.findByMailAddressAndPassword(mockMailAddress,mockPassword )).thenReturn(mockAdministrator);

        Administrator administrator = administratorService.login(mockMailAddress,mockPassword);
        assertNotNull(administrator);
        assertEquals(administrator.getName(), mockName, "名前：期待された結果と異なります");
    }

    /**
     * パスワード不一致によるログイン失敗
     */
    @Test
    void login_failure_for_password() {
        Administrator mockAdministrator = new Administrator();
        Integer mockID = 999;
        String mockName = "MockAdministrator";
        String mockMailAddress = "mock@mock.com";
        String mockActualPassword = "correct";
        String mockWrongPassword = "wrong";
        mockAdministrator.setId(mockID);
        mockAdministrator.setName(mockName);
        mockAdministrator.setMailAddress(mockMailAddress);
        mockAdministrator.setPassword(mockActualPassword);

        when(administratorRepository.findByMailAddressAndPassword(mockMailAddress,mockActualPassword)).thenReturn(mockAdministrator);

        when(administratorRepository.findByMailAddressAndPassword(mockMailAddress,mockWrongPassword)).thenReturn(null);

        Administrator actualAdministrator = administratorService.login(mockMailAddress, mockActualPassword);

        Administrator wrongAdministrator = administratorService.login(mockMailAddress, mockWrongPassword);

        assertNotNull(actualAdministrator);
        assertNull(wrongAdministrator);
    }
}