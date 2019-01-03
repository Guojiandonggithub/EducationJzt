package org.springside.examples.bootapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.domain.XbStudent;
import org.springside.examples.bootapi.domain.XbStudentRelation;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.XbStudentService;

import java.util.HashMap;
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
	public String changeClass(ModelMap model, @PageableDefault(sort = { "layOrder" }, direction = Sort.Direction.ASC)Pageable pageable){

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
	 * 跳转到查询学员
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
		Page<XbStudentRelation> xbStudentPage = studentService.getXbStudentRelationList(pageable,studentMap);
		Map<String,Object> studentMaps = new HashMap<>();
		Page<XbStudentRelation> xbClassPage = studentService.getXbStudentRelationList(pageable,studentMaps);
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbClassList",xbStudentPage.getContent());
		model.addAttribute("xbQbClassList",xbClassPage.getContent());
		return "changeClass::changeClassFragment";
	}
}
