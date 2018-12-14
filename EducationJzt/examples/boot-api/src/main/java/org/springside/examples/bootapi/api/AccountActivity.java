package org.springside.examples.bootapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.examples.bootapi.service.EmployeeService;


@Controller
@RequestMapping(value = "/accounts")
public class AccountActivity {

	private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);

	@Autowired
	private EmployeeService accountService;


	@RequestMapping("/hello")
	public String hello(ModelMap map) {
		map.put("title", "你好");
		return "login";
	}
	@RequestMapping("/index")
	public String index(ModelMap map) {
		map.put("title", "你好");
		return "indexs";
	}
}
