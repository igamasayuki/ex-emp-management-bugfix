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
public class AdministratorServiceTest {

	@InjectMocks
	private AdministratorService administratorService;

	@Mock
	private AdministratorRepository administratorRepository;

	@Test
	void testInsert() {
		Administrator administrator = new Administrator(1, "田中", "tanaka@hoge.com", "tanakapass");
		administratorService.insert(administrator);
		Mockito.verify(administratorRepository).insert(administrator);
	}

	@Test
	void testLogin() {
		when(administratorRepository.findByMailAddressAndPassward("hayashida@hoge.com", "hayashida")).thenReturn(new Administrator(2, "林田", "hayashida@hoge.com", "hayashida"));
		
		Administrator actualAdministrator = administratorService.login("hayashida@hoge.com", "hayashida");
		Administrator expectedAdministrator = new Administrator(2, "林田", "hayashida@hoge.com", "hayashida");
		assertEquals(expectedAdministrator.getId(), actualAdministrator.getId(), "idが一致しません");
		assertEquals(expectedAdministrator.getName(), actualAdministrator.getName(), "nameが一致しません");
		assertEquals(expectedAdministrator.getMailAddress(), actualAdministrator.getMailAddress(),
				"mailAddressが一致しません");
		assertEquals(expectedAdministrator.getPassword(), actualAdministrator.getPassword(), "passwordが一致しません");
	}

}
