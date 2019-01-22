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
		if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}
		searhMap.put("EQ_sysRole.roleName","销售员");
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
		if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}
		searhMap.put("EQ_sysRole.roleName","销售员");
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
		if(sysOrgansList.size()>0){
			searhMap.put("EQ_organId",sysOrgansList.get(0).id);
		}
		searhMap.put("EQ_sysRole.roleName","销售员");
		Page<SysEmployee> employeePage = accountService.getAccountList(pageable,searhMap);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SysEmployee sysEmployee = (SysEmployee)request.getSession().getAttribute("sysEmployee");
		model.addAttribute("sysOrgansList",sysOrgansList);
		model.addAttribute("employeeList",employeePage.getContent());
		model.addAttribute("sysEmployee",sysEmployee);
		return "stopClass";
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
		studentMap.put("EQ_xbClass.deleteStatus","1");
		if(null!=classId&&!classId.equals("")){
			studentMap.put("EQ_id",classId);
		}
		XbStudentRelation xbStudentRelation = new XbStudentRelation();
		List<XbStudentRelation> xbStudentPage = studentService.getXbRelationList(studentMap);
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
		model.addAttribute("balanceamount",balanceamount);
		return "changeClass::changeClassFragment";
	}

	/**
	 * 选择学员
	 * @return
	 */
	@RequestMapping("/chooseSubsidyStudent")
	public String chooseSubsidyStudent(@RequestParam(required = false) String classId,@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		studentMap.put("EQ_xbClass.deleteStatus","1");
		if(null!=classId&&!classId.equals("")){
			studentMap.put("EQ_id",classId);
		}
		XbStudentRelation xbStudentRelation = new XbStudentRelation();
		List<XbStudentRelation> xbStudentPage = studentService.getXbRelationList(studentMap);
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
		model.addAttribute("balanceamount",balanceamount);
		return "subsidy::changeClassFragment";
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
		studentMap.put("EQ_xbClass.deleteStatus","1");
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,studentMap);
		studentMap.put("EQ_id",classId);
		List<XbStudentRelation> xbRelationList = studentService.getXbRelationList(studentMap);
		XbStudentRelation xbStudentRelation = new XbStudentRelation();
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
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,studentMap);
		Map<String,Object> studentMaps = new HashMap<>();
		studentMaps.put("EQ_xbCourse.xbcoursetype.id",studentId);
		Page<XbStudentRelation> xbClassPage = studentService.getXbStudentRelationList(pageable,studentMaps);
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
	public void changeClassSave(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "1";//转班
			String studentId = xbSupplementFee.studentId;
			String classId = xbSupplementFee.classId;
			String toClassId = xbSupplementFee.toClassId;
			String receivables = xbSupplementFee.receivable;
			String balanceamount = xbSupplementFee.balanceamount;
			String choosecourseId = xbSupplementFee.choosecourseId;
			BigDecimal receivablebd = new BigDecimal(receivables);
			BigDecimal balanceamountbd = new BigDecimal(balanceamount);
			XbStudent xbStudent = studentService.getXbStudent(studentId);
			BigDecimal ba = balanceamountbd.subtract(receivablebd);
			int r = ba.compareTo(BigDecimal.ZERO); //和0，Zero比较
			if(r==-1){//小于
				xbStudent.paymentMoney = new BigDecimal(0.00).subtract(ba);
				xbSupplementFee.paymentMoney = new BigDecimal(0.00);
				xbSupplementFee.surplusMoney = new BigDecimal(0.00).subtract(ba);
			}else{
				xbStudent.surplusMoney = xbStudent.surplusMoney.add(ba);
				xbStudent.paymentMoney = new BigDecimal("0.00");
				xbSupplementFee.paymentMoney = ba;
				xbSupplementFee.surplusMoney = ba;
			}
			Map<String,Object> searchMap = new HashMap<>();
			XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(classId);
			XbCoursePreset xbCoursePreset = xbCoursePresetService.getXbCoursePreset(choosecourseId);
			xbStudentRelation.classId = toClassId;
			XbClass xbClass = studentService.getXbClass(toClassId);
			xbStudentRelation.courseId = xbClass.courseId;
			xbSupplementFee.remarks = xbStudentRelation.xbClass.className+"转到"+xbClass.className;
			xbStudentRelation.totalReceivable = xbCoursePreset.money;
			xbStudentRelation.receivable = xbCoursePreset.money;
			xbStudentRelation.totalPeriodNum = new BigDecimal(xbCoursePreset.periodNum);
			xbStudentRelation.periodNum = new BigDecimal(xbCoursePreset.periodNum);
			studentService.saveXbStudentRelation(xbStudentRelation);
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
	 * 补费
	 * @param studentEntity
	 * @param resp
	 */
	@RequestMapping("/saveSubsidy")
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
				BigDecimal min = xbStudent.surplusMoney.subtract(subsidyMoneyBd);
				xbStudent.surplusMoney = min.add(bd);
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
		XbStudentRelation xbStudentRelation = new XbStudentRelation();
		List<XbStudentRelation> xbStudentPage = studentService.getXbRelationList(studentMap);
		String balanceamount = "0.00";
		if(xbStudentPage.size()>0){
			xbStudentRelation = xbStudentPage.get(0);
		}
		if(null!=classId&&!classId.equals("")){
			xbStudentRelation = studentService.getXbStudentRelation(classId);
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

}
