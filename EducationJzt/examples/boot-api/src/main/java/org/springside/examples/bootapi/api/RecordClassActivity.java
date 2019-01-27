package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.XbCoursePresetService;
import org.springside.examples.bootapi.service.XbCourseTypeService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 记上课
 */
@Controller
@RequestMapping(value = "/recordClass")
public class RecordClassActivity {

	private static Logger logger = LoggerFactory.getLogger(RecordClassActivity.class);

	@Autowired
	private XbStudentService studentService;

	@Autowired
	public XbCoursePresetService xbCoursePresetService;

	@Autowired
	private OrgansService organsService;

	@Autowired
	public XbCourseTypeService xbCourseTypeService;

	/*
	 * 查询按学员列表
	 * @return
	 */
	@RequestMapping("/accordingStudent")
	public String accordingStudent(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();

		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		Map<String,Object> searhMap = searchModelForAccordingStudent(model,resultMap);
		Page<XbRecordClass> xbRecordClassPage = studentService.getRecordClassPage(pageable,searhMap);
		model.addAttribute("xbRecordClassPage",xbRecordClassPage);
		model.addAttribute("accordingcurrentzise",xbRecordClassPage.getSize());

		return "attendClass::accordingStudent";
	}
	private Map<String,Object>  searchModelForAccordingStudent(ModelMap model,Map<String,Object> resultMap){
		Map<String,Object> searhMap = new HashMap<>();
		String classesName  = (String)resultMap.get("classesName");
		if(null!=classesName&&!classesName.equals("")){
			searhMap.put("LIKE_className",classesName);
		}
		//校区
		String organId = (String)resultMap.get("organId");
		if(StringUtils.isEmpty(organId)|| organId.equals("0")){
			organId = "0";
		}else{
			searhMap.put("EQ_xbClass.sysOrgans.id",organId);
		}
		//教师名称
		String TeacherName = (String)resultMap.get("TeacherName");
		if(StringUtils.isNotEmpty(TeacherName)){
			searhMap.put("LIKE_xbClass.teacher.employeeName",TeacherName);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateTimeBegin = (String)resultMap.get("startDateTimeBegin");
		//开课结束日期
		String startDateTimeEnd = (String)resultMap.get("startDateTimeEnd");
		try {
			Date date = new Date();
			if(StringUtils.isEmpty(startDateTimeBegin)){
				startDateTimeBegin = DateUtil.weekDateFirstDay();
				searhMap.put("GTE_recordTime",DateUtil.weekDateTimeFirstDayDA());
			}else{
				searhMap.put("GTE_recordTime",sdf.parse(startDateTimeBegin+" 00:00:00"));
			}
			if(StringUtils.isEmpty(startDateTimeEnd)){
				startDateTimeEnd = DateUtil.weekDateLastDay();
				searhMap.put("LTE_recordTime",DateUtil.weekDateTimeLastDayDA());
			}else{
				searhMap.put("LTE_recordTime",sdf.parse(startDateTimeEnd+" 23:59:59"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		//查询所有校区
		Map<String,Object> sorgsearmap = new HashMap<>();
		List<SysOrgans> sorganList = organsService.getOrgansListAll(sorgsearmap);
		model.addAttribute("sorganList",sorganList);
		model.addAttribute("classesName",classesName);
		model.addAttribute("organId",organId);
		model.addAttribute("startDateTimeBegin",startDateTimeBegin);
		model.addAttribute("startDateTimeEnd",startDateTimeEnd);
		model.addAttribute("TeacherName",TeacherName);
			return searhMap;
	}

	/*
	 * 跳转到记上课列表
	 * @return
	 */
	@RequestMapping("/getRecordClassList")
	public String getRecordClassList(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String classesName  = (String)resultMap.get("classesName");
		String type = (String)resultMap.get("type");
		String chargingMode = (String)resultMap.get("chargingMode");

		String conrseType = (String)resultMap.get("conrseType");
		String organId = (String)resultMap.get("organId");
		if(null==organId){
			organId = "0";
		}else if(!organId.equals("0")){
			searhMap.put("EQ_organId",organId);
		}
		if(null!=classesName&&!classesName.equals("")){
			searhMap.put("LIKE_className",classesName);
		}
		if(null==type){
			type = "2";
		}else if(!type.equals("2")){
			searhMap.put("EQ_xbCourse.type",type);
		}
		if(null==chargingMode){
			chargingMode = "3";
		}else if(!chargingMode.equals("3")){
			searhMap.put("EQ_xbCourse.chargingMode",chargingMode);
		}
		if(null==conrseType||conrseType.equals("0")){
			conrseType = "";
		}else if(!conrseType.equals("")&&!conrseType.equals("0")){
			searhMap.put("EQ_xbCourse.xbcoursetype.id",conrseType);
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbClass> classPage = studentService.getXbClassList(pageable,searhMap);
		Map<String,Object> searhtypeMap = new HashMap<>();
		List<XbCourseType> coursetypelist = xbCourseTypeService.findXbCourseTypeList(searhtypeMap);
		model.addAttribute("classPage",classPage);
		model.addAttribute("classesName",classesName);
		model.addAttribute("currentzise",classPage.getSize());
		model.addAttribute("organId",organId);
		model.addAttribute("type",type);
		model.addAttribute("chargingMode",chargingMode);
		model.addAttribute("organsList",organsList);
		model.addAttribute("coursetypelist",coursetypelist);
		model.addAttribute("conrseType",conrseType);
		return "attendClass";
	}

	/*
	 * 跳转到记上课
	 * @return
	 */
	@RequestMapping("/classEdit")
	public String classEdit(@RequestParam(required = false) String classesId, ModelMap model, Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=classesId){
			searhMap.put("LIKE_classId",classesId);
		}
		XbClass classes = studentService.getXbClass(classesId);
		Page<XbStudentRelation> classPage = studentService.getXbStudentRelationList(pageable,searhMap);
		model.addAttribute("classPage",classPage);
		model.addAttribute("classes",classes);
		return "classEdit";
	}

	@PostMapping("/save/recordClass")
	public void recordClass(@RequestBody List<XbRecordClass> xbRecordClassList, HttpServletResponse resp, Pageable pageable) {
		try {
			for (XbRecordClass xbRecordClass : xbRecordClassList) {
				String studentId = xbRecordClass.studentId;
				BigDecimal deductPeriod = xbRecordClass.deductPeriod;
				String classId = xbRecordClass.classId;
				String organId = xbRecordClass.organId;
				String courseId = xbRecordClass.courseId;
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				xbRecordClass.recordTime = sdf.parse(xbRecordClass.recordTimeTemp);
				Map<String,Object> searhMap = new HashMap<>();
				Map<String,Object> courseMap = new HashMap<>();
				if(null!=classId){
					searhMap.put("EQ_classId",classId);
					searhMap.put("EQ_studentId",studentId);
				}
				courseMap.put("EQ_courseId",courseId);
				courseMap.put("EQ_organIds",organId);
				BigDecimal money = new BigDecimal(0);
				List<XbCoursePreset> xbCoursePresetList = xbCoursePresetService.getXbCoursePresets(courseMap);
				if(xbCoursePresetList.size()>0){
					String chargingMode = xbCoursePresetList.get(0).xbCourse.chargingMode;
					money = xbCoursePresetList.get(0).money;
					if(chargingMode.equals("2")){
						money = money.divide(new BigDecimal(xbCoursePresetList.get(0).periodNum),2,BigDecimal.ROUND_HALF_UP);
					}
				}
				Page<XbStudentRelation> xbStudentRelations = studentService.getXbStudentRelationList(pageable,searhMap);
				XbStudentRelation xbStudentRelation = xbStudentRelations.getContent().get(0);
				//Integer periodNum = xbStudentRelation.periodNum;
				BigDecimal bigDecimal = xbStudentRelation.periodNum;
				BigDecimal receivable = xbStudentRelation.receivable;
				//BigDecimal bigDecimal = new BigDecimal(periodNum.toString());
				money = deductPeriod.multiply(money);
				receivable = receivable.subtract(money);
				bigDecimal = bigDecimal.subtract(deductPeriod);
				//xbStudentRelation.periodNum = Integer.parseInt(bigDecimal.toString());
				xbStudentRelation.periodNum = bigDecimal;
				xbStudentRelation.receivable = receivable;
				studentService.saveXbStudentRelation(xbStudentRelation);
				studentService.saveXbRecordClass(xbRecordClass);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "编辑成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
	}

	/*
	 * 查询上课记录按班级
	 * @return
	 */
	/*@RequestMapping("/getRecordClassListByClass")
	public String getRecordClassListByClass(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String classesName  = (String)resultMap.get("classesName");

		List recordLists = studentService.getXbRecordClassdtoList(pageable.getOffset(),pageable.getPageSize());
		int totalElements = studentService.findRecordTotalCount();
		int size = pageable.getPageSize();
		int number = pageable.getPageNumber();
		int totalPages = totalElements/size;
		if(totalPages==0){
			number = 1;
		}
		totalPages = totalPages + 1;
		model.addAttribute("recordLists",recordLists);
		model.addAttribute("recordcurrentzise",size);
		model.addAttribute("number",number);
		model.addAttribute("totalPages",totalPages);
		model.addAttribute("totalElements",totalElements);
		//查询所有校区
		Map<String,Object> sorgsearmap = new HashMap<>();
		List<SysOrgans> sorganList = organsService.getOrgansListAll(sorgsearmap);
		model.addAttribute("sorganList",sorganList);
		return "attendClass::accordingClass";
	}*/
	/*
	 * 查询上课记录按班级
	 * @return
	 */
	@RequestMapping("/getRecordClassListByClass")
	public String getRecordClassListByClass(@RequestParam(required = false) String data, ModelMap model,
											@PageableDefault(value = 10) Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String classesName  = (String)resultMap.get("classesName");
		String organclaId = (String)resultMap.get("organclaId");
		if(null==organclaId){
			organclaId = "0";
		}else if(!organclaId.equals("0")){
			searhMap.put("EQ_orgid",organclaId);
		}
		//教师名称
		String TeacherNameCla = (String)resultMap.get("TeacherNameCla");
		if(StringUtils.isNotEmpty(TeacherNameCla)){
			searhMap.put("LIKE_employeeName",TeacherNameCla);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startDateTimeBegin = (String)resultMap.get("startclaDateTimeBegin");
		//开课结束日期
		String startDateTimeEnd = (String)resultMap.get("startclaDateTimeEnd");
		try {
			Date date = new Date();
			if(StringUtils.isEmpty(startDateTimeBegin)){
				startDateTimeBegin = DateUtil.weekDateFirstDay();
				searhMap.put("GTE_recordTime",DateUtil.weekDateTimeFirstDayDA());
			}else{
				searhMap.put("GTE_recordTime",sdf.parse(startDateTimeBegin+" 00:00:00"));
			}
			if(StringUtils.isEmpty(startDateTimeEnd)){
				startDateTimeEnd = DateUtil.weekDateLastDay();
				searhMap.put("LTE_recordTime",DateUtil.weekDateTimeLastDayDA());
			}else{
				searhMap.put("LTE_recordTime",sdf.parse(startDateTimeEnd+" 23:59:59"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Page<XbRecordClassView> recordLists = studentService.getXbRecordClassdViewtoList(pageable,searhMap);
		int totalElements = studentService.findRecordTotalCount();
		int size = pageable.getPageSize();
		int number = pageable.getPageNumber();
		int totalPages = totalElements/size;
		if(totalPages==0){
			number = 1;
		}
		totalPages = totalPages + 1;
		model.addAttribute("recordLists",recordLists);
		model.addAttribute("recordcurrentzise",size);
		model.addAttribute("number",number);
		model.addAttribute("totalPages",totalPages);
		model.addAttribute("totalElements",totalElements);
		//查询所有校区
		Map<String,Object> sorgsearmap = new HashMap<>();
		List<SysOrgans> sorganList = organsService.getOrgansListAll(sorgsearmap);
		model.addAttribute("sorganList",sorganList);
		model.addAttribute("organclaId",organclaId);
		model.addAttribute("startclaDateTimeBegin",startDateTimeBegin);
		model.addAttribute("startclaDateTimeEnd",startDateTimeEnd);
		model.addAttribute("TeacherNameCla",TeacherNameCla);
		return "attendClass::accordingClass";
	}

}
