package com.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.domain.Administrator;
import com.example.repository.AdministratorRepository;

@SpringBootTest
class AdministratorServiceTest {

	@InjectMocks
	private AdministratorService administratorService;
	
	@Mock
	private AdministratorRepository administratorRepository;
	
	@Test
	void testLogin() {
		when(administratorRepository.findByMailAddressAndPassward("123@sample.com", "123456"))
				.thenReturn(new Administrator(1, "A", "123@sample.com", "123456"));

		Administrator actualAdministrator = administratorService.login("123@sample.com", "123456");
		Administrator expectedAdministrator = new Administrator(1, "A", "123@sample.com", "123456");
		assertEquals(expectedAdministrator.getId(), actualAdministrator.getId(), "idが一致しません");
		assertEquals(expectedAdministrator.getName(), actualAdministrator.getName(), "nameが一致しません");
		assertEquals(expectedAdministrator.getMailAddress(), actualAdministrator.getMailAddress(),
				"mailAddressが一致しません");
		assertEquals(expectedAdministrator.getPassword(), actualAdministrator.getPassword(), "passwordが一致しません");
	}
	
	@Test
	void testInsert() {
		Administrator administrator = new Administrator(1, "A", "123@sample.com", "igaigaiga");
		administratorService.insert(administrator);
		Mockito.verify(administratorRepository).insert(administrator);
	}
	
	@Test
	void testLogin2() {
		administratorService.login("123@sample.com", "123456");

		Mockito.verify(administratorRepository).findByMailAddressAndPassward("123@sample.com", "123456");
	}
}
