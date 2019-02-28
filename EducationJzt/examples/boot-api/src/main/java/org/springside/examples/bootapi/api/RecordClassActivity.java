package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import java.math.RoundingMode;
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
		//学员名称
		String studentName = (String)resultMap.get("studentName");
		if(StringUtils.isNotEmpty(studentName)){
			searhMap.put("LIKE_xbStudent.studentName",studentName);
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
		model.addAttribute("studentName",studentName);
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
		List<XbStudentRelationViewNew> classPage = studentService.getXbRelationList(searhMap);
		model.addAttribute("classPage",classPage);
		model.addAttribute("classSize",classPage.size());
		model.addAttribute("classes",classes);
		return "classEdit";
	}

	@PostMapping("/save/recordClass")
	public void recordClass(@RequestBody List<XbRecordClass> xbRecordClassList, HttpServletResponse resp, Pageable pageable) {
		try {
			for (XbRecordClass xbRecordClass : xbRecordClassList) {
				if(null!=xbRecordClass.state&&!xbRecordClass.state.equals("4")&&null!=xbRecordClass.deductPeriod){
					if(!xbRecordClass.state.equals("0")&&xbRecordClass.deductPeriod.compareTo(new BigDecimal("0"))==0){
						BigDecimal deductPeriod = xbRecordClass.deductPeriod;
						String studentRelationId = xbRecordClass.studentRelationId;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						xbRecordClass.recordTime = sdf.parse(xbRecordClass.recordTimeTemp);
						XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(studentRelationId);
						BigDecimal bigDecimal = xbStudentRelation.periodNum;
						BigDecimal totalPeriodNum = xbStudentRelation.totalPeriodNum;
						BigDecimal totalReceivable = xbStudentRelation.totalReceivable;
						if(bigDecimal.compareTo(new BigDecimal("0"))!=0){
							BigDecimal receivable = xbStudentRelation.receivable;
							BigDecimal money = totalReceivable.divide(totalPeriodNum,2,RoundingMode.HALF_UP).multiply(deductPeriod);
							receivable = receivable.subtract(money);
							bigDecimal = bigDecimal.subtract(deductPeriod);
							//xbStudentRelation.periodNum = Integer.parseInt(bigDecimal.toString());
							if(bigDecimal.compareTo(new BigDecimal("0"))<0){
								bigDecimal = new BigDecimal("0");
							}
							if(receivable.compareTo(new BigDecimal("0"))<0){
								receivable = new BigDecimal("0");
							}
							xbStudentRelation.periodNum = bigDecimal;
							xbStudentRelation.receivable = receivable;
							xbRecordClass.deductMoney = money;
							studentService.saveXbRecordClass(xbRecordClass);
							studentService.saveXbStudentRelation(xbStudentRelation);
						}
					}
				}
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "记上课成功");
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
		for (int i = 0; i < recordLists.getContent().size(); i++) {
			BigDecimal periodnum = recordLists.getContent().get(i).periodnum;
			String classesId = recordLists.getContent().get(i).classId;
			Map<String, Object> searhMaps = new HashMap<>();
			searhMaps.put("EQ_classId", classesId);
			searhMaps.put("GTE_periodNum", new BigDecimal("0"));
			Pageable pageables = new PageRequest(0, 1, null);
			Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageables, searhMaps);
			if (classPage.getContent().size() > 0) {
				BigDecimal totalPeriodNum = classPage.getContent().get(0).totalPeriodNum;
				BigDecimal totalReceivable = classPage.getContent().get(0).totalReceivable;
				BigDecimal periodnums = recordLists.getContent().get(i).periodnum;
				BigDecimal receivable = totalReceivable.divide(totalPeriodNum,2,RoundingMode.HALF_UP).multiply(periodnums);
				BigDecimal setScale = receivable.setScale(2,BigDecimal.ROUND_HALF_UP);
				recordLists.getContent().get(i).totalReceivable =setScale;
			}
		}
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


		List<XbRecordClassView> recordList = studentService.getXbRecordClassdViewtoList(searhMap);
		BigDecimal totalPeriodnum = new BigDecimal("0");
		BigDecimal totalReceivables = new BigDecimal("0");
		for (int i = 0; i < recordList.size(); i++) {
			BigDecimal periodnum = recordList.get(i).periodnum;
			totalPeriodnum = totalPeriodnum.add(periodnum);
			String classesId = recordList.get(i).classId;
			Map<String, Object> searhMaps = new HashMap<>();
			searhMaps.put("EQ_classId", classesId);
			searhMaps.put("GTE_periodNum", new BigDecimal("0"));
			Pageable pageables = new PageRequest(0, 1, null);
			Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageables, searhMaps);
			if (classPage.getContent().size() > 0) {
				BigDecimal totalPeriodNum = classPage.getContent().get(0).totalPeriodNum;
				BigDecimal totalReceivable = classPage.getContent().get(0).totalReceivable;
				BigDecimal receivable = totalReceivable.divide(totalPeriodNum,2,RoundingMode.HALF_UP).multiply(recordList.get(i).periodnum);
				totalReceivables = totalReceivables.add(receivable);
			}
		}
		model.addAttribute("totalPeriodnum",totalPeriodnum);
		model.addAttribute("totalReceivables",totalReceivables);
		return "attendClass::accordingClass";
	}

	@RequestMapping("/delete/recrodClass")
	public void deleteRecordClass(@RequestParam(required = false) String classId,@RequestParam(required = false) String recordTime,HttpServletResponse resp,Pageable pageable) {
		try {
			Map<String, Object> searchParams = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			searchParams.put("EQ_attendId",classId);
			searchParams.put("EQ_recordTime",sdf.parse(recordTime));
			Page<XbRecordClass> xbRecordClassPage = studentService.getRecordClassPage(pageable,searchParams);
			List<XbRecordClass> xbRecordClassList = xbRecordClassPage.getContent();
			for (int i = 0; i < xbRecordClassList.size(); i++) {
				String id = xbRecordClassList.get(i).id;
				String studentRelationId = xbRecordClassList.get(i).studentRelationId;
				BigDecimal deductPeriod = xbRecordClassList.get(i).deductPeriod;
				XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(studentRelationId);
				xbStudentRelation.periodNum = xbStudentRelation.periodNum.add(deductPeriod);
				BigDecimal money = xbStudentRelation.totalReceivable.divide(xbStudentRelation.totalPeriodNum,2,RoundingMode.HALF_UP).multiply(deductPeriod);
				xbStudentRelation.receivable = xbStudentRelation.receivable.add(money);
				studentService.saveXbStudentRelation(xbStudentRelation);
				studentService.deleteXbRecordClass(id);
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "删除成功");
			logger.info("删除返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
	}

	/*
	 * 跳转到修改记上课
	 * @return
	 */
	@RequestMapping("/recordClassUpdate")
	public String clasrecordClassUpdatesEdit(@RequestParam(required = false) String classesId,@RequestParam(required = false) String recordTime, ModelMap model, Pageable pageable){
		try {
			Map<String, Object> searchParams = new HashMap<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			searchParams.put("EQ_attendId",classesId);
			searchParams.put("EQ_recordTime",sdf.parse(recordTime));
			Page<XbRecordClass> xbRecordClassPage = studentService.getRecordClassPage(pageable,searchParams);
			List<XbRecordClass> xbRecordClassList = xbRecordClassPage.getContent();
			XbClass classes = studentService.getXbClass(classesId);
			//List<XbStudentRelationViewNew> classPage = studentService.getXbRelationList(searhMap);
			model.addAttribute("classPage",xbRecordClassList);
			model.addAttribute("classSize",xbRecordClassList.size());
			model.addAttribute("classes",classes);
			model.addAttribute("recordTime",recordTime);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
		return "recordClassEdit";
	}

	@PostMapping("/update/recordClass")
						public void updateRecordClass(@RequestBody List<XbRecordClass> xbRecordClassList, HttpServletResponse resp, Pageable pageable) {
							try {
								for (XbRecordClass xbRecordClass : xbRecordClassList) {
									if(null!=xbRecordClass.state&&!xbRecordClass.state.equals("4")&&null!=xbRecordClass.deductPeriod){
										BigDecimal deductPeriod = xbRecordClass.deductPeriod;
										XbRecordClass xbRecordClasses = studentService.getxbRecordClass(xbRecordClass.id);
										xbRecordClasses.state = xbRecordClass.state;
										String studentRelationId = xbRecordClass.studentRelationId;
										XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(studentRelationId);
										BigDecimal bigDecimal = xbStudentRelation.periodNum;
										BigDecimal totalPeriodNum = xbStudentRelation.totalPeriodNum;
										BigDecimal totalReceivable = xbStudentRelation.totalReceivable;
										if(bigDecimal.compareTo(new BigDecimal("0"))!=0){
						BigDecimal receivable = xbStudentRelation.receivable;
						BigDecimal money = totalReceivable.divide(totalPeriodNum,2,RoundingMode.HALF_UP).multiply(xbRecordClasses.deductPeriod.subtract(deductPeriod));
						receivable = receivable.add(money);
						bigDecimal = bigDecimal.add(xbRecordClasses.deductPeriod.subtract(deductPeriod));
						//xbStudentRelation.periodNum = Integer.parseInt(bigDecimal.toString());
						if(bigDecimal.compareTo(new BigDecimal("0"))<0){
							bigDecimal = new BigDecimal("0");
						}
						if(receivable.compareTo(new BigDecimal("0"))<0){
							receivable = new BigDecimal("0");
						}
						xbStudentRelation.periodNum = bigDecimal;
						xbStudentRelation.receivable = receivable;
						studentService.saveXbStudentRelation(xbStudentRelation);
						xbRecordClasses.deductPeriod = deductPeriod;
						studentService.saveXbRecordClass(xbRecordClasses);
					}
				}
				if(xbRecordClass.state.equals("4")){
					XbRecordClass xbRecordClasses = studentService.getxbRecordClass(xbRecordClass.id);
					BigDecimal deductPeriods = xbRecordClasses.deductPeriod;
					String studentRelationId = xbRecordClass.studentRelationId;
					XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(studentRelationId);
					BigDecimal money = xbStudentRelation.totalReceivable.divide(xbStudentRelation.totalPeriodNum,2,RoundingMode.HALF_UP).multiply(deductPeriods);
					xbStudentRelation.receivable = xbStudentRelation.receivable.add(money);
					xbStudentRelation.periodNum = xbStudentRelation.periodNum.add(deductPeriods);
					studentService.saveXbStudentRelation(xbStudentRelation);
					studentService.deleteXbRecordClass(xbRecordClasses.id);
				}
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("status","1");
			jsonObject.put("msg", "记上课成功");
			logger.info("编辑机构返回json参数="+jsonObject.toString());
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
	}

}
