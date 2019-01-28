package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.RoleService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


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
	@Autowired
	private XbStudentService studentService;

	@RequestMapping("/index")
	public String index(HttpSession session,ModelMap map,@RequestParam(required = false) String data, ModelMap model,
						@PageableDefault(value = 10)Pageable pageable) {
		map.put("title", "你好");
		getXbStudentList(data,model,pageable);
		SysEmployee sysEmployee = (SysEmployee)session.getAttribute("sysEmployee");
		session.setAttribute("userName",sysEmployee.employeeName);
		session.setAttribute("organName",sysEmployee.sysOrgans.organName);
		session.setAttribute("roleName",sysEmployee.sysRole.roleName);
		return "indexs";
	}
	public void getXbStudentList(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,searhMap.getClass());
		}
		String organId = (String)resultMap.get("organId");
		String type = (String)resultMap.get("type");
		String nameormobile = (String)resultMap.get("nameormobile");
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
				searhMap.put("LIKE_xbStudent.studentName",nameormobile);
			}else{
				searhMap.put("LIKE_xbStudent.contactPhone",nameormobile);
			}
		}
		String enrollDateSearch = (String)resultMap.get("enrollDateSearch");
		String enrollDateSearchEnd = (String)resultMap.get("enrollDateSearchEnd");
		Date date = new Date();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotEmpty(enrollDateSearch)){
				date = sdf.parse(enrollDateSearch);
				searhMap.put("EQ_enrollDate",date);
			}
			if(null==enrollDateSearch){
				date = sdf.parse(DateUtil.getDateStr(new Date()));
				enrollDateSearch = DateUtil.getDateStr(new Date());
				searhMap.put("EQ_enrollDate",date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List studentlist = new ArrayList<>();
		if(StringUtils.isEmpty(enrollDateSearch)){
			studentlist =	studentService.getAllStudentListNoDate();
		}else{
			studentlist =	studentService.getAllStudentList(date);
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,searhMap);
		Map<String,Object> studentMap = new HashMap<>();
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("studentlistsize",studentlist.size());
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("studentcurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		model.addAttribute("enrollDateSearch",enrollDateSearch);
		model.addAttribute("enrollDateSearchEnd",enrollDateSearchEnd);
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
