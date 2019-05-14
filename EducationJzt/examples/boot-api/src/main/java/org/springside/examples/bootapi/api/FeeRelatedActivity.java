package org.springside.examples.bootapi.api;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.dto.XbClassesDto;
import org.springside.examples.bootapi.dto.XbCoursePresetDto;
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
 * 费用相关
 */
@Controller
@RequestMapping(value = "/feeRelated")
public class FeeRelatedActivity {

	private static Logger logger = LoggerFactory.getLogger(FeeRelatedActivity.class);

	@Autowired
	private EmployeeService accountService;

	@Autowired
	private OrgansService organsService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	public XbCourseTypeService xbCourseTypeService;

	@Autowired
	private XbStudentService studentService;

	@Autowired
	public XbCoursePresetService xbCoursePresetService;

	@Autowired
	public XbCourseService xbCourseService;


	/**
	 * 跳转到转班
	 * @return
	 */
	@RequestMapping("/changeClass")
	public String changeClass(ModelMap model,Pageable pageable){
		List<SysOrgans> sysOrgansList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		/*if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = accountService.getAccountList(pageable,searhMap);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		model.addAttribute("sysOrgansList",sysOrgansList);
		model.addAttribute("employeeList",employeePage.getContent());
		model.addAttribute("sysEmployee",sysEmployee);
		return "changeClass";
	}

	/**
	 * 跳转到补费
	 * @return
	 */
	@RequestMapping("/subsidy")
	public String subsidy(ModelMap model,Pageable pageable){
		List<SysOrgans> sysOrgansList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		/*if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = accountService.getAccountList(pageable,searhMap);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		model.addAttribute("sysOrgansList",sysOrgansList);
		model.addAttribute("employeeList",employeePage.getContent());
		model.addAttribute("sysEmployee",sysEmployee);
		return "subsidy";
	}

	/**
	 * 跳转到停课
	 * @return
	 */
	@RequestMapping("/stopClass")
	public String stopClass(ModelMap model,Pageable pageable){
		List<SysOrgans> sysOrgansList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		/*if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = accountService.getAccountList(pageable,searhMap);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		model.addAttribute("sysOrgansList",sysOrgansList);
		model.addAttribute("employeeList",employeePage.getContent());
		model.addAttribute("sysEmployee",sysEmployee);
		return "stopClass";
	}

	/**
	 * 跳转到复课
	 * @return
	 */
	@RequestMapping("/repeatClass")
	public String repeatClass(ModelMap model,Pageable pageable){
		List<SysOrgans> sysOrgansList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		/*if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = accountService.getAccountList(pageable,searhMap);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		model.addAttribute("sysOrgansList",sysOrgansList);
		model.addAttribute("employeeList",employeePage.getContent());
		model.addAttribute("sysEmployee",sysEmployee);
		return "repeat";
	}

	/**
	 * 跳转到退费
	 * @return
	 */
	@RequestMapping("/cancelClass")
	public String cancelClass(ModelMap model,Pageable pageable){
		List<SysOrgans> sysOrgansList = organsService.getOrgansList();
		Map<String,Object> searhMap = new HashMap<>();
		/*if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = accountService.getAccountList(pageable,searhMap);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		model.addAttribute("sysOrgansList",sysOrgansList);
		model.addAttribute("employeeList",employeePage.getContent());
		model.addAttribute("sysEmployee",sysEmployee);
		return "cancelClass";
	}


	/**
	 * 跳转到查询学员
	 * @return
	 */
	@RequestMapping("/getStudentList")
	public String getStudentList(@RequestParam(required = false) String studentName, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentName&&!studentName.equals("")){
			resultMap.put("LIKE_studentName",studentName);
		}
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,resultMap);
		model.addAttribute("xbStudentPage",xbStudentsPage);
		return "changeClass::studentList";
	}

	/**
	 * 跳转到查询学员
	 * @return
	 */
	@RequestMapping("/getSubsidyList")
	public String getSubsidyList(@RequestParam(required = false) String studentName, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentName&&!studentName.equals("")){
			resultMap.put("LIKE_studentName",studentName);
		}
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,resultMap);
		model.addAttribute("xbStudentPage",xbStudentsPage);
		return "subsidy::studentList";
	}

	/**
	 * 获取课程列表
	 * @return
	 */
	@RequestMapping("/getCourseList")
	public String getCourseList(@RequestParam(required = false) String orginId,@RequestParam(required = false) String studentId,@RequestParam(required = false) String classId,@RequestParam(required = false) String courseName,ModelMap model, Pageable pageable){
		List<SysOrgans> organsList = organsService.getOrgansList();
		if(organsList.size()>0){
			Map<String,Object> xbCoursesearhMap = new HashMap<>();
			if(null==orginId||orginId.equals("")){
				orginId = organsList.get(0).id;
			}
			if(null!=courseName&&!courseName.equals("")){
				xbCoursesearhMap.put("LIKE_xbCourse.courseName",courseName);
			}
			XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(classId);
			String courseTypeId = xbStudentRelation.xbCourse.courseTypeId;
			orginId = xbStudentRelation.organId;
			xbCoursesearhMap.put("EQ_xbCourse.xbcoursetype.id",courseTypeId);
			xbCoursesearhMap.put("EQ_organIds",orginId);
			Page<XbCoursePreset> xbCoursePage = xbCoursePresetService.getXbCoursePresetList(pageable,xbCoursesearhMap);
			model.addAttribute("xbCoursePage",xbCoursePage);
			model.addAttribute("organsList",organsList);
		}
		return "changeClass::courselist";
	}

	/**
	 * 选择课程
	 * @return
	 */
	@RequestMapping("/chooseCourse")
	public void chooseCourse(@RequestParam(required = false) String courseIds , HttpServletResponse resp){
		BigDecimal moneys = new BigDecimal(0);
		Integer num = 0;
		XbCoursePreset xbCoursePreset = new XbCoursePreset();
		xbCoursePreset = xbCoursePresetService.getXbCoursePreset(courseIds);
		Map<String,Object> xbClasssearhMap = new HashMap<>();
		xbClasssearhMap.put("EQ_courseId",xbCoursePreset.getCourseId());
		List<XbClass> classPage = studentService.findXbClassListAll(xbClasssearhMap);
		XbCoursePresetDto xbCoursePresetDto = new XbCoursePresetDto();
		for (int i = 0; i < classPage.size(); i++) {
			XbClassesDto xbClassesDto = new XbClassesDto();
			xbClassesDto.id = classPage.get(i).id;
			xbClassesDto.className = classPage.get(i).className;
			xbClassesDto.teacherName = classPage.get(i).teacher.employeeName;
			String choosecourseId = xbCoursePreset.id;
			xbCoursePresetDto.choosecourseId = choosecourseId;
			xbCoursePresetDto.courseName = classPage.get(i).xbCourse.courseName;
			xbCoursePresetDto.xbClassList.add(xbClassesDto);
		}
		xbCoursePresetDto.totalPeriodNum = String.valueOf(xbCoursePreset.periodNum);
		XbCourse xbc = xbCourseService.findById(xbCoursePreset.getCourseId());
		if(xbc.chargingMode.equals("0")){
			BigDecimal bde = new BigDecimal(xbCoursePreset.periodNum);
			xbCoursePreset.money=xbCoursePreset.money.multiply(bde);
		}
		xbCoursePresetDto.totalReceivable = String.valueOf(xbCoursePreset.money);
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "查询成功");
			jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(xbCoursePresetDto));
			logger.info("查询返回json参数="+jsonObject.toString());
			resp.setContentType("application/json;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}


	/**
	 * 选择学员
	 * @return
	 */
	@RequestMapping("/chooseStudent")
	public String chooseStudent(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		//studentMap.put("EQ_deleteStatus","1");
		if(null!=classId&&!classId.equals("")){
			studentMap.put("EQ_id",classId);
		}
		XbStudentRelationViewNew xbStudentRelation = new XbStudentRelationViewNew();
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(studentMap);
		String balanceamount = "0.00";
		if(xbStudentPage.size()>0){
			xbStudentRelation = xbStudentPage.get(0);
			BigDecimal receivable = xbStudentRelation.receivable;
			BigDecimal paymentMoney = xbStudent.paymentMoney;
			BigDecimal su = receivable.subtract(paymentMoney);
			balanceamount = su.toString();
		}
		List<XbStudentRelation> xbClassPage = new ArrayList<>();
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage);
		model.addAttribute("xbQbClassList",xbClassPage);
		model.addAttribute("classId","");
		model.addAttribute("xbStudentRelationId",xbStudentRelation.id);
		model.addAttribute("totalReceivable",xbStudentRelation.totalReceivable);
		model.addAttribute("receivable",xbStudentRelation.receivable);
		model.addAttribute("periodNum",xbStudentRelation.periodNum);
		model.addAttribute("balanceamount",balanceamount);
		return "changeClass::changeClassFragment";
	}

	/**
	 * 补费选择学员
	 * @return
	 */
	@RequestMapping("/chooseSubsidyStudent")
	public String chooseSubsidyStudent(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		//studentMap.put("EQ_deleteStatus","1");
		if(null!=classId&&!classId.equals("")){
			studentMap.put("EQ_id",classId);
		}
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(studentMap);
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("xbStudent",xbStudent);
		return "subsidy::changeClassFragment";
	}

	/**
	 * 复课选择学员
	 * @return
	 */
	@RequestMapping("/chooseRepeatStudent")
	public String chooseRepeatStudent(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		//studentMap.put("EQ_deleteStatus","1");
		studentMap.put("EQ_studentStart","1");
		XbStudentRelationViewNew xbStudentRelation = new XbStudentRelationViewNew();
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(studentMap);
		String balanceamount = "0.00";
		if(xbStudentPage.size()>0){
			xbStudentRelation = xbStudentPage.get(0);
			BigDecimal receivable = xbStudentRelation.receivable;
			BigDecimal paymentMoney = xbStudent.paymentMoney;
			BigDecimal su = receivable.subtract(paymentMoney);
			balanceamount = su.toString();
		}
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage);
		model.addAttribute("classId","");
		model.addAttribute("xbStudentRelationId",xbStudentRelation.id);
		model.addAttribute("totalReceivable",xbStudentRelation.totalReceivable);
		model.addAttribute("receivable",xbStudentRelation.receivable);
		model.addAttribute("periodNum",xbStudentRelation.periodNum);
		model.addAttribute("balanceamount",balanceamount);
		return "repeat::changeClassFragment";
	}

	/**
	 * 选择班级
	 * @return
	 */
	@RequestMapping("/chooseClass")
	public String chooseClass(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		studentMap.put("EQ_.deleteStatus","1");
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,studentMap);
		studentMap.put("EQ_id",classId);
		List<XbStudentRelationViewNew> xbRelationList = studentService.getXbRelationList(studentMap);
		XbStudentRelationViewNew xbStudentRelation = new XbStudentRelationViewNew();
		String balanceamount = "0.00";
		if(xbRelationList.size()>0){
			xbStudentRelation = xbRelationList.get(0);
			BigDecimal receivable = xbStudentRelation.receivable;
			BigDecimal paymentMoney = xbStudent.paymentMoney;
			BigDecimal su = receivable.subtract(paymentMoney);
			balanceamount = su.toString();
		}
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage.getContent());
		model.addAttribute("xbStudentRelationId",xbStudentRelation.id);
		model.addAttribute("totalReceivable",xbStudentRelation.totalReceivable);
		model.addAttribute("receivable",xbStudentRelation.receivable);
		model.addAttribute("periodNum",xbStudentRelation.periodNum);
		model.addAttribute("classId",classId);
		model.addAttribute("balanceamount",balanceamount);
		return "changeClass::changeClassFragment";
	}

	/**
	 * 报名选择学员
	 * @return
	 */
	@RequestMapping("/enrollchooseStudent")
	public void enrollchooseStudent(@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable, HttpServletResponse resp){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentId&&!studentId.equals("")){
			resultMap.put("LIKE_studentName",studentId);
		}
		resultMap.put("EQ_id",studentId);
		XbStudent xbStudentold = studentService.getXbStudent(studentId);
		XbStudent xbStudentnew = new XbStudent();
		xbStudentnew.id = xbStudentold.id;
		xbStudentnew.sex = xbStudentold.sex;
		xbStudentnew.studentName = xbStudentold.studentName;
		xbStudentnew.contactPhone = xbStudentold.contactPhone;
		xbStudentnew.advisoryChannel = xbStudentold.advisoryChannel;
		xbStudentnew.contactRelation = xbStudentold.contactRelation;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "查询成功");
			jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(xbStudentnew));
			logger.info("查询返回json参数="+jsonObject.toString());
			resp.setContentType("application/json;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
		//return "changeClass::changeClassFragment";
	}

	/**
	 * 班级报名选择学员
	 * @return
	 */
	@RequestMapping("/enrollchooseStudentByClass")
	public void enrollchooseStudentByClass(@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable, HttpServletResponse resp){
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("EQ_id",studentId);
		XbStudentRelation xbStudentRelationold = studentService.getXbStudentRelation(studentId);
		XbStudentRelation xbStudentRelationnew = new XbStudentRelation();
		xbStudentRelationnew.periodNum = xbStudentRelationold.periodNum;
		xbStudentRelationnew.receivable = xbStudentRelationold.receivable;
		xbStudentRelationnew.id = xbStudentRelationold.id;
		String courseName = xbStudentRelationold.xbCourse.courseName;
		XbStudent xbStudentold = studentService.getXbStudent(xbStudentRelationold.studentId);
		XbStudent xbStudentnew = new XbStudent();
		xbStudentnew.id = xbStudentold.id;
		xbStudentnew.sex = xbStudentold.sex;
		xbStudentnew.studentName = xbStudentold.studentName;
		xbStudentnew.contactPhone = xbStudentold.contactPhone;
		xbStudentnew.advisoryChannel = xbStudentold.advisoryChannel;
		xbStudentnew.contactRelation = xbStudentold.contactRelation;
		xbStudentnew.paymentMoney = xbStudentold.paymentMoney;

		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "查询成功");
			jsonObject.put("courseName", courseName);
			jsonObject.put("data", com.alibaba.fastjson.JSONObject.toJSON(xbStudentnew));
			jsonObject.put("xbStudentRelation", com.alibaba.fastjson.JSONObject.toJSON(xbStudentRelationnew));
			logger.info("查询返回json参数="+jsonObject.toString());
			resp.setContentType("application/json;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
		//return "changeClass::changeClassFragment";
	}

	/**
	 * 获取相同课程类别班级
	 * @return
	 */
	@RequestMapping("/chooseClasses")
	public String chooseClasses(@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentId&&!studentId.equals("")){
			resultMap.put("LIKE_studentName",studentId);
		}
		resultMap.put("EQ_id",studentId);
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,studentMap);
		Map<String,Object> studentMaps = new HashMap<>();
		studentMaps.put("EQ_xbCourse.xbcoursetype.id",studentId);
		Page<XbStudentRelationViewNew> xbClassPage = studentService.getXbStudentRelationViewNewList(pageable,studentMaps);
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage.getContent());
		model.addAttribute("xbQbClassList",xbClassPage.getContent());
		return "changeClass::changeClassFragment";
	}

	/**
	 * 转班
	 * @param studentEntity
	 * @param resp
	 */
	@RequestMapping("/changeClassSave")
	@SystemControllerLog(descrption = "转班",actionType = "1")
	public void changeClassSave(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "1";//转班
			String studentId = xbSupplementFee.studentId;
			String classId = xbSupplementFee.classId;
			String toClassId = xbSupplementFee.toClassId;
			String receivables = xbSupplementFee.receivable;
			String periodNum = xbSupplementFee.periodNum;
			String choosecourseId = xbSupplementFee.choosecourseId;
			XbStudentRelation xbStudentRelations = studentService.getXbStudentRelation(classId);
			xbStudentRelations.periodNum = new BigDecimal(periodNum);
			String classIdStr = xbStudentRelations.classId;
			JSONObject jsonObject = new JSONObject();
			if(null!=classIdStr&&!"".equals(classIdStr)){
				String className = studentService.getXbClass(xbStudentRelations.classId).className;
				xbStudentRelations.studentStart = 2;
				XbCoursePreset xbCoursePreset = xbCoursePresetService.getXbCoursePreset(choosecourseId);
				xbStudentRelations.classId = toClassId;
				XbClass xbClass = studentService.getXbClass(toClassId);
				xbStudentRelations.organId = xbClass.organId;
				xbStudentRelations.courseId = xbClass.courseId;
				xbStudentRelations.studentId = studentId;
				xbSupplementFee.remarks = className+"转到"+xbClass.className;
				xbSupplementFee.surplusMoney = xbStudentRelations.receivable;//应收金额
				xbSupplementFee.paymentMoney = xbStudentRelations.receivable;//实收金额
				studentService.saveXbStudentRelation(xbStudentRelations);
				HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
				SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
				xbSupplementFee.handlePerson = sysEmployee.employeeName;
				studentService.saveXbSupplementFee(xbSupplementFee);
				jsonObject.put("msg", "转班成功!");
				jsonObject.put("status","1");
			}else{
				jsonObject.put("status","0");
				jsonObject.put("msg", "该学员还没有分班!");
			}
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	/**
	 * 复课
	 * @param studentEntity
	 * @param resp
	 */
	@RequestMapping("/rePeatClassSave")
	@SystemControllerLog(descrption = "复课",actionType = "1")
	public void rePeatClassSave(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "4";//复课
			String studentRelationId = xbSupplementFee.studentRelationId;
			XbStudentRelationViewNew xbStudentRelations = studentService.getXbStudentRelationView(studentRelationId);
			XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(studentRelationId);
			xbStudentRelation.studentStart = 0;//在读
			xbStudentRelation.classId= "";
            xbStudentRelation.periodNum = new BigDecimal(xbSupplementFee.periodNum);
			xbSupplementFee.remarks = "对"+xbStudentRelations.sysOrgans.organName+xbStudentRelations.xbCourse.xbcoursetype.courseTypeName+xbStudentRelations.xbCourse.courseName+xbStudentRelations.className+"进行复课";
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
			xbSupplementFee.handlePerson = sysEmployee.employeeName;
			xbSupplementFee.paymentMoney = new BigDecimal("0");
			xbSupplementFee.surplusMoney = new BigDecimal("0");
			studentService.saveXbSupplementFee(xbSupplementFee);
			studentService.saveXbStudentRelation(xbStudentRelation);
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
	 * 补费
	 * @param studentEntity
	 * @param resp
	 */
	@RequestMapping("/saveSubsidy")
	@SystemControllerLog(descrption = "补费",actionType = "1")
	public void saveSubsidy(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "2";//补费
			String studentId = xbSupplementFee.studentId;
			String subsidyMoney = xbSupplementFee.subsidyMoney;
			XbStudent xbStudent = studentService.getXbStudent(studentId);
			BigDecimal subsidyMoneyBd = new BigDecimal(subsidyMoney);
			BigDecimal bd = xbSupplementFee.paymentMoney.add(subsidyMoneyBd);
			if(xbSupplementFee.surplusMoney.compareTo(bd)>0){
				BigDecimal paymentMoney = xbSupplementFee.surplusMoney.subtract(bd);
				xbStudent.paymentMoney = paymentMoney;
				xbStudent.surplusMoney = xbStudent.surplusMoney.subtract(subsidyMoneyBd);
			}else{
				BigDecimal paymentMoney = new BigDecimal("0.00");
				xbStudent.paymentMoney = paymentMoney;
				xbStudent.surplusMoney = xbStudent.surplusMoney.add(bd.subtract(xbSupplementFee.surplusMoney).subtract(subsidyMoneyBd));
			}
			studentService.saveXbStudent(xbStudent);
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
			xbSupplementFee.handlePerson = sysEmployee.employeeName;
			xbSupplementFee.remarks = "补费"+bd+"元,使用账户余额"+subsidyMoneyBd+"元";
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
	 * 停课选择学员
	 * @return
	 */
	@RequestMapping("/stopchooseStudent")
	public String stopchooseStudent(@RequestParam(required = false) String studentId ,@RequestParam(required = false) String classId , ModelMap model){

		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		studentMap.put("EQ_xbCourse.deleteStatus","1");
		studentMap.put("NEQ_studentStart",1);
		XbStudentRelationViewNew xbStudentRelation = new XbStudentRelationViewNew();
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(studentMap);
		String balanceamount = "0.00";
		if(xbStudentPage.size()>0){
			xbStudentRelation = xbStudentPage.get(0);
		}
		if(null!=classId&&!classId.equals("")){
			xbStudentRelation = studentService.getXbStudentRelationView(classId);
		}
		List<XbStudentRelation> xbClassPage = new ArrayList<>();
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage);
		model.addAttribute("xbStudentRelation",xbStudentRelation);
		model.addAttribute("classId",classId);

		return "stopClass::changeClassFragment";
	}

	/**
	 * 停课
	 * @param studentEntity
	 * @param resp
	 */
	@RequestMapping("/saveStopClass")
	@SystemControllerLog(descrption = "停课",actionType = "1")
	public void saveStopClass(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "3";//停课
			String studentId = xbSupplementFee.studentId;
			String shengyu = xbSupplementFee.shengyu;
			XbStudent xbStudent = studentService.getXbStudent(studentId);
			BigDecimal shengyubd = new BigDecimal(shengyu);
			xbStudent.surplusMoney = xbStudent.surplusMoney.add(shengyubd);
			studentService.saveXbStudent(xbStudent);//更新余额

			//报名课程变更为停课状态
			String classId = xbSupplementFee.classId;
			XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(classId);
			xbSupplementFee.surplusMoney = xbStudentRelation.receivable;
			xbStudentRelation.studentStart = 1;
			xbStudentRelation.receivable = xbStudentRelation.receivable.subtract(shengyubd);
			studentService.saveXbStudentRelation(xbStudentRelation);

			//添加办理中心记录
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
			xbSupplementFee.paymentMoney = shengyubd;
			xbSupplementFee.handlePerson = sysEmployee.employeeName;
			xbSupplementFee.remarks = xbStudentRelation.sysOrgans.organName+xbStudentRelation.xbCourse.courseName+"进行停课";
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
	 * 跳转到查询学员
	 * @return
	 */
	@RequestMapping("/getStopStudentList")
	public String getStopStudentList(@RequestParam(required = false) String studentName, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentName&&!studentName.equals("")){
			resultMap.put("LIKE_studentName",studentName);
		}
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,resultMap);
		model.addAttribute("xbStudentPage",xbStudentsPage);
		return "stopClass::studentList";
	}

	/**
	 * 查询报名未分班的学员
	 * @return
	 */
	@RequestMapping("/getStudentListByCourseType")
	public String getStudentListByCourseType(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentName, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=classId&&!classId.equals("")){
			resultMap.put("EQ_xbCourse.courseTypeId",studentService.getXbClass(classId).xbCourse.courseTypeId);
		}
		if(null!=studentName&&!studentName.equals("")){
			resultMap.put("LIKE_xbStudent.studentName",studentName);
		}
		//yxy
		//resultMap.put("EQ_classId","");
		List<XbStudentRelationViewNew> xbStudentsList = studentService.getXbRelationList(resultMap);
		model.addAttribute("xbStudentsList",xbStudentsList);
		return "enrollClassChoose::studentList";
	}

	/**
	 * 报名选择学员
	 * @return
	 */
	@RequestMapping("/saveStudentRelationClassId")
	public void saveStudentRelationClassId(@RequestParam(required = false) String classid,@RequestParam(required = false) String studentRelationId, ModelMap model, Pageable pageable, HttpServletResponse resp){

		XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(studentRelationId);
		xbStudentRelation.classId = classid;

		try {
			xbStudentRelation = studentService.saveXbStudentRelation(xbStudentRelation);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "选择成功");
			logger.info("查询返回json参数="+jsonObject.toString());
			resp.setContentType("application/json;charset=UTF-8");

			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
		//return "changeClass::changeClassFragment";
	}

	/*
	 * 班级插班
	 * @return
	 */
	@RequestMapping("/enrollClasschoose")
	public String enrollClasschoose(@RequestParam(required = false) String classId, ModelMap model, Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		List<SysOrgans> organsList = organsService.getOrgansList();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		/*if(organsList.size()>0){
			searhMap.put("EQ_organId",organsList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = employeeService.getAccountList(pageable,searhMap);
		XbClass xbClass = studentService.getXbClass(classId);
		/*model.addAttribute("xbXbStudent",student);*/
		model.addAttribute("organsList",organsList);
		model.addAttribute("sysEmployee",sysEmployee);
		model.addAttribute("classId",classId);
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("employeeList",employeePage.getContent());
		return "enrollClassChoose";
	}

	/*
	 * 班级学员选择
	 * @return
	 */
	@RequestMapping("/enrollClass")
	public String getStudent(@RequestParam(required = false) String classId, ModelMap model, Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		List<SysOrgans> organsList = organsService.getOrgansList();
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		/*if(organsList.size()>0){
			searhMap.put("EQ_organId",organsList.get(0).id);
		}*/
		searhMap.put("EQ_sysRole.roleName","运营助理");
		Page<SysEmployee> employeePage = employeeService.getAccountList(pageable,searhMap);
		XbClass xbClass = studentService.getXbClass(classId);
		/*model.addAttribute("xbXbStudent",student);*/
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("organsList",organsList);
		model.addAttribute("sysEmployee",sysEmployee);
		model.addAttribute("classId",classId);
		model.addAttribute("employeeList",employeePage.getContent());
		return "enrollClass";
	}


	@RequestMapping("/save/enroll")
	@SystemControllerLog(descrption = "报名",actionType = "1")
	public void saveOrgans(@RequestParam String studentEntity,HttpServletResponse resp) {
		Map<String, Object> map  =  new HashMap<>();
		try {
			XbStudent xbStudent = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbStudent.class);
			xbStudent = studentService.getXbStudent(xbStudent.id);
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.surplusMoney = xbStudent.paymentMoney;
			BigDecimal su = xbStudent.paymentMoney.subtract(xbStudent.surplusMoney.add(xbSupplementFee.paymentMoney));
			BigDecimal pay = xbStudent.surplusMoney.add(xbSupplementFee.paymentMoney.subtract(xbStudent.paymentMoney));
			int r=su.compareTo(BigDecimal.ZERO); //和0，Zero比较
			int r2=pay.compareTo(BigDecimal.ZERO); //和0，Zero比较
			if(r==-1){//小于
				su = new BigDecimal(0);
			}
			if(r2==-1){//小于
				pay = new BigDecimal(0);
			}
			xbStudent.surplusMoney = pay;
			xbStudent.paymentMoney = su;
			String id = xbStudent.id;
			XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(xbSupplementFee.studentRelationId);
			if(null!=id&&!"".equals(id)){
				BigDecimal totalPeriodNum = BigDecimal.ZERO;
				if(xbStudent.totalPeriodNum!=null){
					totalPeriodNum = xbStudent.totalPeriodNum;
				}
				xbStudent.totalPeriodNum = xbStudentRelation.periodNum.add(totalPeriodNum);
			}
			xbStudent.deleteStatus = "1";
			xbStudent = studentService.saveXbStudent(xbStudent);
			xbStudentRelation.classId = xbSupplementFee.classId;
			studentService.saveXbStudentRelation(xbStudentRelation);
			xbSupplementFee.studentId = xbStudent.id;
			String content = xbStudentRelation.xbCourse.courseName;
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
	 * 选择课程
	 * @return
	 */
	@RequestMapping("/chooseCoursees")
	public String chooseCoursees(@RequestParam(required = false) String classId,@RequestParam(required = false) String courseIds,ModelMap model, Pageable pageable){
		List<XbCoursePreset> xbCourseList = new ArrayList<>();
		BigDecimal moneys = new BigDecimal(0);
		Integer num = 0;
		if(null!=courseIds&&!courseIds.equals("")){
			String[] str = courseIds.split(",");
			for (int i = 0; i < str.length; i++) {
				XbCoursePreset xbCoursePreset = new XbCoursePreset();
				xbCoursePreset = xbCoursePresetService.getXbCoursePreset(str[i]);
				Map<String,Object> xbClasssearhMap = new HashMap<>();
				xbClasssearhMap.put("EQ_courseId",xbCoursePreset.getCourseId());
				Page<XbClass> classPage = studentService.getXbClassList(pageable,xbClasssearhMap);
				xbCoursePreset.xbClassList = classPage.getContent();
				BigDecimal totalmoney = xbCoursePreset.money;//每个课程总金额
				if(xbCoursePreset.xbCourse.chargingMode.equals("0")){
					totalmoney = xbCoursePreset.money.multiply(new BigDecimal(xbCoursePreset.periodNum));
				}
				xbCoursePreset.lsmoney = totalmoney;
				moneys= moneys.add(totalmoney);
				num = num + xbCoursePreset.periodNum;
				xbCourseList.add(xbCoursePreset);
			}
		}
		XbClass xbClass = new XbClass();
		if(null!=classId&&!classId.equals("")){
			xbClass = studentService.getXbClass(classId);
		}
		model.addAttribute("xbCourseLists",xbCourseList);
		model.addAttribute("money",moneys);//总金额
		model.addAttribute("periodNum",num);//总课时
		model.addAttribute("xbClass",xbClass);
		model.addAttribute("organName",xbCourseList.get(0).sysorgans.organName);
		return "enrollClass::baoming";
	}

	/**
	 * 复课查询学员
	 * @return
	 */
	@RequestMapping("/getStudentListByRepeat")
	public String getStudentListByRepeat(@RequestParam(required = false) String studentName, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentName&&!studentName.equals("")){
			resultMap.put("LIKE_studentName",studentName);
		}
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,resultMap);
		model.addAttribute("xbStudentPage",xbStudentsPage);
		return "repeat::studentList";
	}

	/**
	 * 退费选择学员
	 * @return
	 */
	@RequestMapping("/chooseCancelStudent")
	public String chooseCancelStudent(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		//studentMap.put("EQ_deleteStatus","1");
		XbStudentRelationViewNew xbStudentRelation = new XbStudentRelationViewNew();
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(studentMap);
		String balanceamount = "0.00";
		if(xbStudentPage.size()>0){
			if(null!=classId&&!classId.equals("")){
				for (int i = 0; i < xbStudentPage.size(); i++) {
					if(xbStudentPage.get(i).id.equals(classId)){
						xbStudentRelation = xbStudentPage.get(i);
					}
				}
			}else{
				xbStudentRelation = xbStudentPage.get(0);
			}
			BigDecimal receivable = xbStudentRelation.receivable;
			BigDecimal paymentMoney = xbStudent.paymentMoney;
			BigDecimal su = receivable.subtract(paymentMoney);
			balanceamount = su.toString();
		}
		//List<XbStudentRelation> xbClassPage = new ArrayList<>();
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage);
		//model.addAttribute("xbQbClassList",xbClassPage);
		model.addAttribute("classId",classId);
		model.addAttribute("xbStudentRelationId",xbStudentRelation.id);
		model.addAttribute("totalReceivable",xbStudentRelation.totalReceivable);
		model.addAttribute("receivable",xbStudentRelation.receivable);
		model.addAttribute("balanceamount",balanceamount);
		return "cancelClass::changeClassFragment";
	}


	/**
	 * 退费
	 * @param studentEntity
	 * @param resp
	 */
	@RequestMapping("/cancelClassSave")
	@SystemControllerLog(descrption = "退费",actionType = "1")
	public void cancelClassSave(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "5";//退费
			String studentId = xbSupplementFee.studentId;
			String classId = xbSupplementFee.classId;
			String shengyu = xbSupplementFee.shengyu;
			String balanceamount = xbSupplementFee.balanceamount;
			XbStudentRelation xbStudentRelations = studentService.getXbStudentRelation(classId);
			xbStudentRelations.receivable = new BigDecimal("0");
			xbStudentRelations.periodNum = new BigDecimal("0");
			xbStudentRelations.studentStart = 3;
			XbStudent xbStudent = studentService.getXbStudent(studentId);
			xbStudent.paymentMoney = new BigDecimal("0");
			xbStudent.poundage = new BigDecimal(balanceamount).subtract(new BigDecimal(shengyu));
			studentService.saveXbStudent(xbStudent);
			xbSupplementFee.remarks = "对"+xbStudentRelations.sysOrgans.organName+xbStudentRelations.xbCourse.courseName+"进行了退费";
			BigDecimal balanceamountB = new BigDecimal("0").subtract(new BigDecimal(balanceamount));
			BigDecimal money = new BigDecimal("0").subtract(new BigDecimal(shengyu));
			xbSupplementFee.surplusMoney = balanceamountB;//应收金额
			xbSupplementFee.paymentMoney = money;//实收金额
			studentService.saveXbStudentRelation(xbStudentRelations);
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
	 * 退费部分退款备份
	 * @param studentEntity
	 * @param resp
	 */
	/*@RequestMapping("/cancelClassSave")
	public void cancelClassSave(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "5";//退费
			String studentId = xbSupplementFee.studentId;
			String classId = xbSupplementFee.classId;
			String shengyu = xbSupplementFee.shengyu;
			String balanceamount = xbSupplementFee.balanceamount;
			XbStudentRelation xbStudentRelations = studentService.getXbStudentRelation(classId);
			BigDecimal receivable = xbStudentRelations.totalReceivable.divide(xbStudentRelations.totalPeriodNum,2,BigDecimal.ROUND_HALF_UP);
			BigDecimal periodNum = new BigDecimal(shengyu).divide(receivable,2,BigDecimal.ROUND_HALF_UP);
			xbStudentRelations.receivable = xbStudentRelations.receivable.subtract(new BigDecimal(shengyu));
			xbStudentRelations.periodNum = xbStudentRelations.periodNum.subtract(periodNum);
			if(new BigDecimal(shengyu).compareTo(new BigDecimal(balanceamount))==0){
				xbStudentRelations.studentStart = 3;
				//XbStudent xbStudent = studentService.getXbStudent(studentId);
				//xbStudent.paymentMoney = new BigDecimal("0");
				//studentService.saveXbStudent(xbStudent);
			}
			xbSupplementFee.remarks = "对"+xbStudentRelations.sysOrgans.organName+xbStudentRelations.xbCourse.courseName+"进行了退费";
			BigDecimal money = new BigDecimal("0").subtract(new BigDecimal(shengyu));
			xbSupplementFee.surplusMoney = money;//应收金额
			xbSupplementFee.paymentMoney = money;//实收金额
			studentService.saveXbStudentRelation(xbStudentRelations);
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
	}*/

}
