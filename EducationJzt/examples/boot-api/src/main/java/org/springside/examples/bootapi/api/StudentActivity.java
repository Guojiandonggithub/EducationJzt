package org.springside.examples.bootapi.api;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.domain.SysEmployee;
import org.springside.examples.bootapi.domain.SysOrgans;
import org.springside.examples.bootapi.domain.XbClass;
import org.springside.examples.bootapi.domain.XbClassroom;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/student")
public class StudentActivity {

	private static Logger logger = LoggerFactory.getLogger(StudentActivity.class);

	@Autowired
	private XbStudentService studentService;

	@Autowired
	private OrgansService organsService;

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/save/classroom")
	public void saveclassroom(@RequestBody XbClassroom classroom, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
					XbClassroom classroom1 = studentService.saveXbClassroom(classroom);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(classroom1));
			jsonObject.put("msg", "编辑成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@PostMapping("/save/xbclass")
	public void savexbclass(@RequestBody XbClass xbclass, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			XbClass class1 = studentService.saveXbClass(xbclass);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "编辑成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	/**
	 * 跳转到查询班级教室
	 * @return
	 */
	@RequestMapping("/getXbClassroomList")
	public String getOrgansList(@RequestParam(required = false) String room,ModelMap model, Pageable pageable){
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);
		Map<String,Object> searhMap = new HashMap<>();
		Page<XbClassroom> xbClassroomPage = studentService.getXbClassroomList(pageable,searhMap);
		Page<XbClass> xbClassPage = studentService.getXbClassList(pageable,searhMap);
		model.addAttribute("xbClassroomPage",xbClassroomPage);
		model.addAttribute("xbClassPage",xbClassPage);
		model.addAttribute("roomcurrentzise",xbClassroomPage.getSize());
		return "classes";
	}

	/*
	 * 跳转到教室
	 * @return
	 */
	@RequestMapping("/getClassroom")
	public String getClassroom(@RequestParam(required = false) String id, ModelMap model){
		XbClassroom xbClassroom = new XbClassroom();
		if(null!=id){
			xbClassroom = studentService.getXbClassroom(id);
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		model.addAttribute("xbClassroom",xbClassroom);
		model.addAttribute("organsList",organsList);
		return "newClassroom";
	}

	/*
	 * 跳转到班级
	 * @return
	 */
	@RequestMapping("/getXbClass")
	public String getClass(@RequestParam(required = false) String id, ModelMap model, Pageable pageable){
		XbClass xbClass = new XbClass();
		if(null!=id){
			xbClass = studentService.getXbClass(id);
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		List<SysEmployee> employeeList = employeeService.getAccountList(pageable,searhMap).getContent();
		List<XbClassroom> xbClassroomList = studentService.getXbClassroomList(pageable,searhMap).getContent();
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("xbClassroomList",xbClassroomList);
		model.addAttribute("employeeList",employeeList);
		model.addAttribute("organsList",organsList);
		return "newClass";
	}

}
