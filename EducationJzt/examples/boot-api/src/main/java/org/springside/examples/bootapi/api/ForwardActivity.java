package org.springside.examples.bootapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysEmployeeSub;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.domain.SysRole;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.RoleService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/forward")
public class ForwardActivity {

	private static Logger logger = LoggerFactory.getLogger(ForwardActivity.class);
	@Autowired
	private EmployeeService accountService;
	@Autowired
	private OrgansService organsService;
	@Autowired
	private RoleService roleService;

	@RequestMapping("/index")
	public String index(HttpSession session,ModelMap map) {
		map.put("title", "你好");

		SysEmployee sysEmployee = (SysEmployee)session.getAttribute("sysEmployee");
		session.setAttribute("userName",sysEmployee.employeeName);
		session.setAttribute("organName",sysEmployee.sysOrgans.organName);
		session.setAttribute("roleName",sysEmployee.sysRole.roleName);
		return "indexs";
	}

	@RequestMapping("/organization")
	public String organization(ModelMap model,Pageable pageable) {
		logger.info("跳转到课程");
		Page<SysOrgans> organsPage = organsService.getOrgansList(pageable);
		logger.info("查询到机构organsPage="+organsPage);
		model.addAttribute("organsPage",organsPage);
		return "organization";
	}

	@RequestMapping("/management")
	public String management() {
		return "management";
	}

	@RequestMapping("/employee")
	public String employee(Pageable pageable, ModelMap model) {
		Map<String, Object> searchParams = new HashMap<>();
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<SysRole> sysRolePage = roleService.getRoleList(pageable,searchParams);
		logger.info("查询到机构organsList="+organsList);
		model.addAttribute("organsList",organsList);
		model.addAttribute("employee",new SysEmployee());
		model.addAttribute("employeeSub",new SysEmployeeSub());
		model.addAttribute("sysRolePage",sysRolePage.getContent());
		return "employee";
	}

	@RequestMapping("/student")
	public String student() {
		return "student";
	}

	@RequestMapping("/classes")
	public String classes() {
		return "classes";
	}

	@RequestMapping("/oneToOne")
	public String oneToOne() {
		return "oneToOne";
	}

	@RequestMapping("/courseArray")
	public String courseArray() {
		return "courseArray";
	}

	@RequestMapping("/lessonTable")
	public String lessonTable() {
		return "lessonTable";
	}

	@RequestMapping("/attendClass")
	public String attendClass() {
		return "attendClass";
	}

	@RequestMapping("/course")
	public String course() {
		return "course";
	}

	@RequestMapping("/oneEdit")
	public String oneEdit() {
		return "oneEdit";
	}

	@RequestMapping("/subsidy")
	public String subsidy() {
		return "subsidy";
	}
	@RequestMapping("/stopClass")
	public String stopClass() {
		return "stopClass";
	}
	@RequestMapping("/repeat")
	public String repeat() {
		return "repeat";
	}
}
