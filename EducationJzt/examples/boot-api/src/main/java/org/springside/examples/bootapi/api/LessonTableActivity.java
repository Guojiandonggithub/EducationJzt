package org.springside.examples.bootapi.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springside.examples.bootapi.service.*;

import java.util.HashMap;
import java.util.Map;


@Controller
@RequestMapping(value = "/lessonTable")
public class LessonTableActivity {

	private static Logger logger = LoggerFactory.getLogger(LessonTableActivity.class);
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
	@Autowired
	public XbStudentService xbStudentService;
	@Autowired
	public XbSubjectService xbSubjectService;
	/**
	 * 跳转到课程表
	 * @return
	 */
	@RequestMapping("/toLessonTable")
	public String toArrangingCourse(ModelMap model, @RequestParam(required=false) String data,
									@PageableDefault(value = 10) Pageable pageable){
		Map<String,Object> searmap = new HashMap<>();
		model.addAttribute("xbClassList",xbStudentService.findXbClassListAll(searmap));
		model.addAttribute("xbSubjectList",xbSubjectService.findSubjectAll());
		return "lessonTable";
	}

}
