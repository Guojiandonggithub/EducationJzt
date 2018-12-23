package org.springside.examples.bootapi.api;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.XbAttendClass;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.domain.XbSubject;
import org.springside.examples.bootapi.repository.XbAttendClassDao;
import org.springside.examples.bootapi.repository.XbSubjectDao;
import org.springside.examples.bootapi.service.XbAttendClassService;
import org.springside.examples.bootapi.service.XbSubjectService;

import javax.servlet.http.HttpServletResponse;

/**
 * 排课
 * Created by ZhangLei on 2018/12/15 0015
 */
@Controller
@RequestMapping(value="/jw_center_arranging_course")
public class JwCenterArrangingCoursesActivity {
    private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);
    @Autowired
    public XbAttendClassService xbAttendClassService;
    @Autowired
    public XbSubjectService xbSubjectService;
    /**
     * 跳转到排课
     * @return
     */
    @RequestMapping("/to_arranging_course")
    public String toArrangingCourse(ModelMap model){
        logger.info("跳转到排课");
        model.addAttribute("xbAttendList",xbAttendClassService.findXbAttendClassAll());
        model.addAttribute("xbSubjectList",xbSubjectService.findSubjectAll());

        return "courseArray";
    }
    /**
     * 保存排课
     * @return
     */
    @PostMapping("/save_xbAttend_class")
    public void saveXbAttendClass(@RequestBody XbAttendClass xbAttendClass,HttpServletResponse resp){
        logger.info("保存排课");
        JSONObject jsonObject = new JSONObject();
        XbAttendClass rsxc = xbAttendClassService.saveXbAttendClass(xbAttendClass);
        XbSubject xbsubject = xbSubjectService.findById(rsxc.getSubjectId());
        rsxc.setXbsubject(xbsubject!=null?xbsubject:new XbSubject());
        if(!StringUtils.isEmpty(rsxc.getId())){
            jsonObject.put("msg", "新建排课成功");
            jsonObject.put("status","0");
            jsonObject.put("rsxbAttendClass",com.alibaba.fastjson.JSONObject.toJSON(rsxc));
        }else{
            jsonObject.put("msg", "新建排课失败");
            jsonObject.put("status","1");
        }
        HttpServletUtil.reponseWriter(jsonObject,resp);
    }
    @PostMapping("/remove_xbAttend_class")
    public void removeCourse(@RequestBody XbAttendClass xbattendclass, HttpServletResponse resp){
        logger.info("删除课程");
        boolean rs = xbAttendClassService.removeXbAttendClass(xbattendclass);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除课程成功");
            jsonObject.put("xbattendclass", com.alibaba.fastjson.JSONObject.toJSON(xbattendclass));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除课程失败");
        }
        logger.info("删除课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
}
