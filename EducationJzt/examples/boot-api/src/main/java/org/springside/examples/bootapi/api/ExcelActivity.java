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
import org.springside.examples.bootapi.domain.XbStudentRelation;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springside.examples.bootapi.ToolUtils.ExcelData;
import org.springside.examples.bootapi.ToolUtils.ExportExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


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
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).establishNum);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).studentCount);
			xbRecordClassViewList1.add(xbRecordClassViewList.get(i).periodnum);
			String classesId = xbRecordClassViewList.get(i).classId;
			Map<String,Object> searhMap = new HashMap<>();
			searhMap.put("EQ_classId",classesId);
			Pageable pageable = new PageRequest(0, 1, null);
			Page<XbStudentRelation> classPage = studentService.getXbStudentRelationList(pageable,searhMap);
			if(classPage.getContent().size()>0){
				BigDecimal totalPeriodNum = classPage.getContent().get(0).totalPeriodNum;
				BigDecimal totalReceivable = classPage.getContent().get(0).totalReceivable;
				BigDecimal receivable = totalReceivable.divide(totalPeriodNum).multiply(new BigDecimal(xbRecordClassViewList.get(i).periodnum));
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
		ExportExcelUtils.exportExcel(response,new Date()+"记上课.xlsx",datas);
	}

	/*
	 * 查询上课记录按班级
	 * @return
	 */
	@RequestMapping("/getRecordClassListByClass")
	public List<XbRecordClassView> getRecordClassListByClass(String data){
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
}
