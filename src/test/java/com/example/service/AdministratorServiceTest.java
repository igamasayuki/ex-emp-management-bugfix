package com.example.service;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * AdministratorServiceクラスのテストクラス.
 *
 * @author rui.inoue
 */
@SpringBootTest
class AdministratorServiceTest {

    @InjectMocks
    private AdministratorService administratorService;

    @Mock
    private AdministratorRepository administratorRepository;

    @Test
    void testInsert(){
        Administrator administrator = new Administrator();
        administrator.setId(1);
        administrator.setName("test name");
        administrator.setMailAddress("test@test");
        administrator.setPassword("test pass");
        administratorService.insert(administrator);

        verify(administratorRepository).insert(administrator);
    }

    @Test
    void testLogin(){
        when(administratorRepository.findByMailAddressAndPassword("test@test", "testPass"))
                .thenReturn(new Administrator(1, "testName", "test@test", "testPass"));

        administratorService.login("test@test", "testPass");

        verify(administratorRepository).findByMailAddressAndPassword("test@test", "testPass");
    }
}