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
import org.springside.examples.bootapi.service.*;

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
@RequestMapping(value = "/recordClassWechat")
public class WechatRecordClassActivity {

	private static Logger logger = LoggerFactory.getLogger(WechatRecordClassActivity.class);
	@Autowired
	private XbStudentService studentService;
	@Autowired
	public XbCoursePresetService xbCoursePresetService;
	@Autowired
	private OrgansService organsService;
	@Autowired
	public XbCourseTypeService xbCourseTypeService;
	@Autowired
	public XbAttendClassService xbAttendClassService;
	@Autowired
	public XbStudentService xbStudentService;
	/*
	 * 查询按学员列表
	 * @return
	 */
	@RequestMapping("/accordingStudent")
	public String accordingStudent(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String classesName  = (String)resultMap.get("classesName");
		if(null!=classesName&&!classesName.equals("")){
			searhMap.put("LIKE_className",classesName);
		}
		Page<XbRecordClass> xbRecordClassPage = studentService.getRecordClassPage(pageable,searhMap);
		model.addAttribute("xbRecordClassPage",xbRecordClassPage);
		model.addAttribute("classesName",classesName);
		model.addAttribute("accordingcurrentzise",xbRecordClassPage.getSize());
		return "attendClass::accordingStudent";
	}

	/*
	 * 跳转到记上课列表getRecordClassList
	 * @return
	 */
	@RequestMapping("/getRecordClassList")
	public String getRecordClassList(@RequestParam(required = false) String data, ModelMap model,
									 @PageableDefault(value = 10) Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		/*if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String classesName  = (String)resultMap.get("classesName");
		String type = (String)resultMap.get("type");
		String chargingMode = (String)resultMap.get("chargingMode");
		String organId = (String)resultMap.get("organId");
		String conrseType = (String)resultMap.get("conrseType");
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
		if(null==conrseType){
			conrseType = "";
		}else if(!conrseType.equals("")){
			searhMap.put("EQ_xbCourse.xbcoursetype.id",conrseType);
		}*/
		//Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbClass> classPage = studentService.getXbClassList(pageable,searhMap);
		Map<String,Object> searhtypeMap = new HashMap<>();
		//List<XbCourseType> coursetypelist = xbCourseTypeService.findXbCourseTypeList(searhtypeMap);
		model.addAttribute("classPage",classPage);
		//model.addAttribute("classesName",classesName);
		//model.addAttribute("currentzise",classPage.getSize());
		//model.addAttribute("organId",organId);
		//model.addAttribute("type",type);
		//model.addAttribute("chargingMode",chargingMode);
		//model.addAttribute("organsList",organsList);
		//model.addAttribute("coursetypelist",coursetypelist);
		//model.addAttribute("conrseType",conrseType);
		return "wechat_timetableMore";
	}
	/*
	 * 跳转到记上课列表下拉加载.....
	 * @return
	 */
	@RequestMapping("/getRecordClassList_reloading")
	public String getRecordClassListReloading(@RequestParam(required = false) String data, ModelMap model,
									 @PageableDefault(value = 10) Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		Page<XbClass> classPage = studentService.getXbClassList(pageable,searhMap);
		Map<String,Object> searhtypeMap = new HashMap<>();
		//List<XbCourseType> coursetypelist = xbCourseTypeService.findXbCourseTypeList(searhtypeMap);
		model.addAttribute("classPage",classPage);
		return "wechat_timetableMore::WECHAT_RECORDCLASSLIST_SPANID_FRAGMENT";
	}
	/**
	 * 跳转到日程表
	 * @return
	 */
	@RequestMapping("/toTimeTable")
	public String toTimeTable(ModelMap model,@PageableDefault(value = 20) Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dda = sdf.format(new Date());
		searhMap.put("EQ_startDateTime",dda);//周日历
		Page<XbAttendClass> xbAttendList = xbAttendClassService.findXbAttendClassPageAll(pageable,searhMap);
		model.addAttribute("xbAttendList",xbAttendList);
		model.addAttribute("dda",dda);
		return "wechat_timetable";
	}
	/**
	 * 查询所有的排课信息
	 */
	@RequestMapping("/findXbAttendClassPageListAll")
	public String findXbAttendClassPageListAll(@RequestParam String dda,HttpServletResponse resp,
											   ModelMap model,@PageableDefault(value = 20) Pageable pageable,String data) throws ParseException {
		Map<String,Object> resultMap = new HashMap<>();
		/*if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,resultMap.getClass());
		}*/
		//Map<String,Object> searhMap = parameterAssemblyByfindXbAttendClassPageAll(model,resultMap);
		Map<String,Object> searhMap = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd");
		Date date = sdf.parse(dda);
		SimpleDateFormat sdfsear = new SimpleDateFormat("yyyy-MM-dd");
		searhMap.put("EQ_startDateTime",sdfsear.format(date));//周日历
		Page<XbAttendClass> xbAttendList = xbAttendClassService.findXbAttendClassPageAll(pageable,searhMap);
		/*for(XbAttendClass xbattendclass : xbAttendList){
			Map<String,Object> searchParamsview = new HashMap<>();
			searchParamsview.put("EQ_classId",xbattendclass.classId);
			List<XbStudentRelationView> studentlist = studentService.getxbStudentRelationViewList(searchParamsview);
			xbattendclass.ydstudentnum = studentlist.size();
			xbattendclass.sdstudentnum = xbAttendClassService.getSdstudentnum(xbattendclass.classId,xbattendclass.startDateTime);
		}*/
		model.addAttribute("xbAttendList",xbAttendList);
		model.addAttribute("dda",dda);
		return "wechat_timetable::xbAttendListFra";
		//model.addAttribute("xbAttendListsize",xbAttendList.getSize());
	}
	private Map<String,Object> parameterAssemblyByfindXbAttendClassPageAll(ModelMap model,Map<String,Object> resultMap){
		Map<String,Object> searhMap = new HashMap<>();
		String type = (String)resultMap.get("type");//下拉查询
		if(StringUtils.isEmpty(type)){
			type = "class_search";
		}
		String searhname = (String)resultMap.get("searhname");//下拉查询
		if(StringUtils.isNotEmpty(searhname)){
			if(type.equals("class_search")){
				searhMap.put("LIKE_xbclass.className",searhname);
			}else if(type.equals("less_search")){
				searhMap.put("LIKE_xbclass.xbCourse.courseName",searhname);
			}else if(type.equals("teacher_search")){
				searhMap.put("LIKE_sysemployee.employeeName",searhname);
			}else if(type.equals("class_room_search")){
				searhMap.put("LIKE_xbclassroom.classroomName",searhname);
			}else if(type.equals("student_inclass_search")){
				Map<String,Object> xsrnmap = new HashMap<>();
				xsrnmap.put("LIKE_xbStudent.studentName",searhname);
				List<XbStudentRelationViewNew> nlist = xbStudentService.getxbStudentRelationViewNewList(xsrnmap);
				if(nlist.size()>0){
					XbStudentRelationViewNew xsr =nlist.get(0);
					searhMap.put("EQ_classId",xsr.classId);
				}

			}

		}
		//授课模式
		String courtype = (String)resultMap.get("courtype");
		if(null==courtype){
			courtype = "-1";
		}else if(!courtype.equals("-1")){
			searhMap.put("EQ_wayOfTeaching",courtype);
		}
		//授课模式
		String courseTypeId = (String)resultMap.get("courseTypeId");
		if(null==courseTypeId){
			courseTypeId = "0";
		}else if(!courseTypeId.equals("0")){
			searhMap.put("EQ_xbclass.xbCourse.xbcoursetype.id",courseTypeId);
		}
		//班级
		String classId_combobox = (String)resultMap.get("classId_combobox");
		if(null==classId_combobox){
			classId_combobox = "0";
		}else if(!classId_combobox.equals("0")){
			searhMap.put("EQ_classId",classId_combobox);
		}
		//课程
		String courseId_combobox = (String)resultMap.get("courseId_combobox");
		if(null==courseId_combobox){
			courseId_combobox = "0";
		}else if(!courseId_combobox.equals("0")){
			searhMap.put("EQ_teacherId",courseId_combobox);
		}
		//开课开始日期
		String startDateTimeBegin = (String)resultMap.get("startDateTimeBegin");
		if(StringUtils.isEmpty(startDateTimeBegin)){
			startDateTimeBegin = DateUtil.weekDateFirstDay();
			searhMap.put("GTE_startDateTime",startDateTimeBegin);
		}else{
			searhMap.put("GTE_startDateTime",startDateTimeBegin);
		}

		//开课结束日期
		String startDateTimeEnd = (String)resultMap.get("startDateTimeEnd");
		if(StringUtils.isEmpty(startDateTimeEnd)){
			startDateTimeEnd = DateUtil.weekDateLastDay();
			searhMap.put("LTE_startDateTime",startDateTimeEnd);
		}else{
			searhMap.put("LTE_startDateTime",startDateTimeEnd);
		}
		model.addAttribute("searhname",searhname);
		model.addAttribute("type",type);
		model.addAttribute("courtype",courtype);
		model.addAttribute("courseTypeId",courseTypeId);
		model.addAttribute("classId_combobox",classId_combobox);
		model.addAttribute("courseId_combobox",courseId_combobox);
		model.addAttribute("startDateTimeBegin",startDateTimeBegin);
		model.addAttribute("startDateTimeEnd",startDateTimeEnd);
        /*//授课模式
        String subjectId = (String)resultMap.get("subjectId");
        if(null==subjectId){
            subjectId = "0";
        }else if(!subjectId.equals("0")){
            searhMap.put("EQ_subjectId",subjectId);
        }*/
		// model.addAttribute("subjectId",subjectId);
		String sysorgId_combobox = (String)resultMap.get("sysorgId_combobox");
		if(null==sysorgId_combobox) {
			sysorgId_combobox = "0";
		}
		model.addAttribute("sysorgId_combobox",sysorgId_combobox);
		return searhMap;
	}
	/*
	 * 跳转到记上课记录列表
	 * @return
	 */
	@RequestMapping("/getRecordClassRecordListByClass")
	public String getRecordClassRecordListByClass(@RequestParam(required = false) String data, ModelMap model,
											@PageableDefault(value = 20) Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();

		Page<XbRecordClassView> recordLists = studentService.getXbRecordClassdViewtoList(pageable,searhMap);
		for (int i = 0; i < recordLists.getContent().size(); i++) {
			XbRecordClassView xrcv = recordLists.getContent().get(i);
			BigDecimal periodnum = xrcv.periodnum;
			String classesId = xrcv.classId;
			Map<String, Object> searhMaps = new HashMap<>();
			searhMaps.put("EQ_classId", classesId);
			searhMaps.put("GTE_periodNum", new BigDecimal("0"));
			Pageable pageables = new PageRequest(0, 1, null);
			Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageables, searhMaps);
			if (classPage.getContent().size() > 0) {
				BigDecimal totalPeriodNum = classPage.getContent().get(0).totalPeriodNum;
				BigDecimal totalReceivable = classPage.getContent().get(0).totalReceivable;
				BigDecimal periodnums = recordLists.getContent().get(i).periodnum;
				BigDecimal receivable = totalReceivable.divide(totalPeriodNum,4,RoundingMode.HALF_UP).multiply(periodnums);
				recordLists.getContent().get(i).totalReceivable =receivable;
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

		return "wechat_classRecord";
	}
	@RequestMapping("/getRecordClassRecordListByClassReloding")
	public String getRecordClassRecordListByClassReloding(@RequestParam(required = false) String data, ModelMap model,
												  @PageableDefault(value = 10) Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();

		Page<XbRecordClassView> recordLists = studentService.getXbRecordClassdViewtoList(pageable,searhMap);
		for (int i = 0; i < recordLists.getContent().size(); i++) {
			XbRecordClassView xrcv = recordLists.getContent().get(i);
			BigDecimal periodnum = xrcv.periodnum;
			String classesId = xrcv.classId;
			Map<String, Object> searhMaps = new HashMap<>();
			searhMaps.put("EQ_classId", classesId);
			searhMaps.put("GTE_periodNum", new BigDecimal("0"));
			Pageable pageables = new PageRequest(0, 1, null);
			Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageables, searhMaps);
			if (classPage.getContent().size() > 0) {
				BigDecimal totalPeriodNum = classPage.getContent().get(0).totalPeriodNum;
				BigDecimal totalReceivable = classPage.getContent().get(0).totalReceivable;
				BigDecimal periodnums = recordLists.getContent().get(i).periodnum;
				BigDecimal receivable = totalReceivable.divide(totalPeriodNum,4,RoundingMode.HALF_UP).multiply(periodnums);
				recordLists.getContent().get(i).totalReceivable =receivable;
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
		return "wechat_classRecord::classRecordFra";
	}
	/*
	 * 跳转到记上课
	 * @return
	 */
	@RequestMapping("/classEdit")
	public String classEdit(@RequestParam(required = false) String classesId, ModelMap model,@PageableDefault(value = 1000) Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=classesId){
			searhMap.put("LIKE_classId",classesId);
		}
		XbClass classes = studentService.getXbClass(classesId);
		Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
		model.addAttribute("classPage",classPage);
		model.addAttribute("classes",classes);
		return "wechat_attendClass";
	}

	/**
	 * 查询学员记录列表，根据班级id和上课时间查询
	 * @param classId
	 * @param model
	 * @param pageable
	 * @return
	 */
	@RequestMapping("/accordingStudentRecord")
	public String accordingStudentRecord(@RequestParam(required = false) String classId,
							@RequestParam(required = false) String recordTime, ModelMap model, Pageable pageable) {
		try {
			Map<String,Object> searhMap = new HashMap<>();
			/*if(null!=classId){
				searhMap.put("LIKE_classId",classId);
			}*/
			XbClass classes = studentService.getXbClass(classId);
			searhMap.put("EQ_attendId",classId);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			searhMap.put("EQ_recordTime",sdf.parse(recordTime));
			Page<XbRecordClass> classPage = studentService.getRecordClassPage(pageable,searhMap);
			model.addAttribute("classPage",classPage);
			model.addAttribute("classes",classes);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
		}
		return "wechat_accordingStudent_record";
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
				Page<XbStudentRelationViewNew> xbStudentRelations = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
				String id = xbStudentRelations.getContent().get(0).id;
				XbStudentRelation xbStudentRelation = studentService.getXbStudentRelation(id);
						//Integer periodNum = xbStudentRelation.periodNum;
				BigDecimal bigDecimal = xbStudentRelation.periodNum;//剩余课时
				BigDecimal receivable = xbStudentRelation.receivable;//剩余学费
				//BigDecimal bigDecimal = new BigDecimal(periodNum.toString());
				money = deductPeriod.multiply(money);//课时*单课时金额=总扣除金额
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
	/*
	 * 查询上课记录按班级
	 * @return
	 */
	@RequestMapping("/getRecordClassListByClass")
	public String getRecordClassListByClass(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String classesName  = (String)resultMap.get("classesName");

		List recordLists = studentService.getXbRecordClassdtoList(pageable.getOffset(),pageable.getPageSize());
		int totalElements = studentService.findRecordTotalCount();
		int number = pageable.getPageNumber();
		int size = pageable.getPageSize();
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
		return "attendClass::accordingClass";
	}

}
