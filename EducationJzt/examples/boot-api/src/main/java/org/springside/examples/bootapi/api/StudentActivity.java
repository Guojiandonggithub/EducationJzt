package org.springside.examples.bootapi.api;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.dto.XbClassDto;
import org.springside.examples.bootapi.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学员、班级、教室
 */
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

	@Autowired
	public XbCoursePresetService xbCoursePresetService;

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
		List<SysOrgans> organsList = organsService.getOrgansList();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		List<XbStudent> xbStudentList = studentService.getXbStudentList(pageable,searhMap).getContent();
		if(organsList.size()>0){
			searhMap.put("EQ_organId",organsList.get(0).id);
		}
		searhMap.put("EQ_sysRole.roleName","销售员");
		Page<SysEmployee> employeePage = employeeService.getAccountList(pageable,searhMap);
		/*model.addAttribute("xbXbStudent",student);*/
		model.addAttribute("xbStudentList",xbStudentList);
		model.addAttribute("organsList",organsList);
		model.addAttribute("flag","1");
		model.addAttribute("sysEmployee",sysEmployee);
		model.addAttribute("xbStudent",new XbStudent());
		model.addAttribute("employeeList",employeePage.getContent());
		return "enroll";
	}

    @RequestMapping("/checkClassRoom")
    public void checkClassRoom(@RequestParam(required = false) String name,HttpServletResponse resp) {
        Map<String, Object> map  =  new HashMap<>();
        try {
            String code = "1000";
            XbClassroom xbClassroom = studentService.checkClassroomName(name);
            if(null!=xbClassroom){
                code = "1001";
            }
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("code",code);
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
        }
    }

    @RequestMapping("/checkStudentName")
    public void checkStudentName(@RequestParam(required = false) String name,@RequestParam(required = false) String phone,HttpServletResponse resp) {
        Map<String, Object> map  =  new HashMap<>();
        try {
            String code = "1000";
			XbStudent xbStudent = studentService.checkStudentName(name,phone);
            if(null!=xbStudent){
                code = "1001";
            }
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("code",code);
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
        }
    }

    @RequestMapping("/checkClassesName")
    public void checkClassesName(@RequestParam(required = false) String name,@RequestParam(required = false) String teacherId,@RequestParam(required = false) String organId,@RequestParam(required = false) String courseId,HttpServletResponse resp) {
        Map<String, Object> map  =  new HashMap<>();
        try {
            String code = "1000";
			XbClass xbClass = studentService.checkClassesName(name,organId,courseId,teacherId);
            if(null!=xbClass){
                code = "1001";
            }
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("code",code);
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
        }
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
	public String getXbStudentList(@RequestParam(required = false) String data,ModelMap model, Pageable pageable){
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
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,searhMap);
		Map<String,Object> studentMap = new HashMap<>();
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,studentMap);
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("xbStudentsPage",xbStudentsPage);
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("studentcurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
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
	public String getXbClassroomList(@RequestParam(required = false) String room,ModelMap model, Pageable pageable){
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);
		Map<String,Object> searhMap = new HashMap<>();
		Pageable pageables = new PageRequest(0, 20);
		Pageable pageable1;
		Pageable pageable2;
		if(room=="1"){
			pageable1 = pageables;
			pageable2 = pageable;
		}else{
			pageable2 = pageables;
			pageable1 = pageable;
		}
		Page<XbClassroom> xbClassroomPage = studentService.getXbClassroomList(pageable1,searhMap);
		Page<XbClass> xbClassPage = studentService.getXbClassList(pageable2,searhMap);
		model.addAttribute("xbClassroomPage",xbClassroomPage);
		model.addAttribute("xbClassPage",xbClassPage);
		model.addAttribute("roomcurrentzise",xbClassroomPage.getSize());
		model.addAttribute("classcurrentzise",xbClassPage.getSize());
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
		String organId = "";
		XbClass xbClass = new XbClass();
		List<SysOrgans> organsList = organsService.getOrgansList();
		if(null!=id){
			xbClass = studentService.getXbClass(id);
			organId = xbClass.organId;
		}else{
			if(organsList.size()>0){
				organId = organsList.get(0).id;
			}
		}
		Map<String,Object> searhMap = new HashMap<>();
		Map<String,Object> roomsearhMap = new HashMap<>();
		searhMap.put("EQ_organIds",organId);
		List<XbCourse> xbCoursePage = xbCourseService.findAllDataByOrganId(organId);
		//List<XbCourse> xbCoursePage = xbCourseService.getXbCourseAllList(searhMap);
		//List<XbCoursePreset> xbCoursePage = xbCoursePresetService.getXbCoursePresets(searhMap);
		Map<String,Object> ygsearhMap = new HashMap<>();
		ygsearhMap.put("EQ_isAttendClass","0");
		ygsearhMap.put("EQ_organId",organId);
		List<SysEmployee> employeeList = employeeService.getAccountAllList(ygsearhMap);
		roomsearhMap.put("EQ_organId",organId);
		List<XbClassroom> xbClassroomList = studentService.getXbClassroomList(pageable,roomsearhMap).getContent();
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("xbClassroomList",xbClassroomList);
		model.addAttribute("employeeList",employeeList);
		model.addAttribute("xbCourseList",xbCoursePage);
		model.addAttribute("organsList",organsList);
		return "newClass";
	}

	/*
	 * 跳转到班级
	 * @return
	 */
	@RequestMapping("/getXbClassJl")
	public void getXbClassJl(@RequestParam(required = false) String organId,@RequestParam(required = false) String id,HttpServletResponse resp, ModelMap model, Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		Map<String,Object> roomsearhMap = new HashMap<>();
		searhMap.put("EQ_organIds",organId);
		searhMap.put("EQ_xbCourse.state","0");
		List<XbCourse> xbCoursePage = xbCourseService.findAllDataByOrganId(organId);
		//List<XbCoursePreset> xbCoursePage = xbCoursePresetService.getXbCoursePresets(searhMap);
		roomsearhMap.put("EQ_organId",organId);
		List<XbClassroom> xbClassroomList = studentService.getXbClassroomList(pageable,roomsearhMap).getContent();
		roomsearhMap.put("EQ_isAttendClass",0);
		List<SysEmployee> employeeList = employeeService.getAccountAllList(roomsearhMap);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status","0");
		jsonObject.put("xbClassroomList",com.alibaba.fastjson.JSONObject.toJSON(xbClassroomList));
		jsonObject.put("xbCourseList",com.alibaba.fastjson.JSONObject.toJSON(xbCoursePage));
		jsonObject.put("employeeList",com.alibaba.fastjson.JSONObject.toJSON(employeeList));
		logger.info("新建科目返回json参数="+jsonObject.toString());
		resp.setContentType("text/html;charset=UTF-8");
		try {
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void deleteclass(@PathVariable String id, HttpServletResponse resp,Pageable pageable){

		Map<String, Object> map  =  new HashMap<>();
		try {
            Map<String,Object> xbClassearhMap = new HashMap<>();
            xbClassearhMap.put("EQ_classId",id);
            JSONObject jsonObject = new JSONObject();
            Page<XbStudentRelation> xbStudentRelationPage = studentService.getXbStudentRelationList(pageable,xbClassearhMap);
            if(xbStudentRelationPage.getContent().size()>0){
                jsonObject.put("status","0");
                jsonObject.put("msg", "有学员报名不能删除该班级！");
            }else{
                studentService.deleteClass(id);
                jsonObject.put("status","1");
                jsonObject.put("msg", "删除成功");
            }
			logger.info("删除返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/finish/class/{id}")
	public void finishclass(@PathVariable String id, HttpServletResponse resp,Pageable pageable){

		Map<String, Object> map  =  new HashMap<>();
		try {
            JSONObject jsonObject = new JSONObject();
			studentService.finishClass(id);
			jsonObject.put("status","1");
			jsonObject.put("msg", "同步结束成功");
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}
	@RequestMapping("/save/enroll")
	public void saveOrgans(@RequestParam String studentEntity,@RequestParam String xbStudentRelation, HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			XbStudent xbStudent = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbStudent.class);
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			List<XbStudentRelation> xbStudentRelationList = com.alibaba.fastjson.JSONArray.parseArray(xbStudentRelation,XbStudentRelation.class);
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
			xbSupplementFee.studentId = xbStudent.id;
			String content = "";
			for (XbStudentRelation studentRelation : xbStudentRelationList) {
				studentRelation.studentId = xbStudent.id;
				XbCourse xbCourse = xbCourseService.findById(studentRelation.courseId);
				content = content+xbCourse.courseName +",";
				studentService.saveXbStudentRelation(studentRelation);
			}
			if(content.endsWith(",")){
				content = content.substring(0,content.length()-1);
			}
			xbSupplementFee.remarks = content;
			xbSupplementFee.type = "0";//报名
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
			xbSupplementFee.handlePerson = sysEmployee.employeeName;
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
		model.addAttribute("feecurrentzise",XbSupplementFeePage.getSize());
		return "management";
	}

	/**
	 * 获取班级列表
	 * @return
	 */
	@RequestMapping("/getClassList")
	public String getClassList(@RequestParam(required = false) String orginId,@RequestParam(required = false) String classesName,ModelMap model, Pageable pageable){
		List<SysOrgans> organsList = organsService.getOrgansList();
		if(organsList.size()>0){
			Map<String,Object> xbCoursesearhMap = new HashMap<>();
			if(null==orginId&&!orginId.equals("")){
				orginId = organsList.get(0).id;
			}
			xbCoursesearhMap.put("EQ_organId",orginId);
			if(null!=classesName&&!classesName.equals("")){
				xbCoursesearhMap.put("LIKE_className",classesName);
			}
			Page<XbClass> xbClassPage = studentService.getXbClassList(pageable,xbCoursesearhMap);
            List<XbClass> xbClassList = xbClassPage.getContent();
            List<XbClass> xbClassPages = new ArrayList<>();
            for (int i = 0; i < xbClassList.size(); i++) {
                Map<String,Object> xbClassearhMap = new HashMap<>();
                xbClassearhMap.put("EQ_classId",xbClassList.get(i).id);
                Page<XbStudentRelation> xbStudentRelationPage = studentService.getXbStudentRelationList(pageable,xbClassearhMap);
                if(xbStudentRelationPage.getTotalElements()<xbClassList.get(i).studentNum){
                    xbClassPages.add(xbClassList.get(i));
					String courseId = xbClassList.get(i).courseId;
					String organId = xbClassList.get(i).organId;
					Map<String,Object> searchMap = new HashMap<>();
					searchMap.put("EQ_courseId",courseId);
					searchMap.put("EQ_organIds",organId);
					searchMap.put("EQ_deleteStatus","1");
					List<XbCoursePreset> xbCourseList = xbCoursePresetService.getXbCoursePresets(searchMap);
					xbClassList.get(i).xbCoursePresetList.addAll(xbCourseList);
                }
            }
			model.addAttribute("xbClassPage",xbClassPages);
		}
		return "enroll::classlist";
	}

	/**
	 * 获取课程列表
	 * @return
	 */
	@RequestMapping("/getCourseList")
	public String getCourseList(@RequestParam(required = false) String orginId,@RequestParam(required = false) String courseName,ModelMap model, Pageable pageable){
		List<SysOrgans> organsList = organsService.getOrgansList();
		if(organsList.size()>0){
			Map<String,Object> xbCoursesearhMap = new HashMap<>();
			if(null==orginId||orginId.equals("")){
				orginId = organsList.get(0).id;
			}
			xbCoursesearhMap.put("EQ_organIds",orginId);
			if(null!=courseName&&!courseName.equals("")){
				xbCoursesearhMap.put("LIKE_xbCourse.courseName",courseName);
			}
			Page<XbCoursePreset> xbCoursePage = xbCoursePresetService.getXbCoursePresetList(pageable,xbCoursesearhMap);
			model.addAttribute("xbCoursePage",xbCoursePage);
		}
		return "enroll::courselist";
	}

	/**
	 * 选择课程
	 * @return
	 */
	@RequestMapping("/chooseCourse")
	public String chooseCourse(@RequestParam(required = false) String courseIds,ModelMap model, Pageable pageable){
		List<XbCoursePreset> xbCourseList = new ArrayList<>();
		BigDecimal money = new BigDecimal(0);
			if(null!=courseIds&&!courseIds.equals("")){
				String[] str = courseIds.split(",");
				for (int i = 0; i < str.length; i++) {
					XbCoursePreset xbCoursePreset = new XbCoursePreset();
					xbCoursePreset = xbCoursePresetService.getXbCoursePreset(str[i]);
					Map<String,Object> xbClasssearhMap = new HashMap<>();
					xbClasssearhMap.put("EQ_courseId",xbCoursePreset.getCourseId());
					Page<XbClass> classPage = studentService.getXbClassList(pageable,xbClasssearhMap);
					xbCoursePreset.setXbClassList(classPage.getContent());
					BigDecimal totalmoney = xbCoursePreset.money;//每个课程总金额
					if(xbCoursePreset.xbCourse.chargingMode.equals("0")){
						totalmoney = xbCoursePreset.money.multiply(new BigDecimal(xbCoursePreset.periodNum));
						xbCoursePreset.money = totalmoney;
					}
					money = money.add(totalmoney);
					xbCourseList.add(xbCoursePreset);
				}
			}
			model.addAttribute("xbCourseLists",xbCourseList);
			model.addAttribute("money",money);
			model.addAttribute("organName",xbCourseList.get(0).sysorgans.organName);
		return "enroll::baoming";
	}

	/**
	 * 选择班级
	 * @return
	 */
	@RequestMapping("/chooseClass")
	public String chooseClass(@RequestParam(required = false) String xbClassparams,ModelMap model, Pageable pageable){
		List<XbCoursePreset> xbCoursePresetList = new ArrayList<>();
		List<XbClassDto> xbStudentRelationList = com.alibaba.fastjson.JSONArray.parseArray(xbClassparams,XbClassDto.class);
		BigDecimal money = new BigDecimal(0);
			if(xbStudentRelationList.size()>0){
				for (int i = 0; i < xbStudentRelationList.size(); i++) {
					List<XbClass> classList = new ArrayList<>();
					XbClass xbClass = studentService.getXbClass(xbStudentRelationList.get(i).ids);
					classList.add(xbClass);
					Map<String,Object> xbClasssearhMap = new HashMap<>();
					xbClasssearhMap.put("EQ_courseId",xbClass.courseId);
					xbClasssearhMap.put("EQ_organIds",xbClass.organId);
					List<XbCoursePreset> xbCourseList = xbCoursePresetService.getXbCoursePresets(xbClasssearhMap);
					for (int j = 0; j < xbCourseList.size(); j++) {
						if(xbCourseList.get(j).getId().equals(xbStudentRelationList.get(i).indexs)){
							xbCourseList.get(j).setXbClassList(classList);
							BigDecimal totalmoney = xbCourseList.get(j).money;//每个课程总金额
							if(xbCourseList.get(j).xbCourse.chargingMode.equals("0")){
								totalmoney = xbCourseList.get(j).money.multiply(new BigDecimal(xbCourseList.get(j).periodNum));
								xbCourseList.get(j).money = totalmoney;
							}
							money = money.add(totalmoney);
							xbCoursePresetList.add(xbCourseList.get(j));
						}
					}
				}
			}
			model.addAttribute("xbCourseLists",xbCoursePresetList);
			model.addAttribute("money",money);
			model.addAttribute("organName",xbCoursePresetList.get(0).sysorgans.organName);
		return "enroll::baoming";
	}

	/**
	 * 移除布局
	 * @return
	 */
	@RequestMapping("/removechoose")
	public String removechoose(ModelMap model, Pageable pageable){
		List<XbCoursePreset> xbCoursePresetList = new ArrayList<>();
		model.addAttribute("xbCourseLists",xbCoursePresetList);
		model.addAttribute("money","");
		model.addAttribute("organName","");
		return "enroll::baoming";
	}


	/**
	 * 跳转到查询欠费学员
	 * @return
	 */
	@RequestMapping("/getQianfeiList")
	public String getQianfeiList(@RequestParam(required = false) String data,ModelMap model, Pageable pageable){
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
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		searhMap.put("GT_xbStudent.paymentMoney","0");
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,searhMap);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put( "GT_paymentMoney","0");
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,studentMap);
		model.addAttribute("xbqianfeiPage",xbStudentPage);
		model.addAttribute("xbStudentsPage",xbStudentsPage);
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("qianfeicurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		return "student::qianfei";
	}

	/**
	 * 跳转到查询到期学员
	 * @return
	 */
	@RequestMapping("/getexpiryStulist")
	public String getexpiryStulist(@RequestParam(required = false) String data,ModelMap model, Pageable pageable){
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
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		searhMap.put("EQ_periodNum","0");
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,searhMap);
		model.addAttribute("expiryStuPage",xbStudentPage);
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("expirycurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		return "student::expiryStu";
	}

	/**
	 * 跳转到查询学员
	 * @return
	 */
	@RequestMapping("/getStudentList")
	public String getStudentList(@RequestParam(required = false) String studentName,ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentName&&!studentName.equals("")){
			resultMap.put("LIKE_studentName",studentName);
		}
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,resultMap);
		model.addAttribute("xbStudentPage",xbStudentsPage);
		return "stopClass::studentList";
	}

	/*
	 * 跳转到班级
	 * @return
	 */
	@RequestMapping("/chooseStudent")
	public void chooseStudent(@RequestParam(required = false) String studentId,HttpServletResponse resp, ModelMap model, Pageable pageable) {
		JSONObject jsonObject = new JSONObject();
		Map<String, Object> searhMap = new HashMap<>();
		try {
			searhMap.put("EQ_studentId", studentId);
			Page<XbStudentRelation> xbCoursePage = studentService.getXbStudentRelationList(pageable, searhMap);
			jsonObject.put("msg", "新建科目成功");
			jsonObject.put("status", "0");
			jsonObject.put("xbCoursePage", com.alibaba.fastjson.JSONObject.toJSON(xbCoursePage.getContent()));
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	/*
	 * 跳转到班级详情
	 * @return
	 */
	@RequestMapping("/classDetail")
	public String classDetail(@RequestParam(required = false) String classId,ModelMap model,Pageable pageable){
		XbClass xbClass = studentService.getXbClass(classId);
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_classId",classId);
		Page<XbStudentRelation> xbStudentRelationPage = studentService.getXbStudentRelationList(pageable,searchParams);
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("xbStudentRelationPage",xbStudentRelationPage);
		return "manageClass";
	}

	/*
	 * 跳转到详情
	 * @return
	 */
	@RequestMapping("/studentDetail")
	public String studentDetail(@RequestParam(required = false) String studentId,ModelMap model,Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String, Object> searchParams = new HashMap<>();
		searchParams.put("EQ_studentId",studentId);
		Page<XbStudentRelation> xbStudentRelationPage = studentService.getXbStudentRelationList(pageable,searchParams);
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbStudentRelationPage",xbStudentRelationPage);//课程列表
		Page<XbSupplementFee> XbSupplementFeePage = studentService.getXbSupplementFeeList(pageable,searchParams);
		model.addAttribute("XbSupplementFeePage",XbSupplementFeePage);//订单列表
		model.addAttribute("feecurrentzise",XbSupplementFeePage.getSize());
		Page<XbRecordClass> xbRecordClassPage = studentService.getRecordClassPage(pageable,searchParams);
		model.addAttribute("xbRecordClassPage",xbRecordClassPage);//上课记录
		model.addAttribute("accordingcurrentzise",xbRecordClassPage.getSize());
		return "stuinfoDetail";
	}
}
