package org.springside.examples.bootapi.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

public class AccountServiceTest {

	@Mock
	private EmployeeService accountService;

	//@InjectMocks
	//private AccountEndPoint userController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		//this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
	}

	@Test
	public void hash() throws Exception {


		//Assert.assertNotNull(result.getModelAndView().getModel().get("user"));

		//Account account = new Account(1);
		//account.password="123";
		//String str = accountService.login("test","123456");
		//System.out.println("token:" + str);
		//accountDao.save(Mockito.any(AccountDao.class));
		//System.out.println("hashPassword:" + AccountService.hashPassword("springside"));
		//Mockito.when(accountDao.findOne(1L)).thenReturn(account);
		//Mockito.any(Account.class);
	}

}
