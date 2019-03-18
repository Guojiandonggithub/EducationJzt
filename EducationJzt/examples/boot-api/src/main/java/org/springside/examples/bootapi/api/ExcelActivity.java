package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.ExcelData;
import org.springside.examples.bootapi.ToolUtils.ExportExcelUtils;
import org.springside.examples.bootapi.domain.XbClass;
import org.springside.examples.bootapi.domain.XbRecordClassView;
import org.springside.examples.bootapi.domain.XbStudentRelationViewNew;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@RestController
@RequestMapping("excel")
public class ExcelActivity {

	@Autowired
	private XbStudentService studentService;

	@RequestMapping(value = "/recordCourseExcel", method = RequestMethod.GET)
	public void excel(HttpServletResponse response,@RequestParam(required = false) String data) throws Exception {
		ExcelData datas = new ExcelData();
		datas.setName("记上课");
		List<String> titles = new ArrayList();
		titles.add("上课校区");
		titles.add("报读科目");
		titles.add("代课老师");
		titles.add("班级名称");
		titles.add("上课时间");
		titles.add("班级人数");
		titles.add("上课人数");
		titles.add("课时数");
		titles.add("课时费");
		datas.setTitles(titles);
		List<List<Object>> rows = new ArrayList();
		List<XbRecordClassView> xbRecordClassViewList = getRecordClassListByClass(data);
		for (int i = 0; i < xbRecordClassViewList.size(); i++) {
			List<Object> xbRecordClassViewList1 = new ArrayList();
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).organName);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).courseTypeNname);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).employeeName);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).className);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).recordTime);
			String classesId = xbRecordClassViewList.get(i).classId;
			Map<String,Object> searhMap = new HashMap<>();
			searhMap.put("EQ_classId",classesId);
			searhMap.put("GTE_periodNum", new BigDecimal("0"));
			Pageable pageable = new PageRequest(0, 1, null);
			Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
			xbRecordClassViewList1.add(classPage.getTotalElements());
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).studentCount);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).periodnum);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).totalReceivable);
			rows.add(xbRecordClassViewList1);
		}
		datas.setRows(rows);
		//生成本地
        /*File f = new File("c:/test.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*/
		ExportExcelUtils.exportExcel(response,"记上课.xlsx",datas);
	}

	@RequestMapping(value = "/studentRecordExcel", method = RequestMethod.GET)
	public void studentRecordExcel(HttpServletResponse response,@RequestParam(required = false) String data) throws Exception {
		ExcelData datas = new ExcelData();
		datas.setName("学员记录");
		List<String> titles = new ArrayList();
		titles.add("姓名");
		titles.add("手机号");
		titles.add("所属校区");
		titles.add("所属关系");
		titles.add("余额");
		titles.add("欠费");
		titles.add("报读课程");
		titles.add("教师");
		titles.add("班级");
		titles.add("剩余课时");
		titles.add("报名时间");
		datas.setTitles(titles);
		List<List<Object>> rows = new ArrayList();
		List<XbStudentRelationViewNew> xbStudentRelationViewNewList = getStudentList(data);
		for (int i = 0; i < xbStudentRelationViewNewList.size(); i++) {
			List<Object> xbRecordClassViewList1 = new ArrayList();
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbStudent.studentName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbStudent.contactPhone);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).sysOrgans.organName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbStudent.contactRelation);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbStudent.surplusMoney);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbStudent.paymentMoney);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbCourse.courseName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).employeeName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).className);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).periodNum);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String enrollDate="";
			if(xbStudentRelationViewNewList.get(i).enrollDate!=null){
				enrollDate = xbStudentRelationViewNewList.get(i).enrollDate.toString().substring(0,10);
			}
			xbRecordClassViewList1.add(enrollDate);
			rows.add(xbRecordClassViewList1);
		}
		datas.setRows(rows);
		//生成本地
        /*File f = new File("c:/test.xlsx");
        FileOutputStream out = new FileOutputStream(f);
        ExportExcelUtils.exportExcel(data, out);
        out.close();*/
		ExportExcelUtils.exportExcel(response,"学员信息.xlsx",datas);
	}

	@RequestMapping(value = "/ontTooneRecordExcel", method = RequestMethod.GET)
	public void ontTooneRecordExcel(HttpServletResponse response,@RequestParam(required = false) String data) throws Exception {
		ExcelData datas = new ExcelData();
		datas.setName("一对一教师学员信息");
		List<String> titles = new ArrayList();
		titles.add("所属校区");
		titles.add("教师姓名");
		titles.add("学员姓名");
		titles.add("所属课程");
		titles.add("剩余课时");
		titles.add("剩余金额");
		titles.add("学员状态");
		datas.setTitles(titles);
		List<List<Object>> rows = new ArrayList();
		String reportName = "";
		List<XbStudentRelationViewNew> xbStudentRelationViewNewList = getStudentListByOneToOne(data);
		for (int i = 0; i < xbStudentRelationViewNewList.size(); i++) {
			List<Object> xbRecordClassViewList1 = new ArrayList();
			reportName = xbStudentRelationViewNewList.get(i).sysOrgans.organName+xbStudentRelationViewNewList.get(i).employeeName;
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).sysOrgans.organName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).employeeName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbStudent.studentName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).xbCourse.courseName);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).periodNum);
			xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(i).receivable);
			Integer studentStart = xbStudentRelationViewNewList.get(i).studentStart;
			String status = "";
			if(studentStart==0){
				status = "在读";
			}else if(studentStart==1){
				status = "停课";
			}else if(studentStart==2){
				status = "转班";
			}else if(studentStart==3){
				status = "退费";
			}else if(studentStart==4){
				status = "结课";
			}
			xbRecordClassViewList1.add(status);
			rows.add(xbRecordClassViewList1);
		}
		datas.setRows(rows);
		ExportExcelUtils.exportExcel(response,reportName,datas);
	}

	@RequestMapping(value = "/classRecordExcel", method = RequestMethod.GET)
	public void classRecordExcel(HttpServletResponse response,@RequestParam(required = false) String data) throws Exception {
		ExcelData datas = new ExcelData();
		datas.setName("班级学员信息");
		List<String> titles = new ArrayList();
		titles.add("所属校区");
		titles.add("教师姓名");
		titles.add("学员姓名");
		titles.add("所属课程");
		titles.add("剩余课时");
		titles.add("剩余金额");
		titles.add("学员状态");
		String reportName = "没有查到数据";
		List<List<XbStudentRelationViewNew>> xbStudentRelationViewNewList = getStudentListByclass(data);
		XSSFWorkbook workbook = new XSSFWorkbook();
		/*ExportExcelUtils.exportExcel(response,reportName,datas);*/
		OutputStream out;
		try {
			if(xbStudentRelationViewNewList.size()>0){
				for (int j = 0; j < xbStudentRelationViewNewList.size(); j++) {
					List<List<Object>> rows = new ArrayList();
					List<XbStudentRelationViewNew> list = xbStudentRelationViewNewList.get(j);
					if(list.size()>0){
						reportName = list.get(0).sysOrgans.organName+list.get(0).employeeName;

						for (int i = 0; i < xbStudentRelationViewNewList.get(j).size(); i++) {
							List<Object> xbRecordClassViewList1 = new ArrayList();
							xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(j).get(i).sysOrgans.organName);
							xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(j).get(i).employeeName);
							xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(j).get(i).xbStudent.studentName);
							xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(j).get(i).xbCourse.courseName);
							xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(j).get(i).periodNum);
							xbRecordClassViewList1.add(xbStudentRelationViewNewList.get(j).get(i).receivable);
							Integer studentStart = xbStudentRelationViewNewList.get(j).get(i).studentStart;
							String status = "";
							if(studentStart==0){
								status = "在读";
							}else if(studentStart==1){
								status = "停课";
							}else if(studentStart==2){
								status = "转班";
							}else if(studentStart==3){
								status = "退费";
							}else if(studentStart==4){
								status = "结课";
							}
							xbRecordClassViewList1.add(status);
							rows.add(xbRecordClassViewList1);
						}
						ExportExcelUtils.exportExcel(workbook, j, xbStudentRelationViewNewList.get(j).get(0).employeeName+xbStudentRelationViewNewList.get(j).get(0).className, titles, rows);
					}
				}
			}else{
				ExportExcelUtils.exportExcel(workbook, 0, "没有数据", titles, new ArrayList());
			}
			//response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(reportName + ".xlsx"));
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(reportName + ".xlsx", "utf-8"));
			response.setHeader("content-Type", "application/vnd.ms-excel");
			out = response.getOutputStream();
			workbook.write(out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 查询上课记录按班级
	 * @return
	 */
	private List<XbRecordClassView> getRecordClassListByClass(String data){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,Map.class);
		}
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
		List<XbRecordClassView> recordLists = studentService.getXbRecordClassdViewtoList(searhMap);
		return recordLists;
	}

	/*
	 * 查询学员信息（到期学员模块导出）
	 * @return
	 */
	private List<XbStudentRelationViewNew> getStudentList(String data){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,searhMap.getClass());
		}
		String organId = (String)resultMap.get("organIdDQ");
		String type = (String)resultMap.get("type");
		//课程类别
		String typeId = (String)resultMap.get("typeId");
		if(null==typeId){
			typeId = "0";
		}else if(!typeId.equals("0")){
			searhMap.put("EQ_xbCourse.xbcoursetype.id",typeId);
		}
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
			searhMap.put("LIKE_employeeName",TeacherNameCla);
		}
		//教师名称
		String studentStart = (String)resultMap.get("studentStart");
		if(StringUtils.isEmpty(studentStart) || studentStart.equals("10")){
			studentStart = "10";
		}else {
			searhMap.put("EQ_studentStart",Integer.parseInt(studentStart));
		}
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
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(searhMap);

		return xbStudentPage;
	}

	/*
	 * 查询学员信息（一对一模块导出）
	 * @return
	 */
	private List<XbStudentRelationViewNew> getStudentListByOneToOne(String data){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,searhMap.getClass());
		}
		//教师名称
		String TeacherNameCla = (String)resultMap.get("TeacherNameCla");
		if(StringUtils.isNotEmpty(TeacherNameCla)){
			searhMap.put("LIKE_employeeName",TeacherNameCla);
		}
		List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(searhMap);

		return xbStudentPage;
	}

	/*
	 * 查询学员信息（班级模块导出）
	 * @return
	 */
	private List<List<XbStudentRelationViewNew>> getStudentListByclass(String data){
		Map<String,Object> resultMap = new HashMap<>();
		Map<String,Object> searhMap = new HashMap<>();
		Map<String,Object> searhXbStudentRelationMap = new HashMap<>();
		List<List<XbStudentRelationViewNew>> list = new ArrayList<>();
		if(null!=data){
			resultMap = com.alibaba.fastjson.JSONObject.parseObject(data,searhMap.getClass());
		}
		//教师名称
		String TeacherNameCla = (String)resultMap.get("TeacherNameCla");
		if(StringUtils.isNotEmpty(TeacherNameCla)){
			searhMap.put("EQ_teacher.employeeName",TeacherNameCla);
		}
		List<XbClass> xbClassList = studentService.findXbClassListAll(searhMap);
		for (int i = 0; i <xbClassList.size() ; i++) {
			String classId = xbClassList.get(i).id;
			searhXbStudentRelationMap.put("EQ_classId",classId);
			List<XbStudentRelationViewNew> xbStudentPage = studentService.getXbRelationList(searhXbStudentRelationMap);
			list.add(xbStudentPage);
		}
		return list;
	}

}
