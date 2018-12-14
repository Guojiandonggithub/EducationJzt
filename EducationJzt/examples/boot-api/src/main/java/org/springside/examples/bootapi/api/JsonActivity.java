package org.springside.examples.bootapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.repository.Result;
import org.springside.examples.bootapi.service.EmployeeService;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping(value = "/accounts")
public class JsonActivity {

	private static Logger logger = LoggerFactory.getLogger(JsonActivity.class);

	@Autowired
	private EmployeeService accountService;

	@RequestMapping("/login")
	public Result login(@RequestParam String userName, @RequestParam String password) {
		SysEmployee employee = accountService.login(userName,password);
		//map.addAttribute("data", employee);
		Map<String, Object> map  =  new HashMap<>();
		map.put("data", employee);
		map.put("msg", "登录成功");
		return Result.ok(map);
	}
}
