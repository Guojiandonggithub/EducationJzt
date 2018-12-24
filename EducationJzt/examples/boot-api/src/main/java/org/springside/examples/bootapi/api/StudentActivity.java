package org.springside.examples.bootapi.api;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.XbCourseService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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

	@Autowired
	public XbCourseService xbCourseService;

	/*
	 * 跳转到学员
	 * @return
	 */
	@RequestMapping("/getStudent")
	public String getStudent(@RequestParam(required = false) String id, ModelMap model, Pageable pageable){
		/*XbStudent student = new XbStudent();XbStudentRelation
		if(null!=id){
			student = studentService.getXbStudent(id);
		}*/
		Map<String,Object> searhMap = new HashMap<>();
		Page<XbClass> classPage = studentService.getXbClassList(pageable,searhMap);
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		List<XbStudent> xbStudentList = studentService.getXbStudentList(pageable,searhMap).getContent();
		/*model.addAttribute("xbXbStudent",student);*/
		model.addAttribute("xbStudentList",xbStudentList);
		model.addAttribute("organsList",organsList);
		model.addAttribute("classPage",classPage);
		return "enroll";
	}



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

	@RequestMapping("/class/{id}")
	public void getclassinfo(@PathVariable String id, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		XbClass class1 = studentService.getXbClass(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("className",class1.className);
		jsonObject.put("courseName",class1.xbCourse.courseName);
		jsonObject.put("courseId",class1.xbCourse.id);
		jsonObject.put("tuitionFee",class1.xbCourse.tuitionFee);
		jsonObject.put("organName",class1.sysOrgans.organName);
		//jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(class1));
		jsonObject.put("msg", "编辑成功");
		logger.info("编辑机构返回json参数="+jsonObject.toString());
		HttpServletUtil.reponseWriter(jsonObject,resp);
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
	 * 跳转到查询学员
	 * @return
	 */
	@RequestMapping("/getXbStudentList")
	public String getXbStudentList(@RequestParam(required = false) String room,ModelMap model, Pageable pageable){
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);
		Map<String,Object> searhMap = new HashMap<>();
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,searhMap);
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("roomcurrentzise",xbStudentPage.getSize());
		return "student";
	}

	/**
	 * 跳转到一对一
	 * @return
	 */
	@RequestMapping("/getOneToOneList")
	public String getOneToOneList(@RequestParam(required = false) String room,ModelMap model, Pageable pageable){
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);
		Map<String,Object> searhMap = new HashMap<>();
		searhMap.put("EQ_xbCourse.type","1");
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,searhMap);
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("roomcurrentzise",xbStudentPage.getSize());
		return "oneToOne";
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
		List<SysOrgans> organsList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		Map<String,Object> xbCoursesearhMap = new HashMap<>();
		//xbCoursesearhMap.put("IN_openingType",organsList.get(0).id);
		Page<XbCourse> xbCoursePage = xbCourseService.getXbCourseList(pageable,xbCoursesearhMap);
		List<SysEmployee> employeeList = employeeService.getAccountList(pageable,searhMap).getContent();
		List<XbClassroom> xbClassroomList = studentService.getXbClassroomList(pageable,searhMap).getContent();
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("xbClassroomList",xbClassroomList);
		model.addAttribute("employeeList",employeeList);
		model.addAttribute("xbCourseList",xbCoursePage.getContent());
		model.addAttribute("organsList",organsList);
		return "newClass";
	}

	@RequestMapping("/delete/classroom/{id}")
	public void deleteclassroom(@PathVariable String id, HttpServletResponse resp){

		Map<String, Object> map  =  new HashMap<>();
		try {
			studentService.delete(id);
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

	@RequestMapping("/delete/class/{id}")
	public void deleteclass(@PathVariable String id, HttpServletResponse resp){

		Map<String, Object> map  =  new HashMap<>();
		try {
			studentService.deleteClass(id);
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
	@RequestMapping("/save/enroll")
	public void saveOrgans(@RequestParam String studentEntity, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			XbStudent xbStudent = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbStudent.class);
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			XbStudentRelation xbStudentRelation = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbStudentRelation.class);
			BigDecimal su = xbStudent.paymentMoney.subtract(xbStudent.surplusMoney);
			BigDecimal pay = xbStudent.surplusMoney.subtract(xbStudent.paymentMoney);
			int r=su.compareTo(BigDecimal.ZERO); //和0，Zero比较
			int r2=pay.compareTo(BigDecimal.ZERO); //和0，Zero比较
			if(r==-1){//小于
				su = new BigDecimal(0);
			}
			if(r2==-1){//小于
				pay = new BigDecimal(0);
			}
			xbStudent.surplusMoney = su;
			xbStudent.paymentMoney = pay;
			xbStudent = studentService.saveXbStudent(xbStudent);
			xbStudentRelation.studentId = xbStudent.id;
			xbSupplementFee.studentId = xbStudent.id;
			xbStudentRelation.id = xbStudentRelation.relationId;
			studentService.saveXbStudentRelation(xbStudentRelation);
			studentService.saveXbSupplementFee(xbSupplementFee);
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
	 * 跳转到办理中心
	 * @return
	 */
	@RequestMapping("/getManagementList")
	public String getManagementList(@RequestParam(required = false) String room,ModelMap model, Pageable pageable){
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);
		Map<String,Object> searhMap = new HashMap<>();
		Page<XbSupplementFee> XbSupplementFeePage = studentService.getXbSupplementFeeList(pageable,searhMap);
		model.addAttribute("XbSupplementFeePage",XbSupplementFeePage);
		model.addAttribute("roomcurrentzise",XbSupplementFeePage.getSize());
		return "management";
	}

}
