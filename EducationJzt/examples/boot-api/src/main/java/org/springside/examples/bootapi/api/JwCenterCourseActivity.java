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
import org.springframework.web.bind.annotation.RequestParam;
import org.springside.examples.bootapi.ToolUtils.DateUtil;
import org.springside.examples.bootapi.ToolUtils.HttpServletUtil;
import org.springside.examples.bootapi.domain.XbCourse;
import org.springside.examples.bootapi.domain.XbCourseType;
import org.springside.examples.bootapi.domain.XbSubject;
import org.springside.examples.bootapi.service.XbCourseService;
import org.springside.examples.bootapi.service.XbCourseTypeService;
import org.springside.examples.bootapi.service.XbSubjectService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 教务中心-学员
 * Created by ZhangLei on 2018/12/15 0015
 */
@Controller
@RequestMapping(value="/jwcentercourse")
public class JwCenterCourseActivity {
    private static Logger logger = LoggerFactory.getLogger(AccountActivity.class);
    @Autowired
    public XbCourseService xbCourseService;
    @Autowired
    public XbSubjectService xbSubjectService;
    @Autowired
    public XbCourseTypeService xbCourseTypeService;
    /**
     * 跳转到课程列表
     * @return
     */
    @RequestMapping("/tocourse")
    public String toCourse(ModelMap model){
        logger.info("跳转到课程");
        List<XbCourse> list = xbCourseService.findCourseAll();
        logger.info("查询到课程list.size="+list.size());
        model.addAttribute("courselist",list);
        List<XbCourseType> coursetypelist = xbCourseTypeService.findCourseTypeAll();
        model.addAttribute("coursetypelist",coursetypelist);
        List<XbSubject> subjectlist = xbSubjectService.findSubjectAll();
        model.addAttribute("subjectlist",subjectlist);
        return "course";
    }
    /**
     * 跳转到新建课程
     * @return
     */
    @RequestMapping("/goaddcourse")
    public String goAddCourse(HttpServletRequest req,
            @RequestParam(value="courseId",defaultValue = "") String courseId, ModelMap model){
        logger.info("跳转到新建课程");
        List<XbCourseType> coursetypelist = xbCourseTypeService.findCourseTypeAll();
        model.addAttribute("coursetypelist",coursetypelist);
        List<XbSubject> subjectlist = xbSubjectService.findSubjectAll();
        model.addAttribute("subjectlist",subjectlist);
        if(!courseId.equals("")){
            model.addAttribute("xbCourse",xbCourseService.findById(courseId));
        }
        return "newLesson";
    }
    //@RequestMapping(path="/savecourse", method = RequestMethod.POST)
    @PostMapping("/savecourse")
    public void saveCourse(@RequestBody XbCourse xbcourse, HttpServletResponse resp){
        logger.info("新建课程");
        Date date  = new Date();
        xbcourse.setCreateDate(DateUtil.getDateStr(date));
        xbcourse.setCreateTime(DateUtil.getTimeStr(date));
        XbCourse entity =  xbCourseService.saveXbCourse(xbcourse);
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(entity.getId())){
            jsonObject.put("msg", "新建课程成功");
            jsonObject.put("status","0");
            jsonObject.put("cousrtype",com.alibaba.fastjson.JSONObject.toJSON(entity));

        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "新建课别失败");
        }
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建结束");
    }
    @PostMapping("/savecoursetype")
    public void saveCourseType(@RequestBody XbCourseType xbcoursetype,HttpServletResponse resp){
        logger.info("新建课程");
        XbCourseType entity =  xbCourseTypeService.saveXbCourseType(xbcoursetype);
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(entity.getId())){
            jsonObject.put("msg", "新建课程类别成功");
            jsonObject.put("status","0");
            jsonObject.put("cousrtype",com.alibaba.fastjson.JSONObject.toJSON(entity));
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "新建课程类别失败");
        }
        logger.info("新建课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
    @PostMapping("/savesubject")
    public void saveSubject(@RequestBody XbSubject xbsubject,HttpServletResponse resp){
        logger.info("新建课程");
        xbsubject.setState("0");
        XbSubject entity =  xbSubjectService.saveXbSubject(xbsubject);
        JSONObject jsonObject = new JSONObject();
        if(!StringUtils.isEmpty(entity.getId())){
            jsonObject.put("msg", "新建科目成功");
            jsonObject.put("status","0");
            jsonObject.put("cousrtype",com.alibaba.fastjson.JSONObject.toJSON(entity));
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "新建科目失败");
        }
        logger.info("新建科目返回json参数="+jsonObject.toString());
        resp.setContentType("text/html;charset=UTF-8");
        try {
            resp.getWriter().println(jsonObject.toJSONString());
            resp.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("新建课程结束");
    }
    @PostMapping("/removecourse")
    public void removeCourse(@RequestBody XbCourse xbcourse,HttpServletResponse resp){
        logger.info("删除课程");
        boolean rs = xbCourseService.removeXbCourse(xbcourse);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除课程成功");
            jsonObject.put("course", com.alibaba.fastjson.JSONObject.toJSON(xbcourse));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除课程失败");
        }
        logger.info("删除课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
    @PostMapping("/removecoursetype")
    public void removeCourseType(@RequestBody XbCourseType xbcoursetype,HttpServletResponse resp){
        logger.info("删除课程");
        boolean rs = xbCourseTypeService.removeXbCourseType(xbcoursetype);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除课程类别成功");
            jsonObject.put("cousrtype", com.alibaba.fastjson.JSONObject.toJSON(xbcoursetype));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除课程类别失败");
        }
        logger.info("删除课程返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }
    @PostMapping("/removesubject")
    public void removeSubject(@RequestBody XbSubject xbsubject,HttpServletResponse resp){
        logger.info("删除科目");
        boolean rs = xbSubjectService.removeXbSubject(xbsubject);
        JSONObject jsonObject = new JSONObject();
        if(rs){
            jsonObject.put("msg", "删除科目成功");
            jsonObject.put("cousrtype", com.alibaba.fastjson.JSONObject.toJSON(xbsubject));
            jsonObject.put("status","0");
        }else{
            jsonObject.put("status","1");
            jsonObject.put("msg", "删除科目失败");
        }
        logger.info("删除科目返回json参数="+jsonObject.toString());
        HttpServletUtil.reponseWriter(jsonObject,resp);
        logger.info("新建课程结束");
    }

}
