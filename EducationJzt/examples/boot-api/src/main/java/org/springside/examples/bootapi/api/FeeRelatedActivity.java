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
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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
	 * 选择学员
	 * @return
	 */
	@RequestMapping("/chooseStudent")
	public String chooseStudent(@RequestParam(required = false) String studentId, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		if(null!=studentId&&!studentId.equals("")){
			resultMap.put("LIKE_studentName",studentId);
		}
		resultMap.put("EQ_id",studentId);
		XbStudent xbStudent = studentService.getXbStudent(studentId);
		Map<String,Object> studentMap = new HashMap<>();
		studentMap.put("EQ_studentId",studentId);
		studentMap.put("EQ_xbClass.deleteStatus","1");
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,studentMap);
		Map<String,Object> studentMaps = new HashMap<>();
		if(xbStudentPage.getContent().size()>0){
			String typeId = xbStudentPage.getContent().get(0).xbCourse.xbcoursetype.id;
			String classId = xbStudentPage.getContent().get(0).xbClass.id;
			studentMaps.put("EQ_xbCourse.xbcoursetype.id",typeId);
			studentMaps.put("NEQ_xbClass.id",classId);
			studentMaps.put("EQ_organId",xbStudentPage.getContent().get(0).organId);
			studentMaps.put("EQ_xbClass.deleteStatus","1");
		}else{
			studentMaps.put("EQ_organId","10001");
		}
		Page<XbStudentRelation> xbClassPage = studentService.getXbStudentRelationList(pageable,studentMaps);
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage.getContent());
		model.addAttribute("xbQbClassList",xbClassPage.getContent());
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

	@RequestMapping("/changeClassSave")
	public void changeClassSave(@RequestParam String studentEntity, HttpServletResponse resp) {
		try {
			XbSupplementFee xbSupplementFee = com.alibaba.fastjson.JSONObject.parseObject(studentEntity,XbSupplementFee.class);
			xbSupplementFee.type = "1";//转班
			String studentId = xbSupplementFee.studentId;
			String classId = xbSupplementFee.classId;
			String toClassId = xbSupplementFee.toClassId;
			Map<String,Object> searchMap = new HashMap<>();
			searchMap.put("EQ_studentId",studentId);
			searchMap.put("EQ_classId",classId);
			List<XbStudentRelation> xbStudentRelations = studentService.getXbRelationList(searchMap);
			if(xbStudentRelations.size()>0){
				XbStudentRelation xbStudentRelation = xbStudentRelations.get(0);
				xbStudentRelation.classId = toClassId;
				XbClass xbClass = studentService.getXbClass(toClassId);
				xbStudentRelation.courseId = xbClass.courseId;
				studentService.saveXbStudentRelation(xbStudentRelation);
				BigDecimal receivable = xbStudentRelation.receivable;
				xbSupplementFee.paymentMoney = receivable;
				xbSupplementFee.surplusMoney = receivable;
				xbSupplementFee.remarks = xbStudentRelation.xbClass.className+"转到"+xbClass.className;
			}
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

}
