package org.springside.examples.bootapi.api;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysEmployeeSub;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.domain.SysRole;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/accounts")
public class AccountActivity {

	private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);

	@Autowired
	private EmployeeService accountService;

	@Autowired
	private OrgansService organsService;

	@Autowired
	private RoleService roleService;


	@PostMapping("/save/organs")
	public void saveOrgans(@RequestBody SysOrgans organs,HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			SysOrgans organs1 = organsService.register(organs);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(organs1));
			jsonObject.put("msg", "机构编辑成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("application/x-www-form-urlencoded");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	/**
	 * 跳转到组织机构列表
	 * @return
	 */
	@RequestMapping("/getOrgansList")
	public String getOrgansList(ModelMap model,@PageableDefault(sort = { "layOrder" }, direction = Sort.Direction.ASC)Pageable pageable){
		Page<SysOrgans> organsPage = organsService.getOrgansList(pageable);
		model.addAttribute("organsPage",organsPage);
		model.addAttribute("currentzise",organsPage.getSize());
		//model.addAttribute("currentnumber",organsPage.getNumber()+2);
		return "organization";
	}

	@RequestMapping("/hello")
	public String hello() {
		return "login";
	}

	@RequestMapping("/loginOut")
	public String loginOut(HttpServletRequest request) {
		accountService.logout(request);
		return "login";
	}

	@RequestMapping("/login")
	public void login(HttpServletRequest request, @RequestParam String userName, @RequestParam String password, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			String msg = accountService.login(userName,password,request);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg",msg);
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/checkOrganName")
	public void checkOrganName(@RequestParam String name, HttpServletResponse resp){
		try {
			String code = "1000";
			SysOrgans sysOrgans = organsService.checkOrganName(name);
			if(null!=sysOrgans){
				code = "1001";
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("code",code);
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/checkUserName")
	public void checkUserName(@RequestParam String name, HttpServletResponse resp){
		try {
			String code = "1000";
			SysEmployee sysEmployee = accountService.checkUserName(name);
			if(null!=sysEmployee){
				code = "1001";
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("code",code);
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/delete/organs")
	public void removeCourseType(@RequestParam String id, HttpServletResponse resp){

		Map<String, Object> map  =  new HashMap<>();
		try {
			organsService.delete(id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "机构删除成功");
			logger.info("机构删除返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/delete/employee/{id}")
	public void employee(@PathVariable String id, HttpServletResponse resp){
		Map<String, Object> map  =  new HashMap<>();
		try {
			accountService.delete(id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "用户删除成功");
			logger.info("用户删除返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	/*
	 * 跳转到员工列表
	 * @return
	 */
	@RequestMapping("/getEmployeeList")
	public String getEmployeeList(@RequestParam(required=false) String data, ModelMap model,Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,searhMap.getClass());
		}
		String isAttendClass = (String)resultMap.get("isAttendClass");
		String organId = (String)resultMap.get("organId");
		String type = (String)resultMap.get("type");
		String nameormobile = (String)resultMap.get("nameormobile");
		if(null==isAttendClass){
			isAttendClass = "2";
		}else if(!isAttendClass.equals("2")){
			searhMap.put("EQ_isAttendClass",isAttendClass);
		}
		if(null==organId){
			organId = "0";
		}else if(!organId.equals("0")){
			searhMap.put("EQ_organId",organId);
		}
		if(null==type){
			type = "AZ";
		}
		if(null!=nameormobile&&!nameormobile.equals("")){
			if(type.equals("AZ")){
				searhMap.put("LIKE_employeeName",nameormobile);
			}else{
				searhMap.put("LIKE_mobilePhone",nameormobile);
			}
		}
		Page<SysEmployee> employeePage = null;
		try {
			searhMap.put("EQ_deleteStatus","1");
			employeePage = accountService.getAccountList(pageable,searhMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		model.addAttribute("employeePage",employeePage);
		model.addAttribute("currentzise",employeePage.getSize());
		model.addAttribute("organsList",organsList);
		model.addAttribute("organId",organId);
		model.addAttribute("isAttendClass",isAttendClass);
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		return "newStudent";
	}

	/*
	 * 跳转到员工详情
	 * @return
	 */
	@RequestMapping("/getEmployee")
	public String getEmployee(@RequestParam String id,ModelMap model){
		SysEmployee employee = accountService.getSysEmployee(id);
		SysEmployeeSub employeeSub = accountService.getSysEmployeeSub(id);
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Map<String, Object> searchParams = new HashMap<>();
		List<SysRole> sysRolePage = roleService.getRoleListPage(searchParams);
		model.addAttribute("organsList",organsList);
		model.addAttribute("employee",employee);
		model.addAttribute("employeeSub",employeeSub);
		model.addAttribute("sysRolePage",sysRolePage);
		return "employee";
	}

	@RequestMapping("/getEmployeSession")
	public void getEmployeSession(HttpServletRequest request,HttpServletResponse resp){

		Map<String, Object> map  =  new HashMap<>();
		try {
			SysEmployee employee = (SysEmployee) request.getSession().getAttribute("sysEmployee");
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("organName",employee.sysOrgans.organName);
			jsonObject.put("userName", employee.employeeName);
			logger.info("机构删除返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/save/employee")
	public void saveOrgans(@RequestParam String employeeEntity, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			SysEmployee sysEmployee = com.alibaba.fastjson.JSONObject.parseObject(employeeEntity,SysEmployee.class);
			SysEmployeeSub sysEmployeeSub = com.alibaba.fastjson.JSONObject.parseObject(employeeEntity,SysEmployeeSub.class);
			sysEmployee = accountService.register(sysEmployee);
			sysEmployeeSub.userId = sysEmployee.id;
			sysEmployeeSub.id = sysEmployeeSub.employeeSubid;
			accountService.registerSub(sysEmployeeSub);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "员工编辑成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	/**
	 * 跳转到角色列表
	 * @return
	 */
	@RequestMapping("/getRoleList")
	public String getRoleList(ModelMap model,Pageable pageable){
		Map<String,Object> serchMap = new HashMap<>();
		Page<SysRole> rolePage = roleService.getRoleList(pageable,serchMap);
		model.addAttribute("rolePage",rolePage);
		return "role";
	}

	@PostMapping("/save/role")
	public void saveRole(@RequestBody SysRole role,HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			SysRole sysRole = roleService.saveRole(role);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(sysRole));
			jsonObject.put("msg", "编辑成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("application/x-www-form-urlencoded");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/delete/role")
	public void removeRole(@RequestParam String id, HttpServletResponse resp){

		Map<String, Object> map  =  new HashMap<>();
		try {
			roleService.delete(id);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "删除成功");
			logger.info("删除返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

}
