package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
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
import org.springside.examples.bootapi.domain.XbRecordClassView;
import org.springside.examples.bootapi.domain.XbStudentRelationViewNew;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
			//searhMap.put("GTE_periodNum",new BigDecimal("0"));
			Pageable pageable = new PageRequest(0, 1, null);
			Page<XbStudentRelationViewNew> classPage = studentService.getXbStudentRelationViewNewList(pageable,searhMap);
			xbRecordClassViewList1.add(classPage.getTotalElements());
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).studentCount);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).periodnum);
			if(classPage.getContent().size()>0){
				BigDecimal totalPeriodNum = classPage.getContent().get(0).totalPeriodNum;
				BigDecimal totalReceivable = classPage.getContent().get(0).totalReceivable;
				BigDecimal receivable = totalReceivable.divide(totalPeriodNum,2,RoundingMode.HALF_UP).multiply(xbRecordClassViewList.get(i).periodnum);
				xbRecordClassViewList1.add(receivable);
			}else{
				xbRecordClassViewList1.add("");
			}
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

}
