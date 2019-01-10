package org.springside.examples.bootapi.api;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.domain.XbClass;
import org.springside.examples.bootapi.domain.XbCoursePreset;
import org.springside.examples.bootapi.domain.XbRecordClass;
import org.springside.examples.bootapi.domain.XbStudentRelation;
import org.springside.examples.bootapi.service.XbCoursePresetService;
import org.springside.examples.bootapi.service.XbStudentService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
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

	/*
	 * 跳转到记上课
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
		if(null!=classesName&&!classesName.equals("")){
			searhMap.put("LIKE_className",classesName);
		}
		Page<XbClass> classPage = studentService.getXbClassList(pageable,searhMap);
		model.addAttribute("classPage",classPage);
		model.addAttribute("classesName",classesName);
		model.addAttribute("currentzise",classPage.getSize());
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
				Integer periodNum = xbStudentRelation.periodNum;
				BigDecimal receivable = xbStudentRelation.receivable;
				BigDecimal bigDecimal = new BigDecimal(periodNum.toString());
				money = deductPeriod.multiply(money);
				receivable = receivable.subtract(money);
				bigDecimal = bigDecimal.subtract(deductPeriod);
				xbStudentRelation.periodNum = Integer.parseInt(bigDecimal.toString());
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
		} catch (IOException e) {
			logger.info(e.toString());
		}
	}


}
