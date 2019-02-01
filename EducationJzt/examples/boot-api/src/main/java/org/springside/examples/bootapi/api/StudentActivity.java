package org.springside.examples.bootapi.api;

import com.websuites.core.response.IResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.HashCodeHelper;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.ToolUtils.common.util.UtilTools;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.dto.XbClassDto;
import org.springside.examples.bootapi.service.*;

import javax.rmi.CORBA.Util;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

	@Autowired
	public XbCourseTypeService xbCourseTypeService;

	@Autowired
	public XbAttendClassService xbAttendClassService;
	@Autowired
	public XbSubjectService xbSubjectService;
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
		List<XbCourseType> xbCourseTypeList = xbCourseTypeService.findXbCourseTypeList(searhMap);
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
		model.addAttribute("xbCourseTypeList",xbCourseTypeList);
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
			xbclass.isEnd = "1";
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
		Map<String,Object> searchParamsview = new HashMap<>();
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
			searchParamsview.put("EQ_organId",organId);
		}
		if(null==type){
			type = "AZ";
		}
		if(null!=nameormobile&&!nameormobile.equals("")){
			if(type.equals("AZ")){
				searhMap.put("LIKE_xbStudent.studentName",nameormobile);
				searchParamsview.put("LIKE_studentName",nameormobile);
			}else{
				searhMap.put("LIKE_xbStudent.contactPhone",nameormobile);
				searchParamsview.put("LIKE_contactPhone",nameormobile);
			}
		}
		String enrollDateSearch = (String)resultMap.get("enrollDateSearch");
		String enrollDateSearchEnd = (String)resultMap.get("enrollDateSearchEnd");
		Date startdate = new Date();
		Date enddate = new Date();

		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotEmpty(enrollDateSearch)){
				startdate = sdf.parse(enrollDateSearch);
				searhMap.put("GTE_enrollDate",startdate);
				searchParamsview.put("GTE_enrollDate",startdate);
			}
			if(null==enrollDateSearch){
				enrollDateSearch = DateUtil.weekDateFirstDay();
				startdate = sdf.parse(DateUtil.weekDateFirstDay());
				searhMap.put("GTE_enrollDate",startdate);
				searchParamsview.put("GTE_enrollDate",startdate);
			}
			if(StringUtils.isNotEmpty(enrollDateSearchEnd)){
				enddate = sdf.parse(enrollDateSearchEnd);
				searhMap.put("LTE_enrollDate",enddate);
				searchParamsview.put("LTE_enrollDate",enddate);
			}
			if(null==enrollDateSearchEnd){
				enddate = sdf.parse(DateUtil.weekDateLastDay());
				enrollDateSearchEnd = DateUtil.weekDateLastDay();
				searhMap.put("LTE_enrollDate",enddate);
				searchParamsview.put("LTE_enrollDate",enddate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*List studentlist = new ArrayList<>();
		if(StringUtils.isEmpty(enrollDateSearch)){
			studentlist =	studentService.getAllStudentListNoDate();
		}else{
			studentlist =	studentService.getAllStudentListStartAndEnd(startdate,enddate);
		}*/
		List<XbStudentRelationView> studentlist = studentService.getxbStudentRelationViewList(searchParamsview);
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
		Map<String,Object> studentMap = new HashMap<>();
		/*Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,studentMap);*/
		model.addAttribute("studentlistsize",studentlist.size());
		model.addAttribute("xbStudentPage",xbStudentPage);
		/*model.addAttribute("xbStudentsPage",xbStudentsPage);*/
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("studentcurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		model.addAttribute("enrollDateSearch",enrollDateSearch);
		model.addAttribute("enrollDateSearchEnd",enrollDateSearchEnd);
		return "student";
	}

	/**
	 * 查询分班列表
	 * @return
	 */
	@RequestMapping("/getDividelist")
	public String getDividelist(@RequestParam(required = false) String data,ModelMap model, Pageable pageable){
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
		searhMap.put("NEQ_classId","");
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
		Map<String,Object> studentMap = new HashMap<>();
		Page<XbStudent> xbStudentsPage = studentService.getXbStudentList(pageable,studentMap);
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("xbStudentsPage",xbStudentsPage);
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("studentcurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		return "student::divideStu";
	}

	/**
	 * 跳转到一对一
	 * @return
	 */
	@RequestMapping("/getOneToOneList")
	public String getOneToOneList(@RequestParam(required = false) String room,
								  @RequestParam(required = false) String data,
								  ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);
		searhMap.put("EQ_xbCourse.type","1");
		String organId = (String)resultMap.get("organclaId");
		if(null==organId){
			organId = "0";
		}else if(!organId.equals("0")){
			searhMap.put("EQ_organId",organId);
		}
		//教师名称
		String TeacherNameCla = (String)resultMap.get("TeacherNameCla");
		if(StringUtils.isNotEmpty(TeacherNameCla)){
			searhMap.put("LIKE_employeeName",TeacherNameCla);
		}
		//课程类别
		String typeId = (String)resultMap.get("typeId");
		if(null==typeId){
			typeId = "0";
		}else if(!typeId.equals("0")){
			searhMap.put("EQ_xbCourse.xbcoursetype.id",typeId);
		}
		//学员状态
		String studentStart = (String)resultMap.get("studentStart");
		if(StringUtils.isEmpty(studentStart)){
			studentStart = "100";
		}else if(!studentStart.equals("100")){
			searhMap.put("EQ_studentStart",Integer.parseInt(studentStart));
		}
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
		List<XbStudentRelationView> studentlist = studentService.getxbStudentRelationViewList(searhMap);
		model.addAttribute("studentlistsize",studentlist.size());
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("currentzise",xbStudentPage.getSize());
		//查询所有校区
		Map<String,Object> sorgsearmap = new HashMap<>();
		List<SysOrgans> sorganList = organsService.getOrgansListAll(sorgsearmap);
		model.addAttribute("sorganList",sorganList);
		model.addAttribute("organclaId",organId);
		model.addAttribute("studentStart",studentStart);
		model.addAttribute("typeId",typeId);
		model.addAttribute("TeacherNameCla",TeacherNameCla);
		Map<String,Object> searhtypeMap = new HashMap<>();
		List<XbCourseType> coursetypelist = xbCourseTypeService.findXbCourseTypeList(searhtypeMap);
		model.addAttribute("coursetypelist",coursetypelist);
		return "oneToOne";
	}
	/**
	 * 新建排课编辑框弹出
	 * @return
	 */
	@RequestMapping("/classToFindAll")
	public String oneToOneToFindAll(@RequestParam String id,ModelMap model){
		Pageable pageable = new PageRequest(0, 100);
		//获取学员信息
		XbStudentRelationViewNew xsr = studentService.getXbStudentRelationViewNewByid(id);
		model.addAttribute("xsr",xsr);
		//查询科目列表
		model.addAttribute("xbSubjectList",xbSubjectService.findSubjectAll());
		Map<String,Object> roomsearhMap = new HashMap<>();
		roomsearhMap.put("EQ_organId",xsr.organId);
		//获取教室下拉
		List<XbClassroom> xbClassroomList = studentService.getXbClassroomList(pageable,roomsearhMap).getContent();
		model.addAttribute("xbClassroomList",xbClassroomList);
		return "oneToOne::teacherfra";
	}
	/**
	 * 保存排课
	 * @return,
	 */
	@RequestMapping("/save_xbAttend_class")
	public void saveXbAttendClass(@RequestParam(required = false) String data,
								  HttpServletResponse resp,Model model){
		logger.info("保存排课");
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		try {
			JSONObject jsonObject = new JSONObject();
			if(null!=data){
				resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
			}
			IResult check = check(resultMap);
			if(check.isSuccessful()){
				IResult saveclassrs = saveOneToOneClass(resultMap);
				if(!saveclassrs.isSuccessful()){
					jsonObject.put("status",saveclassrs.isSuccessful()?"00":"11");
					jsonObject.put("msg",saveclassrs.getErrorMessage());
				}else{
					IResult rs = saveXbAttendClass(resultMap,saveclassrs);
					jsonObject.put("status",saveclassrs.isSuccessful()?"00":"11");
					jsonObject.put("msg",saveclassrs.getErrorMessage());
				}
			}else{
				jsonObject.put("status",check.isSuccessful()?"00":"11");
				jsonObject.put("msg",check.getErrorMessage());
			}

		HttpServletUtil.reponseWriter(jsonObject,resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 一对一排课保存班级
	 * @return
	 */
	private  IResult saveOneToOneClass(Map<String,Object> resultMap){
		String className = (String)resultMap.get("className");
		String startDateTime = (String)resultMap.get("startDateTime");
		String endDateTime = (String)resultMap.get("endDateTime");
		String classroomId = (String)resultMap.get("classroomId");
		String id = (String)resultMap.get("id");
		List<XbClass> list= studentService.findByXbStudentRalationId(id);
		XbClass newclass = new XbClass();
		if(list.size()>0){
			newclass = list.get(0);
		}else{
			//获取学员信息
			XbStudentRelationViewNew xsr = studentService.getXbStudentRelationViewNewByid(id);
			//保存班级（一对一）
			XbClass xbclass = new XbClass();

			xbclass.className = className;
			xbclass.organId = xsr.organId;
			xbclass.courseId = xsr.courseId;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				xbclass.classBeginDate = sdf.parse(startDateTime);
				xbclass.classEndDate = StringUtils.isEmpty(endDateTime)?null:sdf.parse(endDateTime);
				xbclass.preRecruitNum = 1;//预招人数
				xbclass.establishNum = 1;//成班人数
				xbclass.recruitState = "1";//招生状态 1-停止招生
				xbclass.isEnd = "0";//结班
				xbclass.studentNum = 1;
				xbclass.teacherNum = 1;
				xbclass.teacherId = xsr.teacherId;
				xbclass.classroomId = classroomId;
				xbclass.wayOfTeaching = "1";
				xbclass.xbStudentRalationId = id;
				xbclass.deleteStatus = "1";
				newclass = studentService.saveXbClass(xbclass);
				if(StringUtils.isEmpty(newclass.id)){
					return UtilTools.makerErsResults("一对一排课新建班级失败");
				}
			} catch (ParseException e) {
				e.printStackTrace();
				return UtilTools.makerErsResults("一对一排课新建班级异常");
			}
		}
		Map map = new HashMap();
		map.put("id",newclass.id);
		map.put("teacherId",newclass.teacherId);
		map.put("wayOfTeaching",newclass.wayOfTeaching);
		return UtilTools.makerSusResults("一对一排课新建班级成功",map);
	}

	/**
	 * 校验教室+时间是否冲突
	 * @param resultMap
	 * @return
	 */
	private IResult check(Map<String,Object> resultMap){
		String startDateTime = (String)resultMap.get("startDateTime");
		String endDateTime = (String)resultMap.get("endDateTime");
		String weekType = (String)resultMap.get("weekType");
		String classroomId = (String)resultMap.get("classroomId");
		String timeInterval = (String)resultMap.get("timeInterval");
		String subjectId = (String)resultMap.get("subjectId");
		String classTheme = (String)resultMap.get("classTheme");
		String scheduleMode = (String)resultMap.get("scheduleMode");
		String msg = "";String status = "";
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(scheduleMode.equals("1")){
			if(checkCourseClassAndTimeInterval(classroomId,timeInterval,startDateTime,"")){
				XbClassroom xbc = studentService.getXbClassroomById(classroomId);
				msg = startDateTime+"的教室【"+xbc.classroomName+"】时间段【"+timeInterval+"】已存在，不能重复";
				return UtilTools.makerErsResults(msg);
			}
		}else{
			Date dBegin = null;Date dEnd = null;
			try {
				dBegin = sdf.parse(startDateTime);
				dEnd = sdf.parse(endDateTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}

			List<Date> lDate = DateUtil.findDates(dBegin, dEnd);
			if(weekType.indexOf("0")>-1){
				weekType = "1,2,3,4,5,6,7";
			}
			for (Date date : lDate)
			{
				if(DateUtil.doesItIncludeAWeek(weekType,sdf.format(date))){
					if(checkCourseClassAndTimeInterval(classroomId,timeInterval,sdf.format(date),"")){
						XbClassroom xbc = studentService.getXbClassroomById(classroomId);
						msg = sdf.format(date)+"的教室【"+xbc.classroomName+"】时间段【"+timeInterval+"】已存在，不能重复";
						return UtilTools.makerErsResults(msg);
					}
				}
			}
		}

		return UtilTools.makerSusResults("校验通过");
	}
	/**
	 * 保存一对一排课信息
	 * @return
	 */
	private IResult saveXbAttendClass(Map<String,Object> resultMap,IResult cla){
		String startDateTime = (String)resultMap.get("startDateTime");
		String endDateTime = (String)resultMap.get("endDateTime");
		String weekType = (String)resultMap.get("weekType");
		String classroomId = (String)resultMap.get("classroomId");
		String timeInterval = (String)resultMap.get("timeInterval");
		String subjectId = (String)resultMap.get("subjectId");
		String classTheme = (String)resultMap.get("classTheme");
		String scheduleMode = (String)resultMap.get("scheduleMode");
		String msg = "";String status = "";
		XbAttendClass rsxc = new XbAttendClass();
		SimpleDateFormat sdf = null;
		//单次
		if(scheduleMode.equals("1")){
			rsxc =  xbAttendClassService.saveXbAttendClass(OrganizationParamter(cla,startDateTime,endDateTime,classroomId,subjectId,classTheme,timeInterval));
		//重复
		}else{
			try {
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dBegin = sdf.parse(startDateTime);
				Date dEnd = sdf.parse(endDateTime);
				List<Date> lDate = DateUtil.findDates(dBegin, dEnd);
				if(weekType.indexOf("0")>-1){
					weekType = "1,2,3,4,5,6,7";
				}
				boolean is = true;
				/*for (Date date : lDate)
				{
					if(DateUtil.doesItIncludeAWeek(weekType,sdf.format(date))){
						if(checkCourseClassAndTimeInterval("",timeInterval,sdf.format(date),"")){
							XbClassroom xbc = studentService.getXbClassroomById("");
							msg = sdf.format(date)+"的教室【"+xbc.classroomName+"】时间段【"+timeInterval+"】已存在，不能重复";
							is = false;
							break;
						}
					}
				}*/
				for (Date date : lDate){
					if(DateUtil.doesItIncludeAWeek(weekType,sdf.format(date))){
						System.out.println("重复排课日期："+sdf.format(date));
						rsxc = xbAttendClassService.saveXbAttendClass(OrganizationParamter(cla,sdf.format(date),endDateTime,classroomId,subjectId,classTheme,timeInterval));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(StringUtils.isEmpty(rsxc.getId())){
			return UtilTools.makerErsResults("新建一对一排课失败");
		}
		return UtilTools.makerErsResults("新建一对一排课成功");
	}

	private XbAttendClass OrganizationParamter(IResult cla,String startDateTime,
							String endDateTime,String classroomId,String subjectId,String classTheme,
											   String timeInterval){
		XbAttendClass xbAttendClassnew = new XbAttendClass();
		xbAttendClassnew.classId = UtilTools.getResultMapValue(cla,"id");
		xbAttendClassnew.wayOfTeaching = UtilTools.getResultMapValue(cla,"wayOfTeaching");
		xbAttendClassnew.startDateTime = startDateTime;
		xbAttendClassnew.endDateTime = endDateTime;
		xbAttendClassnew.weekDay = DateUtil.dayForWeekChinses(startDateTime);
		xbAttendClassnew.teacherId = UtilTools.getResultMapValue(cla,"teacherId");
		xbAttendClassnew.tutorId = UtilTools.getResultMapValue(cla,"teacherId");
		xbAttendClassnew.classRoomId = classroomId;
		xbAttendClassnew.subjectId =subjectId;
		xbAttendClassnew.classTheme = classTheme;
		xbAttendClassnew.timeInterval = timeInterval;
		xbAttendClassnew.deleteStatus = "1";
		return xbAttendClassnew;
	}
	private boolean checkCourseClassAndTimeInterval(String classroomId,String timeInterval,String startDateTime,String id){
		Map<String,Object> searmap = new HashMap<>();
		searmap.put("EQ_xbclass.xbClassroom.id",classroomId);
		searmap.put("EQ_timeInterval",timeInterval);
		searmap.put("EQ_startDateTime",startDateTime);
		searmap.put("EQ_deleteStatus","1");
		if(StringUtils.isNotEmpty(id)){
			searmap.put("NEQ_id",id);
		}
		List<XbAttendClass> xbalist = xbAttendClassService.findXbAttendClassAll(searmap);
		if(xbalist!=null && xbalist.size()>0){
			return true;
		}
		return false;
	}
	/**
	 * 跳转到查询班级教室
	 * @return
	 */
	@RequestMapping("/getXbClassroomList")
	public String getXbClassroomList(@RequestParam(required = false) String room,
									 @RequestParam(required = false) String data,
									 @RequestParam(required = false) boolean isFinishClass,
									 ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		Map<String,Object> classSearhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
		String organId = (String)resultMap.get("organId");
		if(null==organId){
			organId = "0";
		}else if(!organId.equals("0")){
			classSearhMap.put("EQ_organId",organId);
		}
		//教师名称
		String TeacherNameCla = (String)resultMap.get("TeacherNameCla");
		if(StringUtils.isNotEmpty(TeacherNameCla)){
			classSearhMap.put("LIKE_teacher.employeeName",TeacherNameCla);
		}
		if(isFinishClass){
			String isFinishClassStr = "0";
			classSearhMap.put("EQ_isEnd",isFinishClassStr);
			model.addAttribute("finishClass",isFinishClassStr);
		}
		Page<XbClass> xbClassPage = studentService.getXbClassList(pageable,classSearhMap);
		model.addAttribute("organId",organId);
		model.addAttribute("TeacherNameCla",TeacherNameCla);
		if(null==room){
			room = "1";
		}
		model.addAttribute("room",room);

		Pageable pageables = new PageRequest(0, 20);
		Pageable pageable1;
		Pageable pageable2;
		if(room=="1"){
			pageable1 = pageables;
			pageable2 = pageable;
		}else if(room=="0"){
			pageable2 = pageables;
			pageable1 = pageable;
		}else{
			pageable1 = pageables;
			pageable2 = pageables;
		}

		Page<XbClassroom> xbClassroomPage = studentService.getXbClassroomList(pageable1,searhMap);
		List<XbClass> xbClassList = xbClassPage.getContent();
		int establishNumSum = 0;
		for (int i = 0; i < xbClassList.size(); i++) {
			String classId = xbClassList.get(i).id;
			Long establishNum = studentService.findAllDataByClassCount(classId);
			xbClassList.get(i).enrollNum = establishNum;
			establishNumSum += establishNum;
			String sktime="";
			List lists = xbAttendClassService.findListsByClassId(xbClassList.get(i).id);
			if(lists.size()>0){
				for (int j = 0; j < lists.size(); j++) {
					Object[] str = (Object[])lists.get(j);
					sktime = sktime+ str[0]+str[1]+"<br/>";
				}
			}
			xbClassList.get(i).sktime = sktime;
			System.out.println(sktime);//拼接成字符串付给班级
		}

		model.addAttribute("establishNumSum",establishNumSum);//总人数
		model.addAttribute("xbClassroomPage",xbClassroomPage);
		model.addAttribute("xbClassPage",xbClassPage);
		model.addAttribute("roomcurrentzise",xbClassroomPage.getSize());
		model.addAttribute("classcurrentzise",xbClassPage.getSize());
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		model.addAttribute("organsList",organsList);
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
            Page<XbStudentRelationViewNew> xbStudentRelationPage = studentService.getXbStudentRelationViewNewList(pageable,xbClassearhMap);
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
			String msg = studentService.finishClass(id);
			jsonObject.put("status","1");
			jsonObject.put("msg", msg);
			resp.setContentType("text/html;charset=UTF-8");
			resp.getWriter().println(jsonObject.toJSONString());
			resp.getWriter().close();
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}

	@RequestMapping("/finish/course/{id}")
	public void finishcourse(@PathVariable String id, HttpServletResponse resp,Pageable pageable){

		Map<String, Object> map  =  new HashMap<>();
		try {
            JSONObject jsonObject = new JSONObject();
			studentService.finishCourse(id);
			jsonObject.put("status","1");
			jsonObject.put("msg", "结课成功");
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
			BigDecimal su = (xbStudent.paymentMoney.add(xbStudent.registratioFee)).subtract(xbStudent.surplusMoney);
			BigDecimal pay = xbStudent.surplusMoney.subtract(xbStudent.paymentMoney.add(xbStudent.registratioFee));
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
			String id = xbStudent.id;
			if(null!=id&&!"".equals(id)){
				XbStudent xbStudent1 = studentService.getXbStudent(id);
				xbStudent.surplusMoney = xbStudent1.surplusMoney.add(su);
				xbStudent.paymentMoney = xbStudent1.paymentMoney.add(pay);
				xbStudent.totalPeriodNum = xbStudent.totalPeriodNum.add(xbStudent1.totalPeriodNum);
			}
            xbStudent.deleteStatus = "1";
			xbStudent = studentService.saveXbStudent(xbStudent);
			xbSupplementFee.studentId = xbStudent.id;
			String content = "";
			for (XbStudentRelation studentRelation : xbStudentRelationList) {
				studentRelation.studentId = xbStudent.id;
				XbCourse xbCourse = xbCourseService.findById(studentRelation.courseId);
				content = content+xbCourse.courseName +",";
				studentRelation.totalPeriodNum = studentRelation.periodNum;
				studentRelation.totalReceivable = studentRelation.receivable;
				studentRelation.studentStart = 0;//在读
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
			xbSupplementFee.paymentMoney = xbSupplementFee.paymentMoney.add(xbStudent.registratioFee);
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
	public String getManagementList(@RequestParam(required = false) String userName,ModelMap model, Pageable pageable){
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=userName){
			searhMap.put("LIKE_xbStudent.studentName",userName);
		}
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
	public String getClassList(@RequestParam(required = false) String type,@RequestParam(required = false) String orginId,@RequestParam(required = false) String classesName,ModelMap model, Pageable pageable){
		List<SysOrgans> organsList = organsService.getOrgansList();
		if(organsList.size()>0){
			Map<String,Object> xbCoursesearhMap = new HashMap<>();
			List<XbCourseType> xbCourseTypeList = xbCourseTypeService.findXbCourseTypeList(xbCoursesearhMap);
			if(null==orginId&&!orginId.equals("")){
				orginId = organsList.get(0).id;
			}
			xbCoursesearhMap.put("EQ_organId",orginId);
			if(null!=classesName&&!classesName.equals("")){
				xbCoursesearhMap.put("LIKE_className",classesName);
			}
			if(null==type||type.equals("")){
				type = xbCourseTypeList.get(0).id;
			}
			xbCoursesearhMap.put("EQ_xbCourse.xbcoursetype.id",type);
			Page<XbClass> xbClassPage = studentService.getXbClassList(pageable,xbCoursesearhMap);
            List<XbClass> xbClassList = xbClassPage.getContent();
            List<XbClass> xbClassPages = new ArrayList<>();
            for (int i = 0; i < xbClassList.size(); i++) {
                Map<String,Object> xbClassearhMap = new HashMap<>();
                xbClassearhMap.put("EQ_classId",xbClassList.get(i).id);
                Page<XbStudentRelationViewNew> xbStudentRelationPage = studentService.getXbStudentRelationViewNewList(pageable,xbClassearhMap);
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
			model.addAttribute("xbCourseTypeList",xbCourseTypeList);
		}
		return "enroll::classlist";
	}

	/**
	 * 获取课程列表
	 * @return
	 */
	@RequestMapping("/getCourseList")
	public String getCourseList(@RequestParam(required = false) String bjclassId,@RequestParam(required = false) String orginId,@RequestParam(required = false) String courseName,ModelMap model, Pageable pageable){
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
			if(null!=bjclassId&&!bjclassId.equals("")){
				xbCoursesearhMap.put("EQ_xbCourse.courseTypeId",studentService.getXbClass(bjclassId).xbCourse.courseTypeId);
			}
			xbCoursesearhMap.put("EQ_deleteStatus","1");
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
	public String chooseCourse(@RequestParam(required = false) String classId,@RequestParam(required = false) String courseIds,ModelMap model, Pageable pageable){
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
		BigDecimal moneys = new BigDecimal(0);
		Integer num = 0;
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
						XbCoursePreset xbCoursePreset = xbCourseList.get(j);
						if(xbCoursePreset.getId().equals(xbStudentRelationList.get(i).indexs)){
							xbCoursePreset.xbClassList = classList;
							BigDecimal totalmoney = xbCoursePreset.money;//每个课程总金额
							if(xbCoursePreset.xbCourse.chargingMode.equals("0")){
								totalmoney = xbCoursePreset.money.multiply(new BigDecimal(xbCoursePreset.periodNum));
								xbCoursePreset.lsmoney = totalmoney;
							}
							moneys = moneys.add(totalmoney);
							num = num + xbCoursePreset.periodNum;
							xbCoursePresetList.add(xbCoursePreset);
						}
					}
				}
			}
			model.addAttribute("xbCourseLists",xbCoursePresetList);
			model.addAttribute("money",moneys);
			model.addAttribute("periodNum",num);//总课时
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
		searhMap.put("NEQ_xbStudent.paymentMoney","0");
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
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
		String organId = (String)resultMap.get("organIdDQ");
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
		//教师名称
		String TeacherNameCla = (String)resultMap.get("TeacherNameCla");
		if(StringUtils.isNotEmpty(TeacherNameCla)){
			searhMap.put("LIKE_xbClass.teacher.employeeName",TeacherNameCla);
		}
		//教师名称
		String studentStart = (String)resultMap.get("studentStart");
		if(StringUtils.isEmpty(studentStart) || studentStart.equals("10")){
			studentStart = "10";
		}else {
			searhMap.put("EQ_studentStart",Integer.parseInt(studentStart));
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		String totalPeriodNumStart = (String)resultMap.get("totalPeriodNumStart");
		String totalPeriodNumEnd = (String)resultMap.get("totalPeriodNumEnd");
		if(StringUtils.isNotEmpty(totalPeriodNumStart)){
			searhMap.put("GTE_periodNum",Integer.parseInt(totalPeriodNumStart));
		}else{
			searhMap.put("EQ_periodNum","0");
			totalPeriodNumStart = "0";
		}
		if(StringUtils.isNotEmpty(totalPeriodNumEnd)){
			searhMap.put("LTE_periodNum",Integer.parseInt(totalPeriodNumEnd));
		}else{
			searhMap.put("EQ_periodNum","0");
			totalPeriodNumEnd = "0";
		}
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
		model.addAttribute("expiryStuPage",xbStudentPage);
		model.addAttribute("organIdDQ",organId);
		model.addAttribute("TeacherNameCla",TeacherNameCla);
		model.addAttribute("studentStart",studentStart);
		model.addAttribute("organsList",organsList);
		model.addAttribute("expirycurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		model.addAttribute("totalPeriodNumStart",totalPeriodNumStart);
		model.addAttribute("totalPeriodNumEnd",totalPeriodNumEnd);
		//查询所有校区
		Map<String,Object> sorgsearmap = new HashMap<>();
		List<SysOrgans> sorganList = organsService.getOrgansListAll(sorgsearmap);
		model.addAttribute("sorganList",sorganList);
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
			Page<XbStudentRelationViewNew> xbCoursePage = studentService.getXbStudentRelationViewNewList(pageable, searhMap);
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
		Page<XbStudentRelationViewNew> xbStudentRelationPage = studentService.getXbStudentRelationViewNewList(pageable,searchParams);
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
		Map<String, Object> feeListSearchParams = new HashMap<>();
		searchParams.put("EQ_studentId",studentId);
		feeListSearchParams.put("EQ_studentId",studentId);
		Page<XbStudentRelationViewNew> xbStudentRelationPage = studentService.getXbStudentRelationViewNewList(pageable,searchParams);
		model.addAttribute("xbStudent",xbStudent);
		model.addAttribute("xbStudentRelationPage",xbStudentRelationPage);//课程列表
		Page<XbSupplementFee> XbSupplementFeePage = studentService.getXbSupplementFeeList(pageable,feeListSearchParams);
		model.addAttribute("XbSupplementFeePage",XbSupplementFeePage);//订单列表
		model.addAttribute("feecurrentzise",XbSupplementFeePage.getSize());
		Page<XbRecordClass> xbRecordClassPage = studentService.getRecordClassPage(pageable,feeListSearchParams);
		model.addAttribute("xbRecordClassPage",xbRecordClassPage);//上课记录
		model.addAttribute("accordingcurrentzise",xbRecordClassPage.getSize());
		return "stuinfoDetail";
	}

}
