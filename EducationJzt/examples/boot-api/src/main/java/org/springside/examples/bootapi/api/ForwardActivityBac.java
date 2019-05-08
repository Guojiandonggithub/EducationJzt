package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.domain.*;
import org.springside.examples.bootapi.service.EmployeeService;
import org.springside.examples.bootapi.service.OrgansService;
import org.springside.examples.bootapi.service.RoleService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/forwardbak")
public class ForwardActivityBac {

	private static Logger logger = LoggerFactory.getLogger(ForwardActivityBac.class);
	@Autowired
	private EmployeeService accountService;
	@Autowired
	private OrgansService organsService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private XbStudentService studentService;

	@RequestMapping("/index")
	public String index(HttpSession session,ModelMap map,@RequestParam(required = false) String data, ModelMap model,
						@PageableDefault(value = 10)Pageable pageable) {
		//getXbStudentList(data,model,pageable);
		getIndexTotalData(data,model);
		SysEmployee sysEmployee = (SysEmployee)session.getAttribute("sysEmployee");
		session.setAttribute("userName",sysEmployee.employeeName);
		session.setAttribute("organName",sysEmployee.sysOrgans.organName);
		session.setAttribute("roleName",sysEmployee.sysRole.roleName);
		return "indexs";
	}

	@RequestMapping("/getDetailsTodayList")
	public String getSameMonthList(HttpSession session,ModelMap map,@RequestParam(required = false) String data, ModelMap model,
						@PageableDefault(value = 10)Pageable pageable) {
		getXbStudentList(data,model,pageable);
		SysEmployee sysEmployee = (SysEmployee)session.getAttribute("sysEmployee");
		session.setAttribute("userName",sysEmployee.employeeName);
		session.setAttribute("organName",sysEmployee.sysOrgans.organName);
		session.setAttribute("roleName",sysEmployee.sysRole.roleName);
		return "studentDetail";
	}

	private void getIndexTotalData(@RequestParam(required = false) String dataStr,ModelMap model){
		Map<String,Object> searchXbSupplementFee = new HashMap<>();
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		BigDecimal surplusMoney = new BigDecimal(0.00);
		BigDecimal registratiofee = new BigDecimal(0.00);
		BigDecimal zeroMoney = new BigDecimal(0.00);
		Date enddate  = null;
		if(null!=dataStr){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(dataStr,searhMap.getClass());
		}
		String organId = (String)resultMap.get("organId");
		String enrollDateSearch = (String)resultMap.get("enrollDateSearch");
		if(null==organId){
			organId = "0";
		}else if(!organId.equals("0")){
			searhMap.put("EQ_organId",organId);
			searchXbSupplementFee.put("EQ_xbStudent.organId",organId);
		}
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotEmpty(enrollDateSearch)){
				enddate = sdf.parse(enrollDateSearch);
				searchXbSupplementFee.put("EQ_paymentDate",enddate);
			}else{
				enrollDateSearch = DateUtil.getDateStr(new Date());
				String dates = DateUtil.getDateStr(new Date());
				enddate = sdf.parse(dates);
				searchXbSupplementFee.put("EQ_paymentDate",enddate);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}


		List<XbSupplementFee> xbSupplementFeeList = studentService.getXbSupplementFeeList(searchXbSupplementFee);
		for(XbSupplementFee xsrvnf:xbSupplementFeeList){
			if(xsrvnf.type.equals("0")||xsrvnf.type.equals("2")){
				surplusMoney = surplusMoney.add(xsrvnf.surplusMoney==null?zeroMoney:xsrvnf.surplusMoney);
			}
			registratiofee = registratiofee.add(xsrvnf.registratioFee==null?zeroMoney:xsrvnf.registratioFee);
			/*if(xsrvnf.type.equals("5")){
				tuifeifee = tuifeifee.add(xsrvnf.paymentMoney==null?zeroMoney:xsrvnf.paymentMoney);
			}*/
		}
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("surplusMoney",surplusMoney);
		model.addAttribute("registratiofee",registratiofee);
		model.addAttribute("indextotalMoney",surplusMoney.add(registratiofee));
		model.addAttribute("enrollDateSearch",enrollDateSearch);
	}

	public void getXbStudentList(@RequestParam(required = false) String data, ModelMap model, Pageable pageable){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		Map<String,Object> searchParamsview = new HashMap<>();
		Map<String,Object> searchXbSupplementFee = new HashMap<>();
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
			searchXbSupplementFee.put("EQ_xbStudent.organId",organId);
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
		String status = (String)resultMap.get("status");
		//String enrollDateSearchEnd = (String)resultMap.get("enrollDateSearchEnd");
		Date date  = null;
		Date enddate  = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtils.isNotEmpty(status)){
				if(status.equals("today")){
					enddate = sdf.parse(enrollDateSearch);
					searhMap.put("EQ_enrollDate",enddate);
					searchParamsview.put("EQ_enrollDate",enddate);
					searchXbSupplementFee.put("EQ_paymentDate",enddate);
				}else{
					String startTime = DateUtil.getFirstDayOfGivenMonth(enrollDateSearch);
					String endTime = DateUtil.getLastDayOfMonth(enrollDateSearch);
					date = sdf.parse(startTime);
					enddate = sdf.parse(endTime);
					searhMap.put("GTE_enrollDate",date);
					searchParamsview.put("GTE_enrollDate",date);
					searchXbSupplementFee.put("GTE_paymentDate",date);
					searhMap.put("LTE_enrollDate",enddate);
					searchParamsview.put("LTE_enrollDate",enddate);
					searchXbSupplementFee.put("LTE_paymentDate",enddate);
				}
			}else{
				enrollDateSearch = DateUtil.getDateStr(new Date());
				String dates = DateUtil.getDateStr(new Date());
				date = sdf.parse(dates);
				searhMap.put("EQ_enrollDate",date);
				searchParamsview.put("EQ_enrollDate",date);
				searchXbSupplementFee.put("EQ_paymentDate",sdf.parse(dates.substring(0,10)));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		/*List studentlist = new ArrayList<>();
		if(StringUtils.isEmpty(enrollDateSearch)){
			studentlist =	studentService.getAllStudentListNoDate();
		}else{
			studentlist =	studentService.getAllStudentList(date);
		}*/
		//学员状态
		String studentStart = (String)resultMap.get("studentStart");
		if(StringUtils.isEmpty(studentStart)){
			studentStart = "100";
		}else if(!studentStart.equals("100")){
			searhMap.put("EQ_studentStart",Integer.parseInt(studentStart));
			searchParamsview.put("EQ_studentStart",Integer.parseInt(studentStart));
		}
		List<XbSupplementFee> xbSupplementFeeList = studentService.getXbSupplementFeeList(searchXbSupplementFee);
		List<XbStudentRelationView> studentlist = studentService.getxbStudentRelationViewList(searchParamsview);
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<XbStudentRelationViewNew> xbStudentPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
		Map<String,Object> studentMap = new HashMap<>();
		BigDecimal totalMoney = new BigDecimal(0.00);
		BigDecimal surplusMoney = new BigDecimal(0.00);
		BigDecimal registratiofee = new BigDecimal(0.00);
		BigDecimal tuifeifee = new BigDecimal(0.00);
		BigDecimal zeroMoney = new BigDecimal(0.00);
		for(XbStudentRelationView xsrvn:studentlist){
			totalMoney = totalMoney.add(xsrvn.receivable==null?zeroMoney:xsrvn.receivable);
	}
		for(XbSupplementFee xsrvnf:xbSupplementFeeList){
			if(xsrvnf.type.equals("0")||xsrvnf.type.equals("2")){
				surplusMoney = surplusMoney.add(xsrvnf.surplusMoney==null?zeroMoney:xsrvnf.surplusMoney);
			}
			registratiofee = registratiofee.add(xsrvnf.registratioFee==null?zeroMoney:xsrvnf.registratioFee);
			if(xsrvnf.type.equals("5")){
				tuifeifee = tuifeifee.add(xsrvnf.paymentMoney==null?zeroMoney:xsrvnf.paymentMoney);
			}
		}
		model.addAttribute("surplusMoney",surplusMoney);
		model.addAttribute("registratiofee",registratiofee);
		model.addAttribute("indextotalMoney",surplusMoney.add(registratiofee));
		model.addAttribute("tuifeifee",new BigDecimal(0.00).subtract(tuifeifee));
		model.addAttribute("studentStart",studentStart);
		model.addAttribute("totalMoney",totalMoney.toString());
		model.addAttribute("xbStudentPage",xbStudentPage);
		model.addAttribute("studentlistsize",studentlist.size());
		model.addAttribute("organId",organId);
		model.addAttribute("organsList",organsList);
		model.addAttribute("studentcurrentzise",xbStudentPage.getSize());
		model.addAttribute("nameormobile",nameormobile);
		model.addAttribute("type",type);
		model.addAttribute("enrollDateSearch",enrollDateSearch);
		//model.addAttribute("enrollDateSearchEnd",enrollDateSearchEnd);
	}
	@RequestMapping("/organization")
	public String organization(ModelMap model,Pageable pageable) {
		logger.info("跳转到课程");
		Page<SysOrgans> organsPage = organsService.getOrgansList(pageable);
		logger.info("查询到机构organsPage="+organsPage);
		model.addAttribute("organsPage",organsPage);
		return "organization";
	}

	@RequestMapping("/management")
	public String management() {
		return "management";
	}

	@RequestMapping("/employee")
	public String employee(Pageable pageable, ModelMap model) {
		Map<String, Object> searchParams = new HashMap<>();
		Iterable<SysOrgans> organsList = organsService.getOrgansList();
		Page<SysRole> sysRolePage = roleService.getRoleList(pageable,searchParams);
		logger.info("查询到机构organsList="+organsList);
		model.addAttribute("organsList",organsList);
		model.addAttribute("employee",new SysEmployee());
		model.addAttribute("employeeSub",new SysEmployeeSub());
		model.addAttribute("sysRolePage",sysRolePage.getContent());
		return "employee";
	}

	@RequestMapping("/student")
	public String student() {
		return "student";
	}

	@RequestMapping("/classes")
	public String classes() {
		return "classes";
	}

	@RequestMapping("/oneToOne")
	public String oneToOne() {
		return "oneToOne";
	}

	@RequestMapping("/courseArray")
	public String courseArray() {
		return "courseArray";
	}

	@RequestMapping("/lessonTable")
	public String lessonTable() {
		return "lessonTable";
	}

	@RequestMapping("/attendClass")
	public String attendClass() {
		return "attendClass";
	}

	@RequestMapping("/course")
	public String course() {
		return "course";
	}

	@RequestMapping("/oneEdit")
	public String oneEdit() {
		return "oneEdit";
	}

	@RequestMapping("/subsidy")
	public String subsidy() {
		return "subsidy";
	}
	@RequestMapping("/stopClass")
	public String stopClass() {
		return "stopClass";
	}
	@RequestMapping("/repeat")
	public String repeat() {
		return "repeat";
	}
}
